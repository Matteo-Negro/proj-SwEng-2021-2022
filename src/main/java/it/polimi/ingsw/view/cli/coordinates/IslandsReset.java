package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Islands: cursor reset.
 */
public class IslandsReset implements DeltaCoordinates {

    private static IslandsReset instance = null;

    private IslandsReset() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -98;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -17;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static IslandsReset getInstance() {
        if (instance == null)
            instance = new IslandsReset();
        return instance;
    }
}
