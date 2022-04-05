package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.*;

/**
 * Bag Class, it's one of GameBoard elements, contains all the students before the deployment
 *
 * @author Matteo Negro
 */

public class Bag {
    private Stack<HouseColor> listStudents;

    /**
     * Bag Constructor, listStudents is initialized with the all 130 students and shuffled
     */
    public Bag() {
        this.listStudents = new Stack<>();

        Arrays.stream(HouseColor.values()).forEach(color -> {
            for (int i = 0; i < 24; i++) {
                this.listStudents.push(color);
            }
        });

        this.randomize();
    }

    /**
     * This method gets and removes the first element in the List
     *
     * @return First element in the List
     * @throws EmptyStackException The listStudents is empty
     */
    public HouseColor pop() throws EmptyStackException {
        return this.listStudents.pop();
    }

    /**
     * This method adds a student in the List and shuffles it
     *
     * @param student The student (HouseColor) that the user would like to put back in the bag
     */
    public void push(HouseColor student) {
        this.listStudents.push(student);

        this.randomize();
    }

    /**
     * This method shuffles the students in the List
     */
    public void randomize() {
        Collections.shuffle(this.listStudents);
    }

    /**
     * This method gives as result an arraylist to fill the GambeBoard the first time
     *
     * @return The arraylist with the sequence of students for the setup
     */
    public ArrayList<HouseColor> boardSetUp() {
        ArrayList<HouseColor> setUp = new ArrayList<>();
        Arrays.stream(HouseColor.values()).forEach(color -> {
            setUp.add(color);
            setUp.add(color);
        });

        Collections.shuffle(setUp);
        return setUp;
    }
}