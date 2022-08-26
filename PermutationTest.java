package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Nitin Nazeer
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private Alphabet alpha = UPPER;
    private String fox = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG";
    private String fox1 = "PQLXAVMNKUYBWGYRZAOHSYILUPQLTEJCFYD";

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void testPermConstruction() {
        perm = new Permutation(NAVALA.get("I"), UPPER);
    }

    @Test
    public void testLargePermute() {
        alpha = new Alphabet(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_."
        );
        perm = new Permutation(
                "(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) (LM) "
                        + "(PW) (ar) (bd) (co) (ej) (fn) (gt) (hk) (iv) (lm)"
                        + "          (pw) (QZ) (SX) (UY) (qz) (sx) (uy) (_.)",
                alpha);
        checkPerm("large", ".qFlu", "_zNmy");
    }

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkToyTransform() {
        perm = new Permutation(NAVALA.get("I"), UPPER);
        checkPerm("toy", "ABCDE", "EKMFL");
    }

    @Test
    public void checkFoxTransform() {
        perm = new Permutation(NAVALA.get("I"), UPPER);
        checkPerm("fox", fox, fox1);
    }

    @Test
    public void testDerangement() {
        Permutation perm1 = new Permutation(NAVALA.get("I"), UPPER);
        Permutation perm5 = new Permutation(NAVALA.get("V"), UPPER);
        Permutation perm6 = new Permutation(NAVALA.get("VI"), UPPER);
        assertFalse(perm1.derangement());
        assertTrue(perm5.derangement());
        assertTrue(perm6.derangement());
    }
}
