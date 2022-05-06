package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.board.effects.*;
import it.polimi.ingsw.utilities.HouseColor;

import java.util.Map;

/**
 * The character card containing the corresponding effect
 *
 * @author Riccardo Milici
 */

public class SpecialCharacter {

    private final int id;
    private final int effectCost;
    private final Effect assignedEffect;
    private boolean alreadyPaid;
    private boolean paidInRound;
    private boolean isActive;

    /**
     * Class constructor.
     * It creates an instance of the class containing the given specific effect object and identified by the given numeric id.
     *
     * @param id The identification number of the special character card.
     */
    public SpecialCharacter(int id, Map<HouseColor, Integer> students) {
        this.id = id;
        isActive = false;
        alreadyPaid = false;
        paidInRound = false;
        assignedEffect = getEffectBy(id, students);
        effectCost = assignedEffect.getCost();
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param statusId          The identification number of the special character card.
     * @param statusEffectCost  The special character's activation cost.
     * @param statusAlreadyPaid True if the special character has already been payed and it's effect has already been activated during this game.
     * @param statusPaidInRound True if the special character has already been payed and it's effect has already been activated during this round.
     * @param statusIsActive    True if the special character's effect is active.
     */
    public SpecialCharacter(int statusId, int statusEffectCost, boolean statusAlreadyPaid, boolean statusPaidInRound, boolean statusIsActive, Map<HouseColor, Integer> statusStudents) {

        this.id = statusId;
        this.effectCost = statusEffectCost;
        this.assignedEffect = getEffectBy(statusId, statusStudents);
        this.alreadyPaid = statusAlreadyPaid;
        this.paidInRound = statusPaidInRound;
        this.isActive = statusIsActive;
    }

    /**
     * Gets the effect according to it's identification number.
     *
     * @param id The identification number of the effect.
     * @return The required effect.
     */
    private Effect getEffectBy(int id, Map<HouseColor, Integer> students) {
        return switch (id) {
            case 1 -> new MonkEffect(students);
            case 2 -> new FarmerEffect();
            case 3 -> new HeraldEffect();
            case 4 -> new MessengerEffect();
            case 5 -> new HerbalistEffect();
            case 6 -> new CentaurEffect();
            case 7 -> new JesterEffect(students);
            case 8 -> new KnightEffect();
            case 9 -> new MushroomerEffect();
            case 10 -> new MinstrelEffect();
            case 11 -> new PrincessEffect(students);
            case 12 -> new ThiefEffect();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    /**
     * Returns the identification number of the object.
     *
     * @return id attribute
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the actual cost of the effect assigned to the object: the effect's native cost if it has never been activated; the native cost increased by 1 if it has already been activated.
     *
     * @return effectCost or effectCost+1 attribute
     */
    public int getEffectCost() {

        if (alreadyPaid) return effectCost + 1;

        else return effectCost;

    }

    /**
     * Returns the instance of the specific effect assigned to the object.
     *
     * @return assignedEffect attribute
     */
    public Effect getEffect() {
        return assignedEffect;
    }

    /**
     * Activates the specific effect assigned to the object.
     */
    public void activateEffect() {
        alreadyPaid = true;
        paidInRound = true;
        isActive = true;
        getEffect().effect();
    }

    /**
     * Deactivates the specific effect assigned to the object.
     */
    public void cleanEffect() {
        isActive = false;
    }

    /**
     * Sets the "payedInRound" attribute to false.
     */
    public void changedRound() {
        paidInRound = false;
    }

    /**
     * Tells if the card had already been paid.
     *
     * @return if the card had already been paid.
     */
    public boolean isAlreadyPaid() {
        return alreadyPaid;
    }

    /**
     * Tells if the card had been paid in the current round.
     *
     * @return if the card had been paid in the current round.
     */
    public boolean isPaidInRound() {
        return paidInRound;
    }

    /**
     * Tells if the card is currently active.
     *
     * @return if the card is currently active.
     */
    public boolean isActive() {
        return isActive;
    }
}
