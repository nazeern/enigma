package enigma;
import static enigma.EnigmaException.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Nitin Nazeer
 */
class Alphabet {

    /** The characters in the alphabet. */
    private final String chars;

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated.
     *  @param charss the chars of the alphabet */
    Alphabet(String charss) {
        this.chars = charss;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return this.chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return !(this.chars.indexOf(ch) == -1);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return this.chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return this.chars.indexOf(ch);
    }

    /**
     *
     * @param input the input string
     * @return whether or not alphabet has a duplicate
     */
    static boolean hasDuplicate(String input) {
        for (int i = 0; i < input.length(); i += 1) {
            char checkChar = input.charAt(i);
            for (int j = i + 1; j < input.length(); j += 1) {
                if (checkChar == input.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return string representation of alphabet
     */
    public String toString() {
        return this.chars;
    }
}
