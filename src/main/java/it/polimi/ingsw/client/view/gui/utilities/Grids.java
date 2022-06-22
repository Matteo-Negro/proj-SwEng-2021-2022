package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.Cloud;
import it.polimi.ingsw.client.model.Island;
import it.polimi.ingsw.client.model.SchoolBoard;
import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.*;

public class Grids {

    private Grids() {
    }

    static AnchorPane board(SchoolBoard schoolBoard, int number, BoardContainer boardContainer) {

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(703);
        anchorPane.setPrefHeight(305);

        GridPane entrance = entrance(schoolBoard.getEntrance(), number, boardContainer);
        AnchorPane.setTopAnchor(entrance, 29.0);
        AnchorPane.setLeftAnchor(entrance, 5.0);
        anchorPane.getChildren().add(entrance);

        GridPane diningRoom = diningRoom(schoolBoard.getDiningRoom(), boardContainer);
        AnchorPane.setTopAnchor(diningRoom, 29.0);
        AnchorPane.setLeftAnchor(diningRoom, 130.0);
        anchorPane.getChildren().add(diningRoom);

        GridPane professors = professors(schoolBoard.getProfessors(), boardContainer);
        AnchorPane.setTopAnchor(professors, 29.0);
        AnchorPane.setLeftAnchor(professors, 503.0);
        anchorPane.getChildren().add(professors);

        GridPane towers = towers(schoolBoard.getTowerType(), schoolBoard.getTowersNumber(), number == 9 ? 6 : 8, boardContainer);
        AnchorPane.setTopAnchor(towers, 61.0);
        AnchorPane.setLeftAnchor(towers, 569.0);
        anchorPane.getChildren().add(towers);

        return anchorPane;
    }

    static GridPane cloud(Cloud cloud, int playersNumber, CloudContainer cloudContainer) {

        GridPane gridPane = new GridPane();
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        gridPane.setPrefWidth(130);
        gridPane.setPrefHeight(130);

        for (int index = 0; index < 3; index++) {
            rows.add(new RowConstraints());
            columns.add(new ColumnConstraints());
        }
        rows.add(new RowConstraints());
        if (playersNumber == 3) columns.add(new ColumnConstraints());

        initialize(rows, columns);

        for (RowConstraints row : rows)
            row.setValignment(VPos.CENTER);

        for (ColumnConstraints column : columns)
            column.setHalignment(HPos.CENTER);

        rows.get(0).setPrefHeight(20);
        rows.get(1).setPrefHeight(40);
        rows.get(2).setPrefHeight(40);
        rows.get(3).setPrefHeight(20);
        columns.get(0).setPrefWidth(20);
        columns.get(1).setPrefWidth(playersNumber == 3 ? 40 : 80);
        columns.get(2).setPrefWidth(playersNumber == 3 ? 40 : 20);
        if (playersNumber == 3)
            columns.get(3).setPrefWidth(20);

        addStudentsToCloud(gridPane, cloud, playersNumber, cloudContainer);

        return gridPane;
    }

    static GridPane specialCharacter(SpecialCharacter specialCharacter, SpecialCharacterContainer specialCharacterContainer) {

        GridPane gridPane = new GridPane();
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        gridPane.setPrefWidth(261);
        gridPane.setPrefHeight(384);

        for (int index = 0; index < 4; index++)
            rows.add(new RowConstraints());

        for (int index = 0; index < 2; index++)
            columns.add(new ColumnConstraints());

        initialize(rows, columns);

        for (RowConstraints row : rows)
            row.setValignment(VPos.CENTER);

        for (ColumnConstraints column : columns)
            column.setHalignment(HPos.CENTER);

        gridPane.getChildren().add(Images.coin());

        processSpecialCharacter(gridPane, specialCharacter, specialCharacterContainer);

        return gridPane;
    }

    static GridPane island(Island island, IslandContainer islandContainer) {

        GridPane gridPane = new GridPane();
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        gridPane.setPrefWidth(162);
        gridPane.setPrefHeight(156);

        for (int index = 0; index < 3; index++) {
            rows.add(new RowConstraints());
            columns.add(new ColumnConstraints());
        }

        initialize(rows, columns);

        rows.get(0).setPrefHeight(30);
        rows.get(1).setPrefHeight(30);
        rows.get(2).setPrefHeight(10);
        columns.get(0).setPrefWidth(20);
        columns.get(1).setPrefWidth(100);
        columns.get(2).setPrefWidth(20);

        gridPane.add(islandTowerMotherNature(island, islandContainer), 1, 0);
        gridPane.add(islandStudents(island, islandContainer), 1, 1);

        return gridPane;
    }

