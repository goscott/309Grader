package testing.driver;

import testing.Test;

public class DriverRunner extends Test {

	@Override
	public Object[] getTestObjects() {

		Object[] list = {
			new DebugTest(),
			new GraderTest()
		};
		
		return list;
	}

}
