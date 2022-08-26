package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Nitin Nazeer
 */


class FixedRotor extends Rotor {

    /** Whether or not the rotor is set. */
    private boolean isSet;

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        this.isSet = false;
    }

    @Override
    void advance() {
        throw error("fixed rotor cannot advance");
    }
}