    private static void addStudentsToCloud(GridPane gridPane, Cloud cloud, int playersNumber, CloudContainer cloudContainer) {

        ImageView student;

        if (playersNumber == 3) {

            student = Images.student3d(cloud.getStudents(false) != null ? cloud.getStudents(false).get(0) : null);
            cloudContainer.addStudent(student);
            gridPane.add(student, 1, 1);

            student = Images.student3d(cloud.getStudents(false) != null ? cloud.getStudents(false).get(1) : null);
            cloudContainer.addStudent(student);
            gridPane.add(student, 2, 1);

            student = Images.student3d(cloud.getStudents(false) != null ? cloud.getStudents(false).get(2) : null);
            cloudContainer.addStudent(student);
            gridPane.add(student, 1, 2);

            student = Images.student3d(cloud.getStudents(false) != null ? cloud.getStudents(false).get(3) : null);
            cloudContainer.addStudent(student);
            gridPane.add(student, 2, 2);

        } else {

            student = Images.student3d(cloud.getStudents(false) != null ? cloud.getStudents(false).get(0) : null);
            cloudContainer.addStudent(student);
            gridPane.add(student, 1, 1);

            gridPane.add(cloudStudents(cloud, cloudContainer), 1, 2);
        }
    }

    private static GridPane cloudStudents(Cloud cloud, CloudContainer cloudContainer) {

        GridPane gridPane = new GridPane();
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        rows.add(new RowConstraints());
        columns.add(new ColumnConstraints());
        columns.add(new ColumnConstraints());

        initialize(rows, columns);

        rows.get(0).setValignment(VPos.CENTER);
        columns.get(0).setHalignment(HPos.CENTER);
        columns.get(1).setHalignment(HPos.CENTER);

        ImageView student;

        student = Images.student3d(cloud.getStudents(false) != null ? cloud.getStudents(false).get(1) : null);
        cloudContainer.addStudent(student);
        gridPane.add(student, 0, 0);

        student = Images.student3d(cloud.getStudents(false) != null ? cloud.getStudents(false).get(2) : null);
        cloudContainer.addStudent(student);
        gridPane.add(student, 1, 0);

        return gridPane;
    }

    private static GridPane diningRoom(Map<HouseColor, Integer> diningRoom, BoardContainer boardContainer) {

        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(342);
        gridPane.setPrefHeight(247);
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        RowConstraints rowConstraints;
        for (int index = 0; index < 5; index++) {
            rowConstraints = new RowConstraints();
            rowConstraints.setValignment(VPos.CENTER);
            rows.add(rowConstraints);
        }
        ColumnConstraints columnConstraints;
        for (int index = 0; index < 10; index++) {
            columnConstraints = new ColumnConstraints();
            columnConstraints.setHalignment(HPos.CENTER);
            columns.add(columnConstraints);
        }

        initialize(rows, columns);

        boardContainer.setDiningRoom(diningRoom);
        boardContainer.setDiningRoomImages(initializeDiningRoom(gridPane));
        return gridPane;
    }

    private static GridPane entrance(Map<HouseColor, Integer> entrance, int number, BoardContainer boardContainer) {

        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(81);
        gridPane.setPrefHeight(247);
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        RowConstraints rowConstraints;
        for (int index = 0; index < (number == 7 ? 4 : 5); index++) {
            rowConstraints = new RowConstraints();
            rowConstraints.setValignment(VPos.CENTER);
            rows.add(rowConstraints);
        }
        ColumnConstraints columnConstraints;
        for (int index = 0; index < 2; index++) {
            columnConstraints = new ColumnConstraints();
            columnConstraints.setHalignment(HPos.CENTER);
            columns.add(columnConstraints);
        }

        initialize(rows, columns);

        boardContainer.setEntrance(entrance);
        boardContainer.setEntranceImages(initializeEntranceButtons(gridPane, number));

        return gridPane;
    }

