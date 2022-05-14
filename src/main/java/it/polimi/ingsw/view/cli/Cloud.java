package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.colours.DarkGrey;
import it.polimi.ingsw.view.cli.colours.White;
import it.polimi.ingsw.view.cli.coordinates.CloudNewLine;
import it.polimi.ingsw.view.cli.coordinates.CloudReset;
import org.fusesource.jansi.Ansi;

import java.util.List;

import static it.polimi.ingsw.view.cli.Utilities.*;

// ╔[TEMPLATE_CL]╗
// ║   ·······   ║
// ║ ··       ·· ║
// ║·   ● ● ●   ·║
// ║·           ·║
// ║·   ●   ●   ·║
// ║ ··       ·· ║
// ║   ·······   ║
// ║     CL0     ║
// ╚═════════════╝

/**
 * Class used for printing a single cloud.
 * It always returns to the starting point.
 */
public class Cloud {

    private Cloud() {
    }

    /**
     * Prints the cloud.
     *
     * @param id             Id of the cloud.
     * @param students       List of students which are on the cloud.
     * @param studentsNumber Number of students that should be at most on the cloud.
     */
    static Ansi print(int id, List<HouseColor> students, int studentsNumber) {

        Ansi ansi = new Ansi();

        ansi.a(defaultForeground(students));

        // Line #1

        ansi.a("   ·······   ");
        ansi.a(newLine());

        // Line #2

        ansi.a(" ··       ·· ");
        ansi.a(newLine());

        // Line #3

        if (students != null) {
            if (studentsNumber == 4) {
                ansi.a("·   ");
                ansi.a(foreground(getColourFrom(students.get(0))));
                ansi.a("●");
                ansi.a(defaultForeground(students));
                ansi.a("   ");
                ansi.a(foreground(getColourFrom(students.get(1))));
                ansi.a("●");
                ansi.a(defaultForeground(students));
                ansi.a("   ·");
            } else {
                ansi.a("·     ");
                ansi.a(foreground(getColourFrom(students.get(0))));
                ansi.a("●");
                ansi.a(defaultForeground(students));
                ansi.a("     ·");
            }
        } else
            ansi.a(studentsNumber == 4 ? "·   ●   ●   ·" : "·     ●     ·");
        ansi.a(newLine());

        // Line #4

        ansi.a("·           ·");
        ansi.a(newLine());

        // Line #5

        if (students != null) {
            ansi.a("·   ");
            ansi.a(foreground(getColourFrom(students.get(studentsNumber == 4 ? 2 : 1))));
            ansi.a("●");
            ansi.a(defaultForeground(students));
            ansi.a("   ");
            ansi.a(foreground(getColourFrom(students.get(studentsNumber == 4 ? 3 : 2))));
            ansi.a("●");
            ansi.a(defaultForeground(students));
            ansi.a("   ·");
        } else
            ansi.a("·   ●   ●   ·");
        ansi.a(newLine());

        // Line #6

        ansi.a(" ··       ·· ");
        ansi.a(newLine());

        // Line #7

        ansi.a("   ·······   ");
        ansi.a(newLine());

        // Line #8

        ansi.a(bold(true));
        ansi.a("     CL${id}     ".replace("${id}", String.format("%01d", id)));
        ansi.a(bold(false));
        ansi.a(resetCursor());

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     */
    private static Ansi newLine() {
        return moveCursor(CloudNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     */
    private static Ansi resetCursor() {
        return moveCursor(CloudReset.getInstance());
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param students List of students on the cloud.
     */
    private static Ansi defaultForeground(List<HouseColor> students) {
        if (students == null)
            return foreground(DarkGrey.getInstance());
        else
            return foreground(White.getInstance());
    }
}
