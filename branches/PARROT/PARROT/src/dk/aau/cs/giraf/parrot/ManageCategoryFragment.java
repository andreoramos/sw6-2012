package dk.aau.cs.giraf.parrot;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.os.Bundle;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
		//ImageView categoryPic = (ImageView) parrent.findViewById(R.id.categorypic);//FIXME Make xml for this to work.

		/*
		ArrayAdapter<String> categoriesArray = new ArrayAdap; //FIXME not sure which constructor to use...

		setListAdapter();
		 */			


		categories.setAdapter(new ListViewAdapter(parrent, R.layout.categoriesitem, profileBeingModified.getCategories()));


		pictograms.setAdapter(new PictogramAdapter(profileBeingModified.getCategoryAt(currentCategoryId), parrent));



		categories.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) 
			{
				//TODO change currentCategoryId

			}
		});


		categories.setOnItemLongClickListener(new OnItemLongClickListener()
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
	}
}
