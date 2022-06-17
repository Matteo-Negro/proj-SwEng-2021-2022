package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.*;
import it.polimi.ingsw.utilities.ClientState;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Game implements Update {

    private ClientGui client;

    private Map<String, BoardContainer> boards;
    private List<CloudContainer> clouds;
    private List<IslandContainer> islands;

    @FXML
    private VBox boardsLayout;
    @FXML
    private HBox cloudsLayout;
    @FXML
    private Label id;
    @FXML
    private GridPane islandsLayout;
    @FXML
    private Label phase;
    @FXML
    private Label round;

    private List<Button> islandButtons;
    private List<Button> cloudButtons;
    private List<BoardContainer> boardsList;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.GAME_RUNNING, this);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        Platform.runLater(() -> {
            id.requestFocus();
            id.setText(client.getController().getGameCode());
            round.setText(String.valueOf(client.getController().getGameModel().getRound()));
            phase.setText(client.getController().getClientState().name().toLowerCase(Locale.ROOT));
        });
        new Thread(this::prepareGraphicElements).start();
        new Thread(new it.polimi.ingsw.client.view.gui.updates.Game(client)).start();
    }

    /**
     * Goes back to main menu.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void exit(Event event) {
        EventProcessing.exit(event, client);
    }

    /**
     * Calls the methods that prepare the graphic elements and the buttons.
     */
    private void prepareGraphicElements() {
        addBoards();
        addClouds();
        addIslands();
        activateButtons();
    }

    /**
     * Adds all the boards to the GUI.
     */
    private void addBoards() {
        boards = Boards.get(client.getController().getGameModel());
        this.boardsList = reorder();
        Platform.runLater(() -> {
            Rectangle rectangle;
            boardsLayout.getChildren().clear();
            for (BoardContainer board : boardsList) {
                boardsLayout.getChildren().add(board.getPane());
                VBox.setMargin(board.getPane(), new Insets(10));
                if (boardsList.get(boardsList.size() - 1) != board) {
                    rectangle = Various.rectangle();
                    boardsLayout.getChildren().add(rectangle);
                    VBox.setMargin(rectangle, new Insets(10, 0, 0, 0));
                }
            }
        });
    }

    /**
     * Reorders the boards in order to put in first position the current player's one.
     *
     * @return The sorted list.
     */
    private List<BoardContainer> reorder() {
        List<BoardContainer> list = new ArrayList<>();
        list.add(boards.get(client.getController().getUserName()));
        list.addAll(boards.values().stream().filter(board -> board != list.get(0)).toList());
        return list;
    }

    /**
     * Adds all the clouds to the GUI.
     */
    private void addClouds() {
        clouds = Clouds.get(client.getController().getGameModel().getGameBoard().getClouds(), client.getController().getGameModel().getPlayersNumber());
        Platform.runLater(() -> {
            cloudsLayout.getChildren().clear();
            for (CloudContainer cloud : clouds) {
                cloudsLayout.getChildren().add(cloud.getPane());
            }

        });
    }

    /**
     * Adds all the islands to the GUI.
     */
    private void addIslands() {
        islands = Islands.get(client.getController().getGameModel().getGameBoard());
        Platform.runLater(() -> {
            islandsLayout.getChildren().clear();
            islandsLayout.add(islands.get(0).getPane(), 0, 1);
            for (int index = 1; index < 5; index++) {
                islandsLayout.add(islands.get(index).getPane(), index, 0);
            }
            islandsLayout.add(islands.get(5).getPane(), 5, 1);

            islandsLayout.add(islands.get(6).getPane(), 5, 2);

            for (int index = 7; index < 11; index++) {
                islandsLayout.add(islands.get(index).getPane(), 11 - index, 3);

            }
            islandsLayout.add(islands.get(11).getPane(), 0, 2);
        });
    }

    /**
     * Initializes the buttons.
     */
    private void initializeButtons() {
        for (BoardContainer board : this.boardsList) {
            board.enableEntranceButtons(false);
            board.enableAssistantButtons(false);
            board.enableDiningRoomButton(false);
        }

        this.cloudButtons = new ArrayList<>();
        for (CloudContainer cloud : this.clouds)
            cloudButtons.add((Button) cloud.getPane().getChildrenUnmodifiable().get(2));
        enableCloudButtons(false);

        this.islandButtons = new ArrayList<>();
        for (int islandId = 0; islandId < 12; islandId++)
            islandButtons.add((Button) this.islands.get(islandId).getPane().getChildrenUnmodifiable().get(2));
        enableIslandButtons(false);
    }

    /**
     * Activates the due buttons for the current phase.
     */
    private void activateButtons() {
        Platform.runLater(() -> {
            BoardContainer firstBoard = this.boardsList.get(0);
            initializeButtons();
            if (!this.client.getController().hasCommunicationToken()) {
                firstBoard.enableDiningRoomButton(false);
                firstBoard.enableAssistantButtons(false);
                firstBoard.enableEntranceButtons(false);
                enableIslandButtons(false);
                enableCloudButtons(false);
            } else {
                switch (this.client.getController().getGameModel().getSubphase()) {
                    case PLAY_ASSISTANT -> {
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableAssistantButtons(true);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(false);
                        enableCloudButtons(false);
                    }
                    case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3, MOVE_STUDENT_4 -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(true);
                        firstBoard.enableEntranceButtons(true);
                        enableIslandButtons(true);
                        enableCloudButtons(false);
                    }
                    case MOVE_MOTHER_NATURE -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(true);
                        enableCloudButtons(false);
                    }
                    case CHOOSE_CLOUD -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(false);
                        enableCloudButtons(true);
                    }
                }
            }
        });

    }

    /**
     * Set the island buttons visibility to the given value.
     *
     * @param enable The value to set.
     */
    private void enableIslandButtons(boolean enable) {
        for (Button islandButton : this.islandButtons) islandButton.setVisible(enable);
    }

    /**
     * Sets the cloud buttons visibility to the given value.
     *
     * @param enable The value to set.
     */
    private void enableCloudButtons(boolean enable) {
        for (Button cloudButton : this.cloudButtons) cloudButton.setVisible(enable);
    }
}
