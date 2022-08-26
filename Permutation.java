package enigma;

import java.util.ArrayList;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Nitin Nazeer
 */
class Permutation {

    /** the list of cycles in the permutation. */
    private final ArrayList<Alphabet> permList;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        cycles = cycles.trim();
        cycles = cycles.replaceAll(" ", "");
        if (cycles.length() == 0) {
            cycles = "";
        } else {
            cycles = cycles.substring(1, cycles.length() - 1);
        }

        String[] permArray = cycles.split("[)][(]");
        this.permList = new ArrayList();
        for (String s : permArray) {
            this.permList.add(new Alphabet(s));
        }
    }


    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        this.permList.add(new Alphabet(cycle));
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        char pChar = alphabet().toChar(p);
        char permuted = permute(pChar);
        return alphabet().toInt(permuted);

    }

    /**
     *
     * @param c the character to search for
     * @return the cycle that holds character C
     */
    Alphabet getCycle(char c) {
        for (Alphabet perm : this.permList) {
            if (perm.contains(c)) {
                return perm;
            }
        }
        return null;
    }


    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        char cChar = alphabet().toChar(c);
        char inverted = invert(cChar);
        return alphabet().toInt(inverted);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        Alphabet cycle = getCycle(p);
        if (cycle == null || p == '\n') {
            return p;
        }
        int index = cycle.toInt(p);

        if (index == cycle.size() - 1) {
            return cycle.toChar(0);
        } else {
            return cycle.toChar(index + 1);
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        Alphabet cycle = getCycle(c);
        if (cycle == null || c == '\n') {
            return c;
        }
        int index = cycle.toInt(c);
        if (index == 0) {
            return cycle.toChar(cycle.size() - 1);
        } else {
            return cycle.toChar(index - 1);
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int totalLength = 0;
        for (Alphabet a : this.permList) {
            if (a.size() != 1) {
                totalLength += a.size();
            }
        }
        return totalLength == alphabet().size();
    }

    /**
     *
     * @return the string representation of permutation
     */
    public String toString() {
        String retMsg = "";
        for (Alphabet perm : this.permList) {
            retMsg += perm.toString() + " ";
        }
        return retMsg;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

}
