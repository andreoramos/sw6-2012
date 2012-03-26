package sw6.autism.timer.Wombat;

import java.util.ArrayList;
import java.util.List;

import sw6.oasis.viewmodels.Profile;

public class StartLoader {
	public static String getProfileName(Profile profile) {
		String profileName;

		profileName = profile.getFirstname();
		profileName += " ";
		profileName += profile.getSurname();

		return profileName;
	}

	public static List<Template> getTemplatesById(long id) {
		List<Template> templateList = new ArrayList<Template>();
		
		// TODO: Insert logic which loads templates
		if (id == -2) {
			// "Predefineret" has been chosen
			for (int i = 1; i < 15; i++) {
				templateList.add(new Template(i+1, "Predefineret #" + i));
			}
		} else {
			for (int i = 1; i < 15; i++) {
				templateList.add(new Template(i+1, "Timer #" + i));
			}
		}

		return templateList;
	}
}
