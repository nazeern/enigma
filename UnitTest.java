package enigma;

import org.junit.Test;
import ucb.junit.textui;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the enigma package.
 *  @author Nitin Nazeer
 */
public class UnitTest {

    private Machine enigma;

    public String[] rotorNames() {
        Set<String> keys = NAVALA.keySet();
        return keys.toArray(new String[keys.size()]);
    }


    public void checkContainsLetter(Alphabet in, String check,
                                    boolean expected) {
        boolean result = in.contains(check.charAt(0));
        assertEquals(result, expected);
    }

    @Test
    public void testAlphabetContains() {
        Alphabet full = new Alphabet();
        Alphabet half = new Alphabet("ABCDEFGHIJKLM");
        checkContainsLetter(full, "Z", true);
        checkContainsLetter(half, "Z", false);
    }

    @Test
    public void testAlphabetSize() {
        Alphabet full = new Alphabet();
        Alphabet half = new Alphabet("ABCDEFGHIJKLM");
        assertEquals(full.size(), 26);
        assertEquals(half.size(), 13);
    }

    @Test
    public void testAlphabetToChar() {
        Alphabet full = new Alphabet();
        Alphabet half = new Alphabet("ABCDEFGHIJKLM");
        char c1 = 'A';
        char c2 = 'Z';
        assertTrue(full.toChar(25) == c2);
        assertTrue(half.toChar(0) == c1);
    }

    @Test
    public void testAlphabetToInt() {
        Alphabet full = new Alphabet();
        Alphabet half = new Alphabet("ABCDEFGHIJKLM");
        char c1 = 'A';
        char c2 = 'Z';
        assertTrue(full.toInt(c1) == 0);
        assertTrue(half.toInt(c1) == 0);
        assertTrue(half.toInt(c2) == -1);
        assertTrue(full.toInt(c2) == 25);
    }

    public ArrayList<Rotor> getRotors(String[] names) {
        ArrayList<Rotor> rotorList = new ArrayList<>();
        for (String rotorName : names) {
            Permutation perm = new Permutation(NAVALA.get(rotorName), UPPER);
            String type = ROTORTYPE.get(rotorName);

            Rotor nextRotor = constructRotor(type, rotorName, perm);
            rotorList.add(nextRotor);
        }
        return rotorList;
    }

    public Rotor constructRotor(String type, String rotorName,
                               Permutation perm) {
        if (type.charAt(0) == 'M') {
            return new MovingRotor(rotorName, perm, type.substring(1));
        } else if (type.equals("N")) {
            return new FixedRotor(rotorName, perm);
        } else {
            return new Reflector(rotorName, perm);
        }
    }

    @Test
    public void setMachine() {
        enigma = new Machine(UPPER, 5, 3,
                getRotors(rotorNames()));
        enigma.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        enigma.setRotors("AXLE");
        enigma.setPlugboard(new Permutation(
                "(HQ) (EX) (IP) (TR) (BY)", UPPER));
    }

    @Test
    public void testMachine() {
        setMachine();
        System.out.println(HIAWATHA);
        String converted = enigma.convert(HIAWATHA);
        System.out.println(converted);

        setMachine();
        System.out.println(enigma.convert(converted));
    }

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(PermutationTest.class,
                                      MovingRotorTest.class));
    }

}


