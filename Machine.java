package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Nitin Nazeer
 */
class Machine {

    /** The number of rotors machine can hold. */
    private final int numRotors;

    /** The number of pawls machine has. */
    private final int pawls;

    /** The current rotors in the machine. */
    private final ArrayList<Rotor> currRotors;

    /** The current plugboard in the machine. */
    private FixedRotor plugboard;

    /**
     * @param numRotorss the number of rotors
     * @param pawlss the number of moving rotors
     * @param alpha the machines alphabet
     * @param allRotors all rotors the machine can choose from
     *  */
    Machine(Alphabet alpha, int numRotorss, int pawlss,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _allRotors = allRotors;
        this.numRotors = numRotorss;
        this.pawls = pawlss;
        this.currRotors = new ArrayList();
        this.plugboard = null;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return this.numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return this.pawls;
    }

    /** Return the number of active rotors currently in the machine. */
    int totalCurrRotors() {
        return this.currRotors.size();
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int index = 0;
        this.currRotors.clear();
        for (String rotorName : rotors) {
            for (Rotor r : _allRotors) {
                if (r.name().equals(rotorName)) {
                    this.currRotors.add(r);
                }
            }
            index += 1;
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i += 1) {
            char cposn = setting.charAt(i);
            this.currRotors.get(i + 1).set(cposn);
        }
    }

    /**
     *
     * @param setting positions to set ringstellungs to
     */
    void setRings(String setting) {
        for (int i = 0; i < setting.length(); i += 1) {
            char cposn = setting.charAt(i);
            this.currRotors.get(i + 1).setRing(cposn);
        }
    }

    /** Set the plugboard to PLUGBOARD. *
     * @param plugboardd the plugboard to set to
     */
    void setPlugboard(Permutation plugboardd) {
        this.plugboard = new FixedRotor("Plugboard", plugboardd);
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        c = this.plugboard.convertForward(c);
        for (int i = this.currRotors.size() - 1; i >= 0; i -= 1) {
            c = this.currRotors.get(i).convertForward(c);
        }
        for (int i = 1; i < this.currRotors.size(); i += 1) {
            c = this.currRotors.get(i).convertBackward(c);
        }
        c = this.plugboard.convertBackward(c);
        return c;
    }

    /**
     *
     * @param c char to convert
     * @return converted char
     */
    char convert(char c) {
        int d = _alphabet.toInt(c);
        d = convert(d);
        return _alphabet.toChar(d);
    }

    /** Advance the rotors that can be advanced. */
    void advanceRotors() {
        boolean[] willAdvance = canAdvance();
        for (int i = 0; i < this.currRotors.size(); i += 1) {
            if (willAdvance[i]) {
                getRotor(i).advance();
            }
        }
    }

    /** Returns whether the rotor at INDEX can advance next turn. */
    boolean[] canAdvance() {
        boolean[] willAdvance = new boolean[totalCurrRotors()];
        for (int i = 0; i < this.currRotors.size(); i += 1) {
            Rotor currRotor = getRotor(i);
            if (i == totalCurrRotors() - 1) {
                willAdvance[i] = currRotor.rotates();
            } else if (i == 0) {
                willAdvance[i] = currRotor.rotates();
            } else {
                Rotor rightRotor = getRotor(i + 1);
                Rotor leftRotor = getRotor(i - 1);
                willAdvance[i] = (rightRotor.atNotch() && currRotor.rotates())
                        || (currRotor.atNotch() && leftRotor.rotates());
            }
        }
        return willAdvance;
    }

    /** @return the current rotor at INDEX. */
    Rotor getRotor(int index) {
        return this.currRotors.get(index);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] retCharArray = new char[msg.length()];
        for (int i = 0; i < msg.length(); i += 1) {
            char c = msg.charAt(i);
            retCharArray[i] = convert(c);
        }
        return new String(retCharArray);
    }

    /**
     *
     * @return string representation of machine
     */
    public String toString() {
        String retMsg = "";
        for (Rotor r : this.currRotors) {
            retMsg += r.toString() + "\n";
        }
        return retMsg;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The collection of rotors I can access. */
    private final Collection<Rotor> _allRotors;
}
