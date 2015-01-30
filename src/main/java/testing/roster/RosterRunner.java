package testing.roster;

import testing.Test;
import testing.TestRunner;

public class RosterRunner extends TestRunner {

	@Override
	public Test[] getTestObjects() {
		Test[] list = {
				new GradedItemTest(),
				new RosterTest(),
				new StudentTest()
			};
			
			return list;
	}

}
