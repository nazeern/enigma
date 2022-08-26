package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Nitin Nazeer
 */
class MovingRotor extends Rotor {

    /** The notches in my Rotor.  */
    private final String notches;

    /**
     * @param name the name of the rotor
     * @param perm the permutation of the rotor
     * @param notchess rotor notches
     */
    MovingRotor(String name, Permutation perm, String notchess) {
        super(name, perm);
        this.notches = notchess;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        char cposn = alphabet().toChar(
                permutation().wrap(this.posn()));
        return this.notches.contains(String.valueOf(cposn));
    }

    @Override
    void advance() {
        if (this.posn() == size() - 1) {
            set(0);
        } else {
            set(this.posn() + 1);
        }
    }
}
