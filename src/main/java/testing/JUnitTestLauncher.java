package testing;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testing.roster.*;
import testing.administration.*;
import testing.curve.*;
import testing.driver.*;
import testing.history.*;
import testing.server.*;

@RunWith(Suite.class)
@SuiteClasses({ ExporterTest.class, GradedItemTest.class,
		PredictionMathTest.class, RosterTest.class, StudentTest.class,
		UserDBTestJUnit.class, UserTestJUnit.class, CurveTest.class,
		GradeTest.class, GraderTestJUnit.class,
		CourseHistoryTestJUnit.class, TestServerJUnit.class})

public class JUnitTestLauncher {

	public static void main(String[] args) {
		JUnitCore.main("testing.JUnitTestLauncher");
	}

}
