package testing;

import testing.administration.AdminRunner;
import testing.curve.CurveRunner;
import testing.driver.DriverRunner;
import testing.history.HistoryRunner;
import testing.roster.RosterRunner;
import testing.server.ServerRunner;

public class AutomatedTester {

	public static final TestRunner[] tests = { new ServerRunner(),
			new RosterRunner(), new HistoryRunner(), new DriverRunner(),
			new CurveRunner(), new AdminRunner() };

	/**
	 * Runs every test the is mentioned in the files listed in the list above
	 * 
	 * @param args
	 */
	// TODO Add a bunch of cool reflection
	public static void main(String[] args) {
		Debug.initialize();
		Debug.logHeader("STARTING TESTS");
		for (TestRunner test : tests) {
			test.test();
		}
	}
}
