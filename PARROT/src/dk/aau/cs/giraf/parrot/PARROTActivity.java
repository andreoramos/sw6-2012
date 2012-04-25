package dk.aau.cs.giraf.parrot;



import java.util.ArrayList;
import java.util.List;

import parrot.Package.R;
import android.R.integer;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.App;
import dk.aau.cs.giraf.oasis.lib.models.Media;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;


public class PARROTActivity extends Activity {

	private static PARROTProfile parrotUser;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main);

		/*//Made by kim
		GridView gridview = (GridView) findViewById(R.id.pictogramgrid);
		gridview.setAdapter(new PictogramAdapter(getUser().getCategoryAt(0), this));

		findViewById(R.id.pictogramgrid).setOnDragListener(new BoxDragListener());
		 */




		//PARROTProfile parrotUser = loadProfile();			
		//TODO replace the temp lines with the above line
		//START TEMP LINES
		Pictogram tempPic= new Pictogram("Koala","/sdcard/Pictures/005.jpg", null, null);
		parrotUser = new PARROTProfile("tempNiels", tempPic);
		Category tempCat = new Category(0,tempPic);
		tempCat.addPictogram(tempPic);
		tempCat.addPictogram(tempPic);
		Pictogram tempPic2 = new Pictogram("Meg", "/sdcard/Pictures/meg.png", null, null);
		tempCat.addPictogram(tempPic2);

		for (int i=0;i<6;i++)
		{
			tempCat.addPictogram(tempPic);
			tempCat.addPictogram(tempPic2);
		}
		parrotUser.addCategory(tempCat);

		Category tempCat2 = new Category(2, tempPic2);
		tempPic = new Pictogram("Bob", "/sdcard/Pictures/007.jpg", null, null);
		tempPic2= new Pictogram("Madeline", "/sdcard/Pictures/003.jpg", null, null);

		for (int i=0;i<6;i++)
		{
			tempCat2.addPictogram(tempPic);
			tempCat2.addPictogram(tempPic2);
		}
		parrotUser.addCategory(tempCat2);
		//END TEMP LINES

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);	//TODO figure out what this does

		Tab tab = actionBar.newTab()
				.setText(R.string.firstTab)
				.setTabListener(new TabListener<SpeechBoardFragment>(this,"speechboard",SpeechBoardFragment.class));
		actionBar.addTab(tab);

		tab = actionBar.newTab()
				.setText(R.string.secondTab)
				.setTabListener(new TabListener<OptionsFragment>(this,"options",OptionsFragment.class));
		actionBar.addTab(tab);

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		AudioPlayer.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		AudioPlayer.open();
		super.onResume();
	}

	public static PARROTProfile getUser()
	{
		return parrotUser;
	}

	public PARROTProfile loadProfile()	//TODO we might want to update the settings reader to this format : category +number | cat_property | value
	{
		//This part of the code is supposed to get a profile from the launcher, and read it from the admin.
		Bundle extras = getIntent().getExtras();
		Profile prof;
		App app;
		Helper help = new Helper(this);
		if(extras !=null)
		{
			prof = help.profilesHelper.getProfileById(extras.getLong("currentChildID"));	//It used to be "currentProfileId"
			app = help.appsHelper.getAppByIds(extras.getLong("currentAppId"), extras.getLong("currentChildID"));
			Pictogram pic = new Pictogram(prof.getFirstname(), prof.getPicture(), null, null);
			PARROTProfile parrotUser = new PARROTProfile(prof.getFirstname(), pic);

			Setting<String, String, String> specialSettings = app.getSettings();//This object might be null //FIXME handle eventual null pointer exception

			//Add all of the categories to the profile
			int number = 0;
			String categoryString=null;
			while (true)
			{
				//Here we read the categories
				categoryString = specialSettings.get(prof.getFirstname()).get("category"+number);
				if(categoryString !=null)		//If the category of that number exists
				{
					String colourString = specialSettings.get(prof.getFirstname()).get("category"+number+"colour");
					int col=Integer.valueOf(colourString);
					String iconString = specialSettings.get(prof.getFirstname()).get("category"+number+"icon");
					parrotUser.addCategory(loadCategory(categoryString,col,iconString,help));
					number++;
				}
				else
				{
					break;
				}

			}

			return parrotUser;
		}
		else
		{
			//If no profile is found, return null.
			//TODO find out if this means that a Guardian is using the PARROT app.
			//It doesn't, it means the launcher has not provided a profile, either due to an error, or because PARROT has been launched outside of GIRAF.
			return null;
		}

	}

	public Category loadCategory(String pictureIDs,int colour,String iconString, Helper help)
	{
		int iconId = Integer.valueOf(iconString);
		Category cat = new Category(colour,loadPictogram(iconId, help));
		ArrayList<Integer> listIDs = getIDsFromString(pictureIDs);
		for(int i = 0; i<listIDs.size();i++)
		{
			cat.addPictogram(loadPictogram(listIDs.get(i),help));
		}
		return cat;
	}

	public Pictogram loadPictogram(int id,Helper help)
	{
		Pictogram pic = null;
		Media media=help.mediaHelper.getSingleMediaById(id); //This is the image media //TODO check type
		List<Media> subMedias =	help.mediaHelper.getSubMediaByMedia(media); //TODO find out if this is ok, or if it needs to be an ArrayList
		Media investigatedMedia;
		String soundPath = null;
		String wordPath = null;
		if(subMedias != null)	//Media files can have a link to a sub-media file, check if this one does.
		{
			for(int i = 0;i<subMedias.size();i++) 		
			{
				investigatedMedia =subMedias.get(i);
				if(investigatedMedia.getMType().equals("SOUND"))
				{
					soundPath = investigatedMedia.getMPath();
				}
				else if(investigatedMedia.getMType().equals("WORD"))
				{
					wordPath = investigatedMedia.getMPath();
				}
			}
		}
		pic = new Pictogram(media.getName(), media.getMPath(), soundPath, wordPath);
		return pic;
	}

	//This function takes a string consisting of IDs, and returns a list of integer IDs instead
	public ArrayList<Integer> getIDsFromString(String IDstring)
	{
		ArrayList<Integer> listOfID = new ArrayList<Integer>();

		if(IDstring !=null && IDstring.charAt(0)!='$' && IDstring.charAt(0)!='#')
		{
			String temp = String.valueOf(IDstring.charAt(0));
			int w = 0;
			while(IDstring.charAt(w)!='$')
			{
				w++;
				if(IDstring.charAt(w)!='#')
				{
					temp = temp+ IDstring.charAt(w);
				}
				else if(IDstring.charAt(w)=='$')
				{
					break;
				}
				else
				{
					listOfID.add(Integer.valueOf(temp));
					w++;
					temp = String.valueOf(IDstring.charAt(w));
				}
			}
		}

		return listOfID;
	}

	public void saveProfile(PARROTProfile user)
	{
		//TODO write this method so that it sends the changes to the admin.
		//Seems easy enough, as the admin functionality will automatically replace the media files.

	}
}