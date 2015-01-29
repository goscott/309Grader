package testing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Test {
	/**
	 * Gets a list of objects that we want to test.
	 * 
	 * Example: If you write 200 test methods in CurveTest.java,
	 * make a new CurveTest, add it to this list, and all the
	 * methods will be tested automatically the next time 
	 * AutomatedTester.java is run
	 *
	 */
	public abstract Object[] getTestObjects();

	/**
	 * Tests all "testing" methods in the given objects. Testing methods have
	 * "test" in the name and take no paramaters
	 * 
	 * @param list
	 *            The objects to test
	 */
	public void test() {
		if (getTestObjects() != null) {
			for (Object obj : getTestObjects()) {
				Class<?> c = obj.getClass();
				for (Method method : c.getDeclaredMethods()) {
					if (method.getName().toLowerCase().contains("test")
							&& method.getParameterTypes().length == 0) {
						Debug.logHeader("Running tests in " + c.getName() + "...");
						try {
							method.invoke(obj);
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}