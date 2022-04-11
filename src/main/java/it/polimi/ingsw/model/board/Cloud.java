package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Class, it's one of GameBoard elements, here you can find the refill for the SchoolBoard's entrance.
 *
 * @author Matteo Negro
 */

public class Cloud {
    private final int id;
    private final Map<HouseColor, Integer> students;

    /**
     * Cloud Constructor, students is initialized to zero for each color.
     *
     * @param idCloud it's a number in the interval [0;2] or [0;3], it's used to identified the cloud.
     */
    public Cloud(int idCloud) {
        this.id = idCloud;
        this.students = new HashMap<>();
        Arrays.stream(HouseColor.values()).forEach(color -> this.students.put(color, 0));
    }

    /**
     * Cloud Constructor, students is initialized to the saved state.
     *
     * @param idCloud It's a number in the interval [0;2] or [0;3], it's used to identified the cloud.
     * @param status  It's a map that contains the number of student, for each house color, in the last saved status.
     */
    public Cloud(int idCloud, Map<HouseColor, Integer> status) {
        this.id = idCloud;
        this.students = new HashMap<>();
        Arrays.stream(HouseColor.values()).forEach(color -> this.students.put(color, status.get(color)));
    }

    /**
     * This method gives as result the id's island.
     *
     * @return The id's island.
     */
    public int getId() {
        return this.id;
    }

    /**
     * The method gives as result the numbers of students.
     *
     * @return The data structure of the students when the method is called.
     */
    public Map<HouseColor, Integer> getStudents() {
        return new HashMap<>(this.students);
    }

    /**
     * The method returns whether the cloud is full.
     *
     * @return True if it's full.
     */
    public boolean isFull() {
        return Arrays.stream(HouseColor.values()).anyMatch(color -> this.students.get(color) != 0);
    }

    /**
     * The method refills the data structure where students are saved.
     *
     * @param newStudents The students to have to put on the cloud.
     */
    public void refill(Map<HouseColor, Integer> newStudents) {
        Arrays.stream(HouseColor.values()).forEach(color -> this.students.put(color, newStudents.get(color)));
    }

    /**
     * The method cleans the students on the cloud.
     *
     * @return The data structure of the students when the method is called.
     */
    public Map<HouseColor, Integer> flush() {
        Map<HouseColor, Integer> returnMap;

        returnMap = this.getStudents();
        Arrays.stream(HouseColor.values()).forEach(color -> this.students.put(color, 0));

        return returnMap;
    }
}
