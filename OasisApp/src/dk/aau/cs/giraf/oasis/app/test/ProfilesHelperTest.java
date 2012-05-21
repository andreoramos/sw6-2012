package dk.aau.cs.giraf.oasis.app.test;

import android.test.ActivityInstrumentationTestCase2;
import dk.aau.cs.giraf.oasis.app.MainActivity;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class ProfilesHelperTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	
	public ProfilesHelperTest() {
		super("dk.aau.cs.giraf.oasis.app", MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		mActivity = getActivity();
	}
	
	public void testAuthenticateProfile() {
		String certificate = "testprofilejrkpvdfzdxcvjxsuqcbktvncatholiepgczcbjlpbcgqrhmvgzzbbumjradmrwiscvkwnwtzdeopxaxzifovultejikcphyhgzsodxhgmvnggsecqmkfxowulfiuspqjxhrmdpxbrvqxjpwlbtalezrpxqrvjduvtxstaibrptjbztftlwufinbsaeloo";
		Profile expectedProfile = new Profile("Test", "Profile", null, Profile.pRoles.GUARDIAN.ordinal(), 12345678, null, null);
		
		long id = mActivity.helper.profilesHelper.insertProfile(expectedProfile);
		if (id != -1) {
			expectedProfile.setId(id);
			mActivity.helper.profilesHelper.setCertificate(certificate, expectedProfile);
		} else {
			hej jens
		}
		
		
		Profile actualProfile = mActivity.helper.profilesHelper.authenticateProfile(certificate);
		
		assertEquals("Should return profile; Test Profile", expectedProfile, actualProfile);
	}

}
