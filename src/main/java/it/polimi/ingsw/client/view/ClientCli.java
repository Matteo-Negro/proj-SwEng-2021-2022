package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.cli.Autocompletion;
import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.pages.*;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.utilities.Pair;
import org.fusesource.jansi.Ansi;
import org.jline.builtins.Completers;
import org.jline.reader.History;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

public class ClientCli extends Thread implements View {
    private final ClientController controller;
    private final Terminal terminal;
    private final History history;

    /**
     * Default constructor.
     *
     * @throws IOException Thrown if an error occurs during the terminal building.
     */
    public ClientCli() throws IOException {
        this.terminal = TerminalBuilder.terminal();
        this.controller = new ClientController(this);
        Autocompletion.initialize(this.controller);
        this.history = new DefaultHistory();
        updateScreen(false);
    }

    /**
     * The main client method, chooses the correct method to invoke basing on the current controller's state.
     */
    @Override
    public void run() {
        boolean process = true;
        try {
            while (process) {
                //Log.debug(this.getClientState().toString());
                switch (this.controller.getClientState()) {
                    case START_SCREEN -> runStartScreen();
                    case MAIN_MENU -> runMainMenu();
                    case GAME_CREATION -> runGameCreation();
                    case JOIN_GAME -> runJoinGame();
                    case GAME_LOGIN -> runGameLogin();
                    case GAME_WAITING_ROOM -> runWaitingRoom();
                    case GAME_RUNNING -> runGameRunning();
                    case END_GAME -> runEndGame();
                    case CONNECTION_LOST -> this.controller.manageConnectionLost();
                    case EXIT -> process = false;
                }
            }
        } catch (Exception e) {
            Log.error(e);
        } finally {
            updateScreen(true);
            if (this.controller.getGameServer() != null)
                this.controller.getGameServer().disconnected();
        }
    }

    /**
     * Manages the start-screen's I/O.
     */
    public void runStartScreen() throws IOException {
        SplashScreen.print(terminal);
        String hostIp = readLine(" ", terminal, List.of(node("localhost"), node("127.0.0.1")), false, null);
        terminal.writer().print(ansi().restoreCursorPosition());
        terminal.writer().print(ansi().cursorMove(-18, 1));
        terminal.writer().print(ansi().saveCursorPosition());
        terminal.flush();
        int hostTcpPort = Integer.parseInt(readLine(" ", terminal, List.of(node("36803")), false, null));
        try {
            this.controller.manageStartScreen(new Socket(hostIp, hostTcpPort));
        } catch (Exception e) {
            Log.warning(e);
            this.controller.errorOccurred("Wrong data provided or server unreachable.");
        }
    }

    /**
     * Manages the main-menu-screen's I/O.
     */
    public void runMainMenu() {
        MainMenu.print(terminal);
        this.controller.manageMainMenu(readLine(" ", terminal, List.of(node("1"), node("2")), false, null));
        updateScreen(false);
    }

    /**
     * Manages the game-creation-screen's I/O.
     */
    public void runGameCreation() {
        int expectedPlayers;
        boolean expert;

        GameCreation.print(terminal);
        String playersNumber = readLine(" ", terminal, List.of(node("2"), node("3"), node("4"), node("exit")), false, null);
        terminal.writer().print(ansi().restoreCursorPosition());
        terminal.writer().print(ansi().cursorMove(-1, 1));
        terminal.writer().print(ansi().saveCursorPosition());
        terminal.flush();
        switch (playersNumber) {
            case "2", "3", "4" -> expectedPlayers = Integer.parseInt(playersNumber);
            case "exit" -> {
                this.controller.setClientState(ClientStates.MAIN_MENU);
                updateScreen(false);
                this.controller.resetGame();
                return;
            }
            default -> {
                this.controller.errorOccurred("Wrong command.");
                return;
            }
        }

        String difficulty = readLine(" ", terminal, List.of(node("normal"), node("expert"), node("exit")), false, null);
        switch (difficulty) {
            case "normal" -> expert = false;
            case "expert" -> expert = true;
            case "exit" -> {
                this.controller.setClientState(ClientStates.MAIN_MENU);
                updateScreen(false);
                this.controller.resetGame();
                return;
            }
            default -> {
                this.controller.errorOccurred("Wrong command.");
                return;
            }
        }
        this.controller.manageGameCreation(MessageCreator.gameCreation(expectedPlayers, expert));
        updateScreen(false);
    }

