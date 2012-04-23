package dk.aau.cs.giraf.parrot;


import java.util.ArrayList;

import parrot.Package.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.drm.DrmManagerClient.OnEventListener;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

public class SpeechBoardFragment extends Fragment
{

	private Activity parrent;
	
	//Remembers the index of the pictogram that is currently being dragged.
	public static int draggedPictogramIndex = -1;
	public static int dragOwnerID =-1;
	//Serves as the back-end storage for the visual speechboard
	public static ArrayList<Pictogram> speechboardPictograms = new ArrayList<Pictogram>();
	public static Category speechBoardCategory = new Category(0x00ff00);


	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.parrent = activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		parrent.setContentView(R.layout.speechboard_layout);

		PARROTProfile user=PARROTActivity.getUser();
		if(user.getCategoryAt(0)!=null)
		{
			Category cat = user.getCategoryAt(0); //TODO we might have to replace this.




			GridView pictogramGrid = (GridView) parrent.findViewById(R.id.pictogramgrid);
			pictogramGrid.setAdapter(new PictogramAdapter(cat, parrent));

			
			GridView sentenceBoardGrid = (GridView) parrent.findViewById(R.id.sentenceboard);
			
			parrent.findViewById(R.id.pictogramgrid).setOnDragListener(new BoxDragListener(parrent));
			parrent.findViewById(R.id.sentenceboard).setOnDragListener(new BoxDragListener(parrent));
			//parrent.findViewById(R.id.supercategory).setOnDragListener(new BoxDragListener(parrent));


			pictogramGrid.setOnItemLongClickListener(new OnItemLongClickListener()
			{

				public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id)
				{
					draggedPictogramIndex = position; //TODO make sure that position is the index of the pictogram
					dragOwnerID = R.id.pictogramgrid;
					ClipData data = ClipData.newPlainText("label", "text"); //TODO Dummy. Pictogram information can be placed here instead.
					DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
					view.startDrag(data, shadowBuilder, view, 0);
					return true;
				}

			});
			
			sentenceBoardGrid.setOnItemLongClickListener(new OnItemLongClickListener()
			{

				public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id)
				{
					draggedPictogramIndex = position; //TODO make sure that position is the index of the pictogram
					dragOwnerID = R.id.sentenceboard;
					ClipData data = ClipData.newPlainText("label", "text"); //TODO Dummy. Pictogram information can be placed here instead.
					DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
					view.startDrag(data, shadowBuilder, view, 0);
					return true;
				}

			});


		}



	}
}

