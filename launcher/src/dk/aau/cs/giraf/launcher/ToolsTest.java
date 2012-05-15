package dk.aau.cs.giraf.launcher;

import android.content.Context;
import android.test.AndroidTestCase;
//import android.test.mock.MockContext;

public class ToolsTest extends AndroidTestCase {
	
	public void testisLandscape() {
		//fail("not implemented yet");
		Context context = getContext();
		assertEquals(true, Tools.isLandscape(context));
	}
	
	public void testintToDP() {
		Context context = getContext();
		int input;
		int expectedOutput;
		int actualOutput;
		
		input = 100;
		expectedOutput = 100;
		actualOutput = Tools.intToDP(context, input);
		assertEquals(expectedOutput, actualOutput);
		
		input = 30;
		expectedOutput = 30;
		actualOutput = Tools.intToDP(context, input);
		assertEquals(expectedOutput, actualOutput);
	}

}
