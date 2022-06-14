package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.gui.Start;
import it.polimi.ingsw.client.view.gui.Update;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.Pair;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ClientGui extends Application implements View {

    private static final Object instanceLock = new Object();
    private static final Object sceneLock = new Object();
    private static final String DEFAULT_TITLE = "Eriantys";
    private static final Map<ClientState, Update> instances = new EnumMap<>(ClientState.class);
    private static ClientGui instance = null;
    private Stage stage;
    private ClientController controller;
    private Map<ClientState, Scene> scenes;
    private GameModelObserver modelObserver;

    /**
     * Gets the scene from the name of the FXML file to load.
     *
     * @param scene Name of the FXML file.
     * @return The generated scene.
     * @throws IOException If an error occurs during loading.
     */
    public static Scene loadScene(String scene) throws IOException {
        return new Scene(
                FXMLLoader.load(
                        Objects.requireNonNull(
                                Start.class.getResource(
                                        String.format("/fxml/%s.fxml", scene)))));
    }

    /**
     * Gets the instance of the currently running ClientGUI for interaction between GUI and backend.
     *
     * @return The instance of the currently running ClientGUI
     */
    public static ClientGui getInstance() {
        synchronized (instanceLock) {
            return instance;
        }
    }

    /**
     * Links a state with the specific GUI controller.
     *
     * @param clientState The state to which bound the controller.
     * @param controller  The controller of the scene.
     */
    public static void link(ClientState clientState, Update controller) {
        synchronized (instances) {
            instances.put(clientState, controller);
        }
    }

    /**
     * @param primaryStage the primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {

        synchronized (instanceLock) {
            instance = this;
        }

        stage = primaryStage;
        controller = new ClientController(this);
        scenes = new EnumMap<>(ClientState.class);
        modelObserver = new GameModelObserver(controller);

        Log.info("Initializing scenes...");
        try {
            scenes.put(ClientState.START_SCREEN, loadScene("start"));
        } catch (IOException e) {
            Log.error("Cannot initialize scenes because of the following error: ", e);
            return;
        }

        changeScene();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();

        new Thread(() -> {
            try {
                scenes.put(ClientState.GAME_CREATION, loadScene("create"));
                scenes.put(ClientState.END_GAME, loadScene("end"));
                scenes.put(ClientState.GAME_RUNNING, loadScene("game"));
                scenes.put(ClientState.JOIN_GAME, loadScene("join"));
                scenes.put(ClientState.GAME_LOGIN, loadScene("login"));
                scenes.put(ClientState.MAIN_MENU, loadScene("menu"));
                scenes.put(ClientState.GAME_WAITING_ROOM, loadScene("wait"));
                Log.info("Initialization completed.");
            } catch (IOException e) {
                Log.error("Cannot initialize scenes because of the following error: ", e);
            }
        }).start();
    }

    /**
     * Changes the scene according to the state.
     */
    public void changeScene() {

        ClientState state = getController().getClientState();

        if (state.equals(ClientState.MAIN_MENU))
            synchronized (sceneLock) {
                while (scenes.get(state) == null) {
                    try {
                        sceneLock.wait(100);
                    } catch (InterruptedException e) {
                        Log.debug("ClientGui process has been interrupted.");
                        Thread.currentThread().interrupt();
                    }
                }
            }

        if (getController().getClientState().equals(ClientState.CONNECTION_LOST)) {
            getController().manageConnectionLost();
            return;
        }

        synchronized (instances) {
            if (instances.get(state) != null)
                instances.get(state).prepare();
        }

        Log.info("Displaying " + state);
        stage.setScene(scenes.get(state));

        stage.sizeToScene();
        stage.setResizable(state.equals(ClientState.GAME_RUNNING));

        stage.setTitle(switch (state) {
            case CONNECTION_LOST, START_SCREEN -> "Connect to a game server";
            case END_GAME -> "This is the end";
            case EXIT -> "";
            case GAME_CREATION -> "Create a new game";
            case GAME_LOGIN -> "Login";
            case GAME_RUNNING -> "Do your best! Good luck!";
            case GAME_WAITING_ROOM -> "Waiting for the other players";
            case JOIN_GAME -> "Join a game";
            case MAIN_MENU -> "Menu";
        } + " | " + DEFAULT_TITLE);
    }

    /**
     * Gets the controller.
     *
     * @return The controller.
     */
    public ClientController getController() {
        return controller;
    }

    public  GameModelObserver getModelObserver() {
        return modelObserver;
    }

    /**
     * Prints an error on screen.
     *
     * @param message The message to show.
     */
    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.show();
    }

    /**
     * Prints an info on screen.
     *
     * @param info The info to show.
     */
    @Override
    public void showInfo(Pair<String, String> info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(info.key());
        alert.setHeaderText(info.value());
        alert.show();
    }

    /**
     * Changes the current scene.
     *
     * @param def Boolean parameter used by CLI.
     */
    @Override
    public void updateScreen(boolean def) {
        changeScene();
    }
}
