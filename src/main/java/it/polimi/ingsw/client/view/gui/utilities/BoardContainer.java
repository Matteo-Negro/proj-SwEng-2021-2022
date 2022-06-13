package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.IllegalActionException;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BoardContainer {

    private ImageView assistant;
    private Label coins;
    private Map<HouseColor, Integer> diningRoom;
    private Map<HouseColor, List<ImageView>> diningRoomImages;
    private Map<HouseColor, Integer> entrance;
    private List<ImageView> entranceImages;
    private Parent pane;
    private final Supplier<Void> updateDiningRoom;
    private final Consumer<List<HouseColor>> updateEntrance;
    private WizardType wizard;

    BoardContainer() {
        assistant = null;
        coins = null;
        diningRoom = null;
        diningRoomImages = null;
        entrance = null;
        entranceImages = null;
        pane = null;
        updateDiningRoom = () -> {
            for (HouseColor houseColor : HouseColor.values())
                for (int index = 0; index < 10; index++)
                    diningRoomImages.get(houseColor).get(index).setVisible(index < diningRoom.get(houseColor));
            return null;
        };
        updateEntrance = students -> {
            for (int index = 0; index < entranceImages.size(); index++) {
                entranceImages.get(index).setImage(Images.getStudent2dByColor(students.get(index)));
                entranceImages.get(index).setVisible(students.get(index) != null);
            }
        };
        wizard = null;
    }

    public Parent getPane() {
        return pane;
    }

    void setPane(Parent pane) {
        this.pane = pane;
    }

    public void enable(boolean value) {
        Platform.runLater(() -> {
            if (value)
                pane.setStyle("-fx-background-color: #38a2ed");
            else
                pane.setStyle("-fx-background-color: rgba(255, 255, 255, 0%)");
        });
    }

    void setAssistant(ImageView assistant) {
        this.assistant = assistant;
    }

    public void setAssistant(Integer id) {
        Platform.runLater(() -> {
            if (id == null)
                assistant.setImage(Images.getWizardByType(wizard));
            else
                assistant.setImage(Images.getAssistantById(id));
        });
    }

    void setCoins(Label coins) {
        this.coins = coins;
    }

    public void setCoins(int coins) {
        if (this.coins != null && coins >= 0)
            this.coins.setText(String.format("x%d", coins));
    }

    void setDiningRoom(Map<HouseColor, Integer> diningRoom) {
        this.diningRoom = new EnumMap<>(diningRoom);
        if (diningRoomImages != null)
            updateDiningRoom(false);
    }

    void setDiningRoomImages(Map<HouseColor, List<ImageView>> diningRoom) {
        this.diningRoomImages = Collections.unmodifiableMap(diningRoom);
        if (this.diningRoom != null)
            updateDiningRoom(false);
    }

    public void addToDiningRoom(HouseColor houseColor) throws IllegalActionException {
        if (diningRoom.get(houseColor) == 10)
            throw new IllegalActionException("Already enough students of color " + houseColor.name().toLowerCase(Locale.ROOT));
        diningRoom.replace(houseColor, diningRoom.get(houseColor) + 1);
        updateDiningRoom(true);
    }

    public void removeFromDiningRoom(HouseColor houseColor) throws IllegalActionException {
        if (diningRoom.get(houseColor) == 0)
            throw new IllegalActionException("No more students of color " + houseColor.name().toLowerCase(Locale.ROOT));
        diningRoom.replace(houseColor, diningRoom.get(houseColor) - 1);
        updateDiningRoom(true);
    }

    void setEntrance(Map<HouseColor, Integer> entranceColors) {
        this.entrance = new EnumMap<>(entranceColors);
        if (entranceImages != null)
            updateEntrance(false);
    }

    void setEntranceImages(List<ImageView> entranceImages) {
        this.entranceImages = Collections.unmodifiableList(entranceImages);
        if (entrance != null)
            updateEntrance(false);
    }

    public void addToEntrance(HouseColor houseColor) {
        entrance.replace(houseColor, entrance.get(houseColor) + 1);
        updateEntrance(true);
    }

    public void removeFromEntrance(HouseColor houseColor) throws IllegalActionException {
        if (entrance.get(houseColor) == 0)
            throw new IllegalActionException("No more students of color " + houseColor.name().toLowerCase(Locale.ROOT));
        entrance.replace(houseColor, entrance.get(houseColor) - 1);
        updateEntrance(true);
    }

    void setWizard(WizardType wizard) {
        this.wizard = wizard;
    }

    private List<HouseColor> entranceToList() {
        List<HouseColor> list = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : entrance.entrySet())
            for (int index = 0; index < entry.getValue(); index++)
                list.add(entry.getKey());
        for (int index = 0; index < entranceImages.size() - list.size(); index++)
            list.add(null);
        return list;
    }

    private void updateDiningRoom(boolean safe) {
        if (safe)
            Platform.runLater(updateDiningRoom::get);
        else
            updateDiningRoom.get();
    }

    private void updateEntrance(boolean safe) {
        List<HouseColor> entranceStudents = entranceToList();
        if (safe)
            Platform.runLater(() -> updateEntrance.accept(entranceStudents));
        else
            updateEntrance.accept(entranceStudents);
    }
}
