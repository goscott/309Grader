package testing.curve;

import static org.junit.Assert.*;
import model.curve.Grade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
*
* Class GradeTest is the companion testing class for class <a href=
* Grade.html> Grade </a>.  It implements the following module test plan:
*                                                                         <ul>
*                                                                      <p><li>
*     Phase 1: Unit test the constructor.
*                                                                      <p><li>
*     Phase 2: Unit test the simple access method getName, min, max.
*                                                                      <p><li>
*     Phase 3: Unit test the constructive methods set
*                                                                      <p><li>
*     Phase 4: Unit test the methods equals, compareTo
*                                                                      <p><li>
*     Phase 5: Unit test the range, contains, overlap methods.
*                                                                      <p><li>
*     Phase 6: Repeat phases 1 through 5.
*                                                                        </ul>
* @author Frank Poole
*/
public class GradeTest {
    private Grade grade1, grade2, grade3;
    
    /**
     * Sets up the test fixture. 
     * (Called before every test case method.)
     * @author Frank Poole
     */
    @Before
    public void setUp() {
        grade1 = new Grade("A", 100, 90);
        grade2 = new Grade("B", 90, 80);
        grade3 = new Grade("C", 80, 70);
    }
    
    /**
     * Tears down the test fixture. 
     * (Called after every test case method.)
     * @author Frank Poole
     */
    @After
    public void tearDown() {
        grade1 = null;
        grade2 = null;
        grade3 = null;
    }
    
    /**
     * Unit test equals by calling equals on a grade with a
     * null and non-null grade field.
     *                                                                    <pre>
     *  Test
     *  Case    Input                   Output          Remarks
     * ========================================================================
     *   1      curve == null           false           Null case
     *
     *   2      curve == identical      true            Non-null identical case
     *   
     *   3      curve == different grade false          Non-null different case
     *   
     *                                                                   </pre>
     * @author Frank Poole
     */
    @Test
    public void testEquals() {
        assertFalse(grade1.equals(null));
        assertTrue(grade1.equals(grade1));
        assertFalse(grade1.equals(grade2));
    }
    
    /**
     * Unit test overlap by calling overlap on known grades and asserting the
     * correct relationship.
     *                                                                    <pre>
     *  Test
     *  Case    Input                   Output          Remarks
     * ========================================================================
     *   1      Grade A, A              true            Identical grades case
     *
     *   2      Grade A, B              false           Non-null identical case
     *   
     *   3      Grade B, A              false           Non-null different case
     *   
     *   4      Grade B, B              true            Identical grades case
     *   
     *   5      Grade B, C              false           Non-null different case
     *   
     *                                                                   </pre>
     * @author Frank Poole
     */
    @Test
    public void testOverlap() {
        assertTrue(grade1.overlap(grade1));
        assertFalse(grade1.overlap(grade2));
        assertFalse(grade2.overlap(grade1));
        assertTrue(grade2.overlap(grade2));
        assertFalse(grade2.overlap(grade3));
    }
    
    /**
     * Unit test contains by calling contains on known grades and asserting the
     * correct relationship.
     *                                                                    <pre>
     *  Test
     *  Case    Input                   Output          Remarks
     * ========================================================================
     *   1      Grade A, 100.0          true            Extreme edge case
     *
     *   2      Grade A, 95.5           true            Middle case
     *   
     *   3      Grade A, 90.0           true            Lower edge case
     *   
     *   4      Grade A, 85.5           false           Outside case
     *   
     *                                                                   </pre>
     * @author Frank Poole
     */
    @Test
    public void testContains() {
        assertTrue(grade1.contains(100.0));
        assertTrue(grade1.contains(95.5));
        assertTrue(grade1.contains(90.0));
        assertFalse(grade1.contains(89.999));
    }
    
    /**
     * Unit test range by calling range on known grades and asserting that the
     * range is that grade's maximum minus minimum.
     *                                                                    <pre>
     *  Test
     *  Case    Input                   Output          Remarks
     * ========================================================================
     *   1      Grade A                 true            Range match check
     *
     *   2      Grade B                 true            Range match check
     *   
     *   3      Grade C                 true            Range match check
     *   
     *                                                                   </pre>
     * @author Frank Poole
     */
    @Test
    public void testRange() {
        assertTrue(grade1.range() == grade1.max() - grade1.min());
        assertTrue(grade2.range() == grade2.max() - grade2.min());
        assertTrue(grade3.range() == grade3.max() - grade3.min());
    }
}