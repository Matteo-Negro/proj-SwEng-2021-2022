package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.exceptions.GameNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The entity containing the tcp connection socket of the client connected to the game server and its input and output streams.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 * @author Matteo Negro
 */
public class User extends Thread {

    private final Object connectedLock;
    private final BufferedReader inputStream;
    private final PrintWriter outputStream;
    private final Server server;
    private final Ping ping;
    private boolean connected;
    private GameController gameController;
    private String username;
    private boolean logged;

    /**
     * Class constructor.
     * Creates an instance of the class, containing the username, tcp socket and its input and output streams.
     *
     * @param socket The user's tcp socket, used to communicate with the game server throw the network.
     * @param server The main server instance.
     * @throws IOException Thrown if an error occurs during the input and output streams' opening.
     */
    public User(Socket socket, Server server) throws IOException {
        this.connected = true;
        this.server = server;
        this.connectedLock = new Object();
        this.ping = new Ping(this);
        this.gameController = null;
        this.username = null;
        this.logged = false;

        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new PrintWriter(socket.getOutputStream());
        socket.setSoTimeout(10000);
    }

    public boolean isLogged() {
        return logged;
    }

    /**
     * Returns the name associated to the user.
     *
     * @return username attribute.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Every time checks if the user is online and when receives a command, processes it.
     */
    @Override
    public void run() {

        JsonObject incomingMessage;

        new Thread(ping).start();

        while (true) {

            synchronized (connectedLock) {
                if (!connected)
                    break;
            }

            try {
                incomingMessage = getCommand();
                if (!incomingMessage.get("type").getAsString().equals("pong") && !incomingMessage.get("type").getAsString().equals("error"))
                    manageCommand(incomingMessage);
            } catch (IOException e) {
                // If socket time out expires.
                disconnected();
            }
        }

        ping.interrupt();
    }

    /**
     * Reads the incoming message from the tcp socket.
     *
     * @return A JsonObject containing the command received.
     * @throws IOException Thrown if an error occurs during the socket input stream read.
     */
    private synchronized JsonObject getCommand() throws IOException {
        return JsonParser.parseString(inputStream.readLine()).getAsJsonObject();
    }

    /**
     * Sends a message to the user client through the tcp socket.
     *
     * @param command A JsonObject containing the command to send.
     */
    public void sendMessage(JsonObject command) {
        synchronized (outputStream) {
            outputStream.println(command.toString());
            outputStream.flush();
        }
    }

    /**
     * Manages the user's command parsing and calls the "createGame(int playersNumber, boolean expertMode)" method or "searchGame(String gameCode)" method if requested.
     *
     * @param command The command to manage.
     */
    private void manageCommand(JsonObject command) {
        switch (command.get("type").getAsString()) {
            case "ping" -> sendMessage(MessageCreator.pong());
            case "gameCreation" -> sendMessage(MessageCreator.gameCreation(Matchmaking.gameCreation(command, server)));
            case "enterGame" -> {
                try {
                    gameController = Matchmaking.enterGame(command.get("code").getAsString(), server);
                } catch (FullGameException | GameNotFoundException e) {
                    gameController = null;
                }
                sendMessage(MessageCreator.enterGame(gameController));
            }
            case "login" -> {
                logged = Matchmaking.login(gameController, command.get("name").getAsString(), this);
                sendMessage(MessageCreator.login(logged));
                if (logged)
                    username = command.get("name").getAsString();
            }
            case "logout" -> removeFromGame();
            case "command" -> {
                switch (command.get("subtype").getAsString()) {
                    case "playAssistant" -> {
                        this.gameController.playAssistantCard(command.get("player").getAsString(), command.get("assistant").getAsInt());
                    }
                    case "move" -> {
                        switch (command.get("pawn").getAsString()) {
                            case "professor" -> {
                                //TODO: check for special character, @RiccardoMilici
                            }
                            case "student" -> this.gameController.moveStudent(command);
                            case "motherNature" -> this.gameController.moveMotherNature(command.get("island").getAsInt());
                        }
                    }
                    case "ban" -> this.gameController.setBan(command.get("island").getAsInt());
                    case "pay" -> this.gameController.paySpecialCharacter(command.get("character").getAsInt());
                    case "refill" -> this.gameController.chooseCloud(command);
                }
            }
            default -> sendMessage(MessageCreator.error("Wrong command."));
        }
    }

    /**
     * Manages the user disconnection after a network problem.
     */
    public void disconnected() {
        synchronized (connectedLock) {
            connected = false;
        }
        removeFromGame();
    }

    /**
     * If the user was in a game, s/he's removed from the game and the username is reset.
     */
    private void removeFromGame() {
        if (gameController == null)
            return;
        gameController.removeUser(this);
        username = null;
    }
}
