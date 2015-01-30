package testing.driver;

import testing.Test;
import testing.TestRunner;

public class DriverRunner extends TestRunner {

	@Override
	public Test[] getTestObjects() {

		Test[] list = {
			new DebugTest(),
			new GraderTest()
		};
		
		return list;
	}

}