    private static void processSpecialCharacter(GridPane gridPane, SpecialCharacter specialCharacter, SpecialCharacterContainer specialCharacterContainer) {

        if (specialCharacter.getAvailableBans() != null) {
            List<Button> bans = new ArrayList<>();
            ImageView ban;
            for (int index = 0; index < specialCharacter.getAvailableBans(); index++) {
                ban = Images.banIsland();
                ban.setVisible(false);
                Button banButton = new Button("", ban);
                // TODO: check for it
                banButton.setStyle("-fx-background-radius: 50em;" + "-fx-max-width: 10px;" + "-fx-max-height: 10px;" + "-fx-padding: 0px;");
                banButton.setVisible(false);
                bans.add(banButton);
                gridPane.add(banButton, (index % 2 == 0) ? 1 : 0, 3 - index / 2);
            }
            specialCharacterContainer.setBansNum(specialCharacter.getAvailableBans());
            specialCharacterContainer.setBansImages(bans);
        } else if (specialCharacter.getStudents() != null) {
            List<Button> students = new ArrayList<>();
            ImageView imageView;
            for (int index = 0; index < specialCharacter.getStudents().values().stream().mapToInt(Integer::intValue).sum(); index++) {
                imageView = Images.student2d(null);
                imageView.setVisible(false);
                Button studentButton = new Button("", imageView);
                studentButton.setStyle("-fx-background-radius: 50em;" + "-fx-max-width: 10px;" + "-fx-max-height: 10px;" + "-fx-padding: 0px;");
                studentButton.setVisible(false);
                students.add(studentButton);
                gridPane.add(studentButton, (index % 2 == 0) ? 1 : 0, 3 - index / 2);
            }
            specialCharacterContainer.setStudents(specialCharacter.getStudents());
            specialCharacterContainer.setStudentsImages(students);
        } else if (specialCharacter.getId() == 9 || specialCharacter.getId() == 12) {
            List<Button> students = new ArrayList<>();
            ImageView imageView;
            Map<HouseColor, Integer> tmp = new EnumMap<>(HouseColor.class);
            for (int index = 0; index < 5; index++) {
                imageView = Images.student2d(null);
                imageView.setVisible(false);
                Button studentButton = new Button("", imageView);
                studentButton.setStyle("-fx-background-radius: 50em;" + "-fx-max-width: 10px;" + "-fx-max-height: 10px;" + "-fx-padding: 0px;");
                studentButton.setVisible(false);
                students.add(studentButton);
                gridPane.add(studentButton, (index % 2 == 0) ? 1 : 0, 3 - index / 2);
            }
            Arrays.stream(HouseColor.values()).forEach(c -> tmp.put(c, 1));
            specialCharacterContainer.setStudents(tmp);
            specialCharacterContainer.setStudentsImages(students);
        }
    }

    private static GridPane professors(Map<HouseColor, Boolean> professors, BoardContainer boardContainer) {

        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(37);
        gridPane.setPrefHeight(247);
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        RowConstraints rowConstraints;
        for (int index = 0; index < 5; index++) {
            rowConstraints = new RowConstraints();
            rowConstraints.setValignment(VPos.CENTER);
            rows.add(rowConstraints);
        }
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columns.add(columnConstraints);

        initialize(rows, columns);

        Map<HouseColor, ImageView> professorsImages = initializeProfessors(gridPane);
        for (Map.Entry<HouseColor, Boolean> professor : professors.entrySet())
            professorsImages.get(professor.getKey()).setVisible(professor.getValue());

        boardContainer.setProfessors(professorsImages);

        return gridPane;
    }

    private static GridPane towers(TowerType towerType, int towersToShow, int towersNumber, BoardContainer boardContainer) {

        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(111);
        gridPane.setPrefHeight(168);
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        RowConstraints rowConstraints;
        for (int index = 0; index < towersNumber / 2; index++) {
            rowConstraints = new RowConstraints();
            rowConstraints.setValignment(VPos.BOTTOM);
            rows.add(rowConstraints);
        }
        ColumnConstraints columnConstraints;
        for (int index = 0; index < 2; index++) {
            columnConstraints = new ColumnConstraints();
            columnConstraints.setHalignment(HPos.CENTER);
            columns.add(columnConstraints);
        }

        initialize(rows, columns);

        List<ImageView> towers = initializeTowers(gridPane, towerType, towersNumber, towersToShow);
        boardContainer.setTowers(towers);

        return gridPane;
    }

    private static GridPane islandTowerMotherNature(Island island, IslandContainer islandContainer) {

        GridPane gridPane = new GridPane();
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        rows.add(new RowConstraints());
        columns.add(new ColumnConstraints());
        columns.add(new ColumnConstraints());

        initialize(rows, columns);

        rows.get(0).setValignment(VPos.BOTTOM);
        columns.get(0).setHalignment(HPos.CENTER);
        columns.get(1).setHalignment(HPos.CENTER);

        ImageView tower = Images.towerRealm(island.getTower());
        islandContainer.setTower(tower);
        gridPane.add(tower, 0, 0);

        ImageView motherNature = Images.motherNature(island.hasMotherNature());
        islandContainer.setMotherNature(motherNature);
        gridPane.add(motherNature, 1, 0);

        return gridPane;
    }

    private static GridPane islandStudents(Island island, IslandContainer islandContainer) {

        GridPane gridPane = new GridPane();
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        rows.add(new RowConstraints());
        rows.add(new RowConstraints());
        columns.add(new ColumnConstraints());

        initialize(rows, columns);

        gridPane.add(islandStudents(0, island, islandContainer), 0, 0);
        gridPane.add(islandStudents(1, island, islandContainer), 0, 1);

        return gridPane;
    }

    private static GridPane islandStudents(int row, Island island, IslandContainer islandContainer) {

        GridPane gridPane = new GridPane();
        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        rows.add(new RowConstraints());

        for (int index = 0; index < 3 - row; index++)
            columns.add(new ColumnConstraints());

        initialize(rows, columns);

        if (row == 0) {
            gridPane.add(Boxes.islandStudent(HouseColor.GREEN, island.getStudents().get(HouseColor.GREEN), islandContainer), 0, 0);
            gridPane.add(Boxes.islandStudent(HouseColor.RED, island.getStudents().get(HouseColor.RED), islandContainer), 1, 0);
            gridPane.add(Boxes.islandStudent(HouseColor.YELLOW, island.getStudents().get(HouseColor.YELLOW), islandContainer), 2, 0);
        } else {
            gridPane.add(Boxes.islandStudent(HouseColor.FUCHSIA, island.getStudents().get(HouseColor.FUCHSIA), islandContainer), 0, 0);
            gridPane.add(Boxes.islandStudent(HouseColor.BLUE, island.getStudents().get(HouseColor.BLUE), islandContainer), 1, 0);
        }

        return gridPane;
    }

    private static void initialize(ObservableList<RowConstraints> rows, ObservableList<ColumnConstraints> columns) {

        for (RowConstraints row : rows) {
            row.setVgrow(Priority.SOMETIMES);
            row.setMinHeight(0);
        }

        for (ColumnConstraints column : columns) {
            column.setHgrow(Priority.SOMETIMES);
            column.setMinWidth(0);
        }
    }

    private static Map<HouseColor, List<ImageView>> initializeDiningRoom(GridPane gridPane) {
        Map<HouseColor, List<ImageView>> students = new EnumMap<>(HouseColor.class);
        List<ImageView> colorStudents;
        ImageView imageView;
        for (HouseColor color : HouseColor.values()) {
            colorStudents = new ArrayList<>();
            for (int index = 0; index < 10; index++) {
                imageView = Images.student2d(color);
                imageView.setVisible(false);
                colorStudents.add(imageView);
                gridPane.add(imageView, index, switch (color) {
                    case BLUE -> 4;
                    case FUCHSIA -> 3;
                    case GREEN -> 0;
                    case RED -> 1;
                    case YELLOW -> 2;
                });
            }
            students.put(color, colorStudents);
        }
        return students;
    }

    private static List<Button> initializeEntranceButtons(GridPane gridPane, int number) {
        List<Button> students = new ArrayList<>();
        ImageView imageView;
        for (int index = 0; index < number; index++) {
            imageView = Images.student2d(null);
            imageView.setVisible(false);
            Button studentButton = new Button("", imageView);
            studentButton.setStyle("-fx-background-radius: 50em;" + "-fx-max-width: 10px;" + "-fx-max-height: 10px;" + "-fx-padding: 0px;");
            studentButton.setVisible(false);
            students.add(studentButton);
            gridPane.add(studentButton, 1 - index % 2, (number == 7 ? 3 : 4) - index / 2);
        }
        return students;
    }

    private static Map<HouseColor, ImageView> initializeProfessors(GridPane gridPane) {
        Map<HouseColor, ImageView> professors = new EnumMap<>(HouseColor.class);
        List<HouseColor> colors = List.of(HouseColor.GREEN, HouseColor.RED, HouseColor.YELLOW, HouseColor.FUCHSIA, HouseColor.BLUE);
        ImageView imageView;
        for (int index = 0; index < 5; index++) {
            imageView = Images.professor(colors.get(index));
            imageView.setVisible(false);
            professors.put(colors.get(index), imageView);
            gridPane.add(imageView, 0, index);
        }
        return professors;
    }

    private static List<ImageView> initializeTowers(GridPane gridPane, TowerType towerType, int towersNumber, int towersToShow) {
        List<ImageView> towers = new ArrayList<>();
        ImageView imageView;
        for (int index = 0; index < towersNumber; index++) {
            imageView = Images.towerBoard(towerType);
            imageView.setVisible(index < towersToShow);
            towers.add(imageView);
        }
        List<ImageView> reverseTowers = new ArrayList<>(towers);
        Collections.reverse(reverseTowers);
        for (int index = 0; index < towersNumber; index++)
            gridPane.add(reverseTowers.get(index), 1 - index % 2, index / 2);
        return towers;
    }
}
