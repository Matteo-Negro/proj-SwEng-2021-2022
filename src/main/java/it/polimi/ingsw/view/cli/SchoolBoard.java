package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.view.cli.colours.*;
import it.polimi.ingsw.view.cli.coordinates.IslandNewLine;
import it.polimi.ingsw.view.cli.coordinates.IslandReset;
import org.fusesource.jansi.Ansi;

import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.*;

// ╔═════[TEMP_SCHOOLBOARD]═════╗
// ║  NAME                      ║1
// ║ ┌─────┬──────┬─────┬─────┐ ║2
// ║ │ ENT │  DR  │ PRF │ TWR │ ║3
// ║ ├─────┼──────┼─────┼─────┤ ║4
// ║ │ ●x0 │ ●x00 │  ●  │ ●x0 │ ║5
// ║ │     │      │     ├─────┤ ║6
// ║ │ ●x0 │ ●x00 │  ●  │ AST │ ║7
// ║ │     │      │     ├─────┤ ║8
// ║ │ ●x0 │ ●x00 │  ●  │AS-00│ ║9
// ║ │     │      │     ├─────┤ ║10
// ║ │ ●x0 │ ●x00 │  ●  │     │ ║11
// ║ │     │      │     │     │ ║12
// ║ │ ●x0 │ ●x00 │  ●  │     │ ║13
// ║ └─────┴──────┴─────┴─────┘ ║14
// ╚════════════════════════════╝
//
// ╔═══[TEMPL_SCHOOLBOARD_EX]═══╗
// ║  NAME                      ║
// ║ ┌─────┬──────┬─────┬─────┐ ║
// ║ │ ENT │  DR  │ PRF │ TWR │ ║
// ║ ├─────┼──────┼─────┼─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │ ●x0 │ ║
// ║ │     │      │     ├─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │ AST │ ║
// ║ │     │      │     ├─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │AS-00│ ║
// ║ │     │      │     ├─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │ CNS │ ║
// ║ │     │      │     ├─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │ x00 │ ║
// ║ └─────┴──────┴─────┴─────┘ ║
// ╚════════════════════════════╝

/**
 * Class used for printing a single schoolboard.
 * It always returns to the starting point.
 *
 * @Matteo Negro
 */
class SchoolBoard {

    private SchoolBoard() {
    }