    /**
     * Manages the join-game-screen's I/O.
     */
    public void runJoinGame() {
        JoinGame.print(terminal);
        this.controller.manageJoinGame(readLine(" ", terminal, List.of(node("exit")), false, null).toUpperCase(Locale.ROOT));
        updateScreen(false);
    }

    /**
     * Manages the login-screen's I/O.
     */
    public void runGameLogin() {
        Login.print(terminal, this.controller.getGameModel().getWaitingRoom(), this.controller.getGameModel().getPlayersNumber());
        this.controller.manageGameLogin(readLine(" ", terminal, playersToNodes(), false, null));
        updateScreen(false);
    }

    /**
     * Manages the waiting-room-screen's output.
     */
    static int waitingIteration = 0;

    public void runWaitingRoom() {
        if (this.controller.getGameModel() != null) {
            List<String> onlinePlayers = new ArrayList<>();
            for (String name : this.controller.getGameModel().getWaitingRoom().keySet())
                if (Boolean.TRUE.equals(this.controller.getGameModel().getWaitingRoom().get(name)))
                    onlinePlayers.add(name);

            WaitingRoom.print(terminal, onlinePlayers, this.controller.getGameCode(), this.controller.getGameModel().getPlayersNumber(), waitingIteration++);
        }
        synchronized (this.controller.getLock()) {
            try {
                this.controller.getLock().wait();
            } catch (InterruptedException ie) {
                this.controller.resetGame();
            }
        }
        updateScreen(false);
    }


    /**
     * Manages the game-screen's I/O.
     */
    public void runGameRunning() {
        Game.print(terminal, this.controller.getGameModel(), this.controller.getGameCode(), this.controller.getGameModel().getRound(), this.controller.getGameModel().getPlayerByName(controller.getUserName()).isActive());
        if (this.controller.hasCommunicationToken()) {
            this.controller.manageGameRunning(readLine(getPrettyUserName(), terminal, Autocompletion.get(), true, history).toLowerCase(Locale.ROOT));

        } else {
            synchronized (this.controller.getLock()) {
                try {
                    this.controller.getLock().wait(1500);
                } catch (InterruptedException e) {
                    this.controller.resetGame();
                }
            }
        }
        updateScreen(false);
    }

    /**
     * Manages the end-game-screen's I/O.
     */
    public void runEndGame() {
        if (this.controller.getGameModel().isWinner()) WinPage.print(terminal);
        else LosePage.print(terminal);
        this.controller.manageEndGame(readLine(" ", terminal, List.of(node("exit")), false, null));
    }

    /**
     * This method is a visualization helper for CLI command acquisition.
     *
     * @return The decorated username.
     */
    private String getPrettyUserName() {
        Ansi ansi = new Ansi();
        ansi.a(" ");
        ansi.a(foreground(switch (this.controller.getGameModel().getPlayerByName(controller.getUserName()).getWizard()) {
            case FUCHSIA -> WizardFuchsia.getInstance();
            case GREEN -> WizardGreen.getInstance();
            case WHITE -> WizardWhite.getInstance();
            case YELLOW -> WizardYellow.getInstance();
        }));
        ansi.a(bold(true));
        ansi.a(controller.getUserName());
        ansi.a(bold(false));
        ansi.a(foreground(Grey.getInstance()));
        ansi.a(" > ");
        ansi.a(foreground(White.getInstance()));
        return ansi.toString();
    }

    /**
     * Clears the terminal screen.
     *
     * @param def Parameter required by the clearScreen method.
     */
    public void updateScreen(boolean def) {
        clearScreen(terminal, def);
    }

    /**
     * This method is a helper for the runGameLogin method, it grants username autocompletion.
     *
     * @return The completer required by autocompletion.
     */
    private List<Completers.TreeCompleter.Node> playersToNodes() {
        List<Completers.TreeCompleter.Node> nodes = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : this.controller.getGameModel().getWaitingRoom().entrySet())
            if (Boolean.FALSE.equals(entry.getValue()))
                nodes.add(node(entry.getKey()));
        nodes.add(node("exit"));
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Prints an error on screen.
     *
     * @param message The message to show.
     */
    public void printError(String message) {
        Utilities.printError(terminal, message);
    }

    /**
     * Prints an info on screen.
     *
     * @param info The info to show.
     */
    public void printInfo(Pair<String, String> info) {
        Utilities.printInfo(terminal, info.key(), info.value());
    }

}
