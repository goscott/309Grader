package testing;

public abstract class Test {
	private final int stackLoc = 4;
	/**
	 * Announces that a test has failed with a message
	 * 
	 * @throws TestFailureException
	 */
	public void fail(String message) {
		Debug.log("Test Failure in " + getTestClass(), message);
	}

	/**
	 * Announces that a test has failed
	 * 
	 * @throws TestFailureException
	 */
	public void fail() {
		fail("");
	}

	private String getTestClass() {
		String name = getClass().getName();
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		return name.substring(name.indexOf('.', name.indexOf('.') + 1) + 1)
				+ "." + stack[stackLoc].getMethodName();
	}
}
