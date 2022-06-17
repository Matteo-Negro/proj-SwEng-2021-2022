package it.polimi.ingsw.client.view.gui.updates;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WaitingRoom implements Runnable, Observer {

    private final ClientGui client;
    private final Object lock;
    private final it.polimi.ingsw.client.view.gui.WaitingRoom waitingRoomGUI;

    public WaitingRoom(ClientGui client, it.polimi.ingsw.client.view.gui.WaitingRoom waitingRoomGUI) {
        this.client = client;
        lock = new Object();
        this.waitingRoomGUI = waitingRoomGUI;
    }

    @Override
    public void run() {
        while (client.getController().getClientState().equals(ClientState.GAME_WAITING_ROOM)) {
            synchronized (lock) {
                if (client.getController().getGameModel() != null)
                    this.waitingRoomGUI.update(getPlayers());
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    client.getController().resetGame();
                    Thread.currentThread().interrupt();
                }
            }
        }
        Platform.runLater(client::changeScene);
    }

    private List<Label> getPlayers() {

        List<Label> players = new ArrayList<>();
        List<String> sortedPlayers = client.getController().getGameModel().getWaitingRoom().entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
        int index = sortedPlayers.size();

        for (String player : sortedPlayers) {
            Label label = new Label(player);
            if (index != 1)
                label.setOpaqueInsets(new Insets(0, 0, 10, 0));
            label.setTextFill(Color.rgb(255, 255, 255));
            index--;
            players.add(label);
        }

        return players;
    }

    @Override
    public void notifyUpdate() {
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
    }
}
