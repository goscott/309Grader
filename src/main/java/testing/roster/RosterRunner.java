package testing.roster;

import testing.Test;

public class RosterRunner extends Test {

	@Override
	public Object[] getTestObjects() {
		Object[] list = {
				new GradedItemTest(),
				new RosterTest(),
				new StudentTest()
			};
			
			return list;
	}

}