    /**
     * Print the school board.
     *
     * @param ansi
     * @param entrance
     * @param diningRoom
     * @param professors
     * @param tower
     * @param towersNumber
     * @param assistant
     * @param coins
     * @param name
     * @param wizard
     * @param active
     * @param exp
     */
    static void print(Ansi ansi,
                      Map<HouseColor, Integer> entrance,
                      Map<HouseColor, Integer> diningRoom,
                      Map<HouseColor, Boolean> professors,
                      TowerType tower,
                      int towersNumber,
                      int assistant,
                      int coins,
                      String name,
                      WizardType wizard,
                      boolean active,
                      boolean exp
    ) {

        int blankLineChars = 26;

        defaultForeground(ansi, active, wizard);

        // Line #1
        String temp = " ";
        if (name.length() > blankLineChars) {
            for (int i = 0; i < blankLineChars - 1; i++) temp += name.charAt(i);
            ansi.append(temp);
        } else {
            for (int i = 0; i < blankLineChars - 1; i++) temp += (i < name.length()) ? name.charAt(i) : " ";
        }
        ansi.append(temp);
        newLine(ansi);

        // Line #2

        ansi.append("┌─────┬──────┬─────┬─────┐");
        newLine(ansi);

        // Line #3

        ansi.append("│ ENT │  DR  │ PRF │ TWR │");
        newLine(ansi);

        // Line #4

        ansi.append("├─────┼──────┼─────┼─────┤");
        newLine(ansi);

        // Line #5

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.GREEN), HouseColor.GREEN, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.GREEN), HouseColor.GREEN, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.GREEN) ? 1 : 0, HouseColor.GREEN, ansi, 0, active, wizard);
        ansi.append(" │ ");
        if (tower != null) {
            switch (tower) {
                case BLACK -> foreground(ansi, TowerBlack.getInstance());
                case GREY -> foreground(ansi, TowerGrey.getInstance());
                case WHITE -> foreground(ansi, TowerWhite.getInstance());
            }
            ansi.append("●");
            defaultForeground(ansi, active, wizard);
            ansi.append(String.format("x%01d", towersNumber));
        }
        ansi.append(" │");
        newLine(ansi);

        // Line #6

        ansi.append("│     │      │     ├─────┤");
        newLine(ansi);

        // Line #7

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.RED), HouseColor.RED, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.RED), HouseColor.RED, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.RED) ? 1 : 0, HouseColor.RED, ansi, 0, active, wizard);
        ansi.append(" │ AST │");
        newLine(ansi);

        // Line #8

        ansi.append("│     │      │     ├─────┤");
        newLine(ansi);

        // Line #9

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.YELLOW), HouseColor.YELLOW, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.YELLOW), HouseColor.YELLOW, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.YELLOW) ? 1 : 0, HouseColor.YELLOW, ansi, 0, active, wizard);
        ansi.append(" │");
        ansi.append(String.format("AST%02d", assistant));
        ansi.append("│");
        newLine(ansi);

        // Line #10

        ansi.append("│     │      │     ├─────┤");
        newLine(ansi);

        // Line #11

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.FUCHSIA), HouseColor.FUCHSIA, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.FUCHSIA), HouseColor.FUCHSIA, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.FUCHSIA) ? 1 : 0, HouseColor.FUCHSIA, ansi, 0, active, wizard);
        if (exp) ansi.append(" │ CNS │");
        else ansi.append(" │     │");
        newLine(ansi);

        // Line #12

        if (exp) ansi.append("│     │      │     ├─────┤");
        else ansi.append("│     │      │     │     │");
        newLine(ansi);

        // Line #13

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.BLUE), HouseColor.BLUE, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.BLUE), HouseColor.BLUE, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.BLUE) ? 1 : 0, HouseColor.BLUE, ansi, 0, active, wizard);
        if (exp) ansi.append(String.format(" │ x%02d │", coins));
        else ansi.append(" │     │");
        newLine(ansi);

        // Line #14
        ansi.append("└─────┴──────┴─────┴─────┘");
        resetCursor(ansi);
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void newLine(Ansi ansi) {
        moveCursor(ansi, IslandNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void resetCursor(Ansi ansi) {
        moveCursor(ansi, IslandReset.getInstance());
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param ansi Ansi stream where to write.
     * @param active A boolean value to set the active player.
     * @param wizard
     */
    private static void defaultForeground(Ansi ansi, boolean active, WizardType wizard) {
        switch (wizard) {
            case FUCHSIA -> foreground(ansi, active ? WizardFuchsia.getInstance() : WizardDarkFuchsia.getInstance());
            case GREEN -> foreground(ansi, active ? WizardGreen.getInstance() : WizardDarkGreen.getInstance());
            case WHITE -> foreground(ansi, active ? WizardWhite.getInstance() : WizardDarkWhite.getInstance());
            case YELLOW -> foreground(ansi, active ? WizardYellow.getInstance() : WizardDarkYellow.getInstance());
        }
    }

    /**
     * Parses the number of students of a specific color in order to render them correctly.
     *
     * @param pawnsNumber
     * @param houseColor
     * @param ansi
     * @param precision
     * @param active
     * @param wizard
     */
    private static void parsePawn(int pawnsNumber, HouseColor houseColor, Ansi ansi, int precision, boolean active, WizardType wizard) {
        switch (precision) {
            case 0 -> {
                foreground(ansi, (pawnsNumber != 0) ? getColourFrom(houseColor) : DarkGrey.getInstance());
                ansi.append("●");
            }
            case 1 -> {
                if (pawnsNumber != 0) {
                    foreground(ansi, getColourFrom(houseColor));
                    ansi.append("●");
                    defaultForeground(ansi, active, wizard);
                    ansi.append(String.format("x%01d", pawnsNumber));
                } else {
                    foreground(ansi, DarkGrey.getInstance());
                    ansi.append("●x0");
                }
            }
            case 2 -> {
                if (pawnsNumber != 0) {
                    foreground(ansi, getColourFrom(houseColor));
                    ansi.append("●");
                    defaultForeground(ansi, active, wizard);
                    ansi.append(String.format("x%02d", pawnsNumber));
                } else {
                    foreground(ansi, DarkGrey.getInstance());
                    ansi.append("●x00");
                }
            }
        }
        defaultForeground(ansi, active, wizard);
    }
}
