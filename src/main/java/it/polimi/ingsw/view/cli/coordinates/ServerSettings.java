package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Server settings: cursor position.
 */
public class ServerSettings implements DeltaCoordinates {

    private static ServerSettings instance = null;

    private ServerSettings() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 0;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 4;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static ServerSettings getInstance() {
        if (instance == null)
            instance = new ServerSettings();
        return instance;
    }
}
