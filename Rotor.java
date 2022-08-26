package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Nitin Nazeer
 */
class Rotor {

    /** The rotor's ringstellung setting. */
    private int ring;

    /** The rotor's setting. */
    private int posn;

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        this.posn = 0;
        this.ring = 0;
    }

    /**
     *
     * @return the current rotor position
     */
    int posn() {
        return this.posn;
    }

    /**
     *
     * @return the current ringstellung position
     */
    int ring() {
        return this.ring;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return this.posn;
    }

    /** Set setting() to POSN.
     * @param posnn the position to set*/
    void set(int posnn) {
        this.posn = permutation().wrap(posnn);
    }

    /** Set setting() to character CPOSN.
     * @ param cposnn the char position to set*/
    void set(char cposn) {
        this.posn = alphabet().toInt(cposn);
    }

    /** Set setting() to POSN.
     * @param ringg the ring setting to set to*/
    void setRing(int ringg) {
        this.ring = permutation().wrap(ringg);
    }

    /** Set setting() to POSN.
     * @param cring the ring setting to set to*/
    void setRing(char cring) {
        this.ring = alphabet().toInt(cring);

    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int contactOut = permutation().permute(p + this.posn - this.ring);
        return permutation().wrap(contactOut - this.posn + this.ring);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int contactOut = permutation().invert(e + this.posn - this.ring);
        return permutation().wrap(contactOut - this.posn + this.ring);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name + ", Position: " + this.posn
                + ", Perm: " + permutation().toString();
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
}
