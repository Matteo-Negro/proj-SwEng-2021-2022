package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.*;

/**
 * Specific effect n.11
 *
 * @author Riccardo Milici
 */

public class PrincessEffect extends Effect {

    private final Map<HouseColor, Integer> students;

    /**
     * Class constructor.
     * It creates an instance of the class containing the map of the students put on the effect card with their respective quantity (initialized at 0).
     */
    public PrincessEffect() {
        students = new HashMap<>();

        students.put(HouseColor.BLUE, 0);
        students.put(HouseColor.GREEN, 0);
        students.put(HouseColor.FUCHSIA, 0);
        students.put(HouseColor.RED, 0);
        students.put(HouseColor.YELLOW, 0);
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param statusStudents
     */
    public PrincessEffect(Map<HouseColor, Integer> statusStudents) {
        this.students = statusStudents;
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public void effect() {

    }

    /**
     * effect() method overload.
     *
     * @param toTake The color of the student to take from the card.
     * @param toPut The color of the student to put on the card.
     */
    public void effect(HouseColor toTake, HouseColor toPut) {
        if (toTake != null) takeStudent(toTake);
        if (toPut != null) addStudent(toPut);
    }

    @Override
    public int getCost() {
        return 2;
    }

    /**
     * Returns the map saved in the students attribute.
     *
     * @return students attribute.
     */
    private Map<HouseColor, Integer> getStudents() {
        return new EnumMap<>(students);
    }

    /**
     * Adds a student, of the color specified by the parameter, to the map saved in the students attribute, increasing the counter mapped with the respective HouseColor.
     *
     * @param color The color of the students to increase.
     */
    private void addStudent(HouseColor color) {
        students.replace(color, students.get(color) + 1);
    }

    /**
     * Deletes a student, of the color specified by the parameter, from the map saved in the students attribute, decreasing the counter mapped with the respective HouseColor.
     * Throws an EmptyStackException  if the counter is already at 0.
     *
     * @param color The color of the students to decrease.
     * @throws EmptyStackException Thrown when there are no students of the specified color on the card.
     */
    private void takeStudent(HouseColor color) throws EmptyStackException {
        if (students.get(color) == 0) throw new EmptyStackException();
        students.replace(color, students.get(color) - 1);
    }

    /**
     * Standard redefinition of "equals" method.
     *
     * @param o Object to compare.
     * @return true if the two objects are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrincessEffect that = (PrincessEffect) o;
        return Objects.equals(students, that.students);
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(students);
    }
}
