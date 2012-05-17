package dk.aau.cs.giraf.parrot;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.os.Bundle;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;


public class ManageCategoryFragment extends Fragment {

	private Activity parrent;

	//Remembers the index of the item that is currently being dragged.
	public static int draggedItemIndex = -1;
	public static int catDragOwnerID =-1;
	public static int currentCategoryId = 0; //This is the currrent category that is chosen
	public static PARROTProfile profileBeingModified; 
	public static ArrayList<Pictogram> categories =  new ArrayList<Pictogram>();


	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.parrent = activity;
		ManageCategoryFragment.profileBeingModified = PARROTActivity.getUser();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	public void onResume() {

		super.onResume();
		parrent.setContentView(R.layout.managecategory_layout);

		Spinner profiles = (Spinner) parrent.findViewById(R.id.profiles);
		ListView categories = (ListView) parrent.findViewById(R.id.categories);
		GridView pictograms = (GridView) parrent.findViewById(R.id.pictograms);
		ImageView trash = (ImageView) parrent.findViewById(R.id.trash);
		PARROTDataLoader dummyLoader = new PARROTDataLoader(parrent);
		ImageView categoryPic = (ImageView) parrent.findViewById(R.id.categorypic);
		//createNewCategory, changeCategoryPic, changeCategoryColor, changeCategoryName, copyThisCategoryToOtherProfile, copyThisCategoryToOtherProfileCategory  
		Button createNewCategory = (Button) parrent.findViewById(R.id.createnewcategorybutton);
		Button changeCategoryPic = (Button) parrent.findViewById(R.id.changecategorypicbutton);
		Button changeCategoryColor = (Button) parrent.findViewById(R.id.changecategorycolorbutton);
		Button changeCategoryName = (Button) parrent.findViewById(R.id.changecategorynamebutton);
		Button copyThisCategoryToOtherProfile = (Button) parrent.findViewById(R.id.copythiscategorytootherprofilebutton);
		Button copyThisCategoryToOtherProfileCategory = (Button) parrent.findViewById(R.id.copythiscategorytootherprofilecategorybutton);

		/*
		ArrayAdapter<String> categoriesArray = new ArrayAdap; //FIXME not sure which constructor to use...

		setListAdapter();
		 */			
		
//		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(parrent, dummyLoader.getChildrenFromCurrentGuardian(), android.R.layout.simple_spinner_item); //Adapter for the spinner //FIXME
//		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		profiles.setAdapter(spinnerAdapter);
		
		
		
		categories.setAdapter(new ListViewAdapter(parrent, R.layout.categoriesitem, profileBeingModified.getCategories())); //Adapter for the category gridview

		pictograms.setAdapter(new PictogramAdapter(profileBeingModified.getCategoryAt(currentCategoryId), parrent));
		
		parrent.findViewById(R.id.trash).setOnDragListener(new ManagementBoxDragListener(parrent));
		parrent.findViewById(R.id.categories).setOnDragListener(new ManagementBoxDragListener(parrent));
		parrent.findViewById(R.id.pictograms).setOnDragListener(new ManagementBoxDragListener(parrent));
		
		
		profiles.setOnItemSelectedListener(new OnItemSelectedListener() //Here we chose what profile to show 
		{

			public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) 
			{
				// TODO Auto-generated method stub
				//profileBeingModified = /* profil p� position pladsen i arrayet givet til adapteren*/;
				
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{
				//Do nothing
				
			}
		});

		categories.setOnItemClickListener(new OnItemClickListener() //This is when we want to select a category
		{

			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) 
			{	
				currentCategoryId = position;
				ImageView categoryPic = (ImageView) parrent.findViewById(R.id.categorypic);
				categoryPic.setImageBitmap(ManageCategoryFragment.profileBeingModified.getCategoryAt(currentCategoryId).getIcon().getBitmap());
				GridView pictograms = (GridView) parrent.findViewById(R.id.pictograms);
				pictograms.setAdapter(new PictogramAdapter(profileBeingModified.getCategoryAt(currentCategoryId), parrent));
			}
		});

		categories.setOnItemLongClickListener(new OnItemLongClickListener() //This is when we want to move a category.
		{
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id)
			{
				draggedItemIndex = position;
				catDragOwnerID = R.id.categories;
				ClipData data = ClipData.newPlainText("label", "text"); //TODO Dummy. Pictogram information can be placed here instead.
				DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
				return true;
			}
		});
		

		pictograms.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id)
			{
				draggedItemIndex = position;
				catDragOwnerID = R.id.pictograms;
				ClipData data = ClipData.newPlainText("label", "text"); //TODO Dummy. Pictogram information can be placed here instead.
				DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
				return true;
			}
		});
	
		createNewCategory.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		changeCategoryPic.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		changeCategoryColor.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		changeCategoryName.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		copyThisCategoryToOtherProfile.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		copyThisCategoryToOtherProfileCategory.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				
			}
		});
	
	
	
	
	}
	
//	public void onPause() 
//	{
//		saveProfileChanges(parrent, profileBeingModified);
//	}
	
	public void saveProfileChanges(Activity parrent, PARROTProfile profileBeingModified)
	{
		PARROTDataLoader dataLoader = new PARROTDataLoader(parrent);
		dataLoader.saveProfile(profileBeingModified);
	}
}
