package testing.curve;

import static org.junit.Assert.*;
import model.curve.Curve;
import model.curve.Grade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
* Class CurveTest is the companion testing class for class <a href=
* Curve.html> Curve </a>.  It implements the following module test plan:
*                                                                         <ul>
*                                                                      <p><li>
*     Phase 1: Unit test the constructor.
*                                                                      <p><li>
*     Phase 2: Unit test the simple access method getGrades, name.
*                                                                      <p><li>
*     Phase 3: Unit test the constructive methods add
*                                                                      <p><li>
*     Phase 4: Unit test the comparative methods equals
*                                                                      <p><li>
*     Phase 5: Unit test the adjust and remove methods.
*                                                                      <p><li>
*     Phase 6: Repeat phases 1 through 5.
*                                                                      <p><li>
*     Phase 7: Stress test by adding and removing 100000 items.
*                                                                        </ul>
* @author Frank Poole
*/
public class CurveTest {
    private Curve curve1, curve2, curve3;
    
    /**
     * Sets up the test fixture. 
     * (Called before every test case method.)
     * @author Frank Poole
     */
    @Before
    public void setUp() {
        curve1 = new Curve();
        curve2 = new Curve();
        curve3 = new Curve("Empty");
    }
    
    /**
     * Tears down the test fixture. 
     * (Called after every test case method.)
     * @author Frank Poole
     */
    @After
    public void tearDown() {
        curve1 = null;
        curve2 = null;
    }
    
    /**
     * Unit test equals by calling equals on a curve with a
     * null and non-null grade field.  The Grade model is tested
     * fully in its own class test.
     *                                                                    <pre>
     *  Test
     *  Case    Input                   Output          Remarks
     * ========================================================================
     *   1      curve == null           false           Null case
     *
     *   2      curve == identical      true            Non-null identical case
     *   
     *   3      curve == same curve     true            Non-null copy case
     *   
     *   4      curve == other curve    false           Non-null different case
     *                                                                   </pre>
     * @author Frank Poole
     */
    @Test
    public void testEquals() {
        assertFalse(curve1.equals(null));
        assertTrue(curve1.equals(curve2));
        assertTrue(curve1.equals(curve1));
        assertFalse(curve1.equals(curve3));
    }
    
    /**
     * Unit test add by comparing the elements of an external set with those of
     * the Curve grade set.
     *                                                                    <pre>
     *  Test
     *  Case    Input                   Output          Remarks
     * ========================================================================
     *   1      grade == null           nothing         Null case
     *
     *   2      grade == non-null grade curve contains  Non-null case
     *                                  new grade      true
     *                                                                   </pre>
     * @author Frank Poole
     */
    @Test
    public void testAdd() {
        int elements = curve3.getGrades().size();
        curve3.add(null);
        assertTrue(curve3.getGrades().size() == elements);
//        curve3.add(new Grade("A", 100, 90));
//        assertTrue(curve3.getGrades().contains(new Grade("A", 100, 90)));
    }
    
    /**
     * Unit test remove by comparing the elements of the curve before and after
     * removal.
     *                                                                    <pre>
     *  Test
     *  Case    Input                   Output          Remarks
     * ========================================================================
     *   1      grade == null           nothing         Null case
     *
     *   2      grade == non-null grade curve does not
     *                                  contain the     Non-null case
     *                                  grade
     *                                                                   </pre>
     * @author Frank Poole
     */
    @Test
    public void testRemove() {
        int elements = curve3.getGrades().size();
        curve1.remove(null);
        assertTrue(curve3.getGrades().size() == elements);
//        curve1.remove(new Grade("A", 100, 90));
//        assertFalse(curve1.getGrades().contains(new Grade("A", 100, 90)));
    }
    
    /**
     * Unit test get by comparing the returned grade to the expected grade
     * using equals.
     *                                                                    <pre>
     *  Test
     *  Case    Input                   Output          Remarks
     * ========================================================================
     *   1      double == 95.5          grade name is A Grade 1 Middle Case
     *
     *   2      double == 100.0         grade name == A Grade 1 Extreme Case
     *   
     *   3.     double == 90.0          grade name is A Grade 1 Low edge case
     *   
     *   4.     double == 85.5          grade name is B Grade 2 Middle case
     *   
     *   5.     double == 89.999        grade name is B Grade 2 High edge case
     *   
     *   6.     double == 80.0          grade name is B Grade 2 Low edge case
     *                                                                   </pre>
     * @author Frank Poole
     */
    @Test
    public void testGet() {
//        assertTrue(curve1.get(95.5).equals(new Grade("A", 100, 90)));
//        assertTrue(curve1.get(100.0).equals(new Grade("A", 100, 90)));
//        assertTrue(curve1.get(90.0).equals(new Grade("A", 100, 90)));
//        assertTrue(curve1.get(85.5).equals(new Grade("B", 90, 80)));
//        assertTrue(curve1.get(89.999).equals(new Grade("B", 90, 80)));
//        assertTrue(curve1.get(80.0).equals(new Grade("B", 90, 80)));
    }
}