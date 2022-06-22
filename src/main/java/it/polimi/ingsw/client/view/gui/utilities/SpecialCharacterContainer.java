package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.view.gui.CommandAssembler;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;

import java.util.*;
import java.util.function.Consumer;

public class SpecialCharacterContainer {

    private final Consumer<List<HouseColor>> updateStudents;
    private final int idSpecialCharacter;
    private final CommandAssembler commandAssembler;
    private List<Button> bansImages;
    private Line connection;
    private Parent pane;
    private Map<HouseColor, Integer> students;
    private List<Button> studentsImages;
    private int bansNum;
    private ImageView extraPrice;

    SpecialCharacterContainer(int idSpecialCharacter, CommandAssembler assembler) {
        this.commandAssembler = assembler;
        this.idSpecialCharacter = idSpecialCharacter;
        this.connection = null;
        this.pane = null;
        this.students = null;
        this.studentsImages = null;
        this.bansImages = null;
        this.bansNum = 0;
        this.extraPrice = null;
        updateStudents = students -> {
            for (int index = 0; index < studentsImages.size(); index++) {
                int studentIndex = index;
                studentsImages.get(index).setGraphic(Images.student2d(index < students.size() ? students.get(index) : null));
                studentsImages.get(index).setVisible(index < students.size() && students.get(index) != null);
                studentsImages.get(index).setOnMouseClicked(mouseEvent -> {
                    switch (idSpecialCharacter) {
                        case 1 -> commandAssembler.manageStudentSCFromCardToIslandSelection(students.get(studentIndex));
                        case 7 -> commandAssembler.manageStudentSCSwapCardEntranceSelection(students.get(studentIndex));
                        case 9 -> commandAssembler.manageStudentSCIgnoreColorSelection(students.get(studentIndex));
                        case 10 ->
                                commandAssembler.manageStudentSCSwapCardEntranceDiningRoomSelection(students.get(studentIndex), studentIndex);
                        case 11 ->
                                commandAssembler.manageStudentSCFromCardToDiningRoomSelection(students.get(studentIndex));
                        case 12 -> commandAssembler.manageStudentSCReturnColorSelection(students.get(studentIndex));
                    }
                });
                enableStudentButtons(false);
            }
        };
    }

    void setConnection(Line connection) {
        this.connection = connection;
    }

    public void connect() {
        Platform.runLater(() -> this.connection.setVisible(true));
    }

    public Parent getPane() {
        return pane;
    }

    void setPane(Parent pane) {
        this.pane = pane;
    }

    void setStudents(Map<HouseColor, Integer> students) {
        this.students = new EnumMap<>(students);
        if (studentsImages != null)
            updateStudents(false);
    }

    public List<Button> getStudentsImages() {
        return this.studentsImages;
    }

    void setStudentsImages(List<Button> studentButtons) {
        this.studentsImages = Collections.unmodifiableList(studentButtons);
        if (students != null)
            updateStudents(false);
    }

    public void updateStudents(Map<HouseColor, Integer> entrance) {
        if (entrance == null)
            return;
        for (HouseColor color : HouseColor.values())
            this.students.replace(color, entrance.get(color));
        updateStudents(true);
    }

    private void updateStudents(boolean safe) {
        List<HouseColor> studentsList = studentsToList();
        if (safe)
            Platform.runLater(() -> updateStudents.accept(studentsList));
        else
            updateStudents.accept(studentsList);
    }

    void setBansNum(int bansNum) {
        this.bansNum = bansNum;
    }

    void setBansImages(List<Button> banButtons) {
        this.bansImages = Collections.unmodifiableList(banButtons);
        if (bansNum != 0)
            Platform.runLater(() -> updateBans(this.bansNum));
        else
            updateBans(this.bansNum);
    }

    public void updateBans(Integer bansNum) {
        if (bansNum == null)
            return;
        if (bansNum > 0) Platform.runLater(() -> {
            for (int index = 0; index < bansImages.size(); index++) {
                this.bansImages.get(index).setGraphic(Images.banIcon());
                this.bansImages.get(index).setVisible(index < bansNum);
                this.bansImages.get(index).setOnMouseClicked(mouseEvent -> {
                    if (this.idSpecialCharacter == 5) this.commandAssembler.manageStudentSCBanSelection();
                });
                enableBanButtons(false);
            }
        });
    }

    public void setExtraPrice(boolean alreadyPaid) {
        GridPane gPane = (GridPane) pane.getChildrenUnmodifiable().get(1);
        this.extraPrice = (ImageView) gPane.getChildren().get(0);
        this.extraPrice.setVisible(alreadyPaid);
    }

    public void updateExtraPrice(boolean extraPrice) {
        Platform.runLater(() -> this.extraPrice.setVisible(extraPrice));
        Log.debug("set extra price visibility to " + extraPrice);
    }

    public void enableStudentButtons(boolean enable) {
        if (this.studentsImages == null)
            return;

        for (Button studentButton : this.studentsImages) {
            if (enable)
                studentButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 1px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 2px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
            else
                studentButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
            studentButton.setMouseTransparent(!enable);
        }
    }

    public void enableBanButtons(boolean enable) {
        if (this.bansImages == null)
            return;

        for (Button banButton : this.bansImages) {
            if (enable)
                banButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 1px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 2px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
            else
                banButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
            banButton.setMouseTransparent(!enable);
        }
    }

    public void enableCharacterButton(boolean enable, boolean activePlayer) {
        if (enable) {
            pane.getChildrenUnmodifiable().get(1).setStyle(
                    "-fx-border-color: #66eb66;" +
                            "-fx-border-width: 2px;" +
                            "-fx-padding: 0px;" +
                            "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99.9%, transparent, #66eb66);"
            );
        } else pane.getChildrenUnmodifiable().get(1).setStyle(
                "-fx-border-color: transparent;"
        );
        enableBanButtons(activePlayer && enable);
        enableStudentButtons(activePlayer && enable);
    }

    private List<HouseColor> studentsToList() {
        boolean end = false;
        List<HouseColor> list = new ArrayList<>();

        while (!end) {
            Arrays.stream(HouseColor.values()).forEach(color -> {
                if (list.stream().filter(c -> c.equals(color)).count() < students.get(color)) list.add(color);
            });
            if (list.size() == students.values().stream().mapToInt(i -> i).sum()) end = true;
        }

        return list;
    }
}
