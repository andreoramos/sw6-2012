package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.SubProfile;

public class SubProfileFragment extends android.app.ListFragment {
	Guardian guard = Guardian.getInstance();
	ListView thisListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setContentView(R.layout.profile_list);
		// Write Tag = Detail and Text = Detail Opened in the LogCat
		Log.e("Detail", "Detail Opened");

	}

	@Override
	// Start the list empty
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO: Implement bind to the profile chosen by launcher
		setListAdapter(null);
		ListView lv = getListView();
		if (guard.getChild().deleteCheck()) {

			lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View v,
						final int row, long arg3) {
					AlertDialog alertDialog = new AlertDialog.Builder(v
							.getContext()).create();
					alertDialog.setTitle(R.string.delete_subprofile_message);
					alertDialog.setButton(getText(R.string.delete_yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

									guard.getChild().SubProfiles().get(row)
											.delete();
									Toast t = Toast.makeText(getActivity(),
											R.string.delete_subprofile_toast,
											5000); // TODO: Which profile has
													// been deleted?
									t.show();
									reloadSubProfiles();
								}
							});

					alertDialog.setButton2(getText(R.string.delete_no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									// do nothing

								}
							});

					alertDialog.show();
					return true;
				}
			});
		}
	}

	public void reloadSubProfiles() {
		loadSubProfiles();
	}

	/**
	 * Inserts the templates on profile id in the details list
	 * 
	 */
	public void loadSubProfiles() {
		ArrayList<SubProfile> subprofiles = guard.getChild().SubProfiles();
		SubProfileAdapter adapter = new SubProfileAdapter(getActivity(),
				android.R.layout.simple_list_item_1, subprofiles);
		setListAdapter(adapter);
	}

	public void onListItemClick(ListView lv, View view, int position, long id) {
		if (TimerLoader.firstClick) {
			for (int i = 0; i < lv.getChildCount(); i++) {
				lv.getChildAt(i).setBackgroundResource(R.drawable.list);
			}
			TimerLoader.firstClick = false;
		}
		for (int i = 0; i < lv.getChildCount(); i++) {
			lv.getChildAt(i).setSelected(false);
		}
		view.setSelected(true);

		CustomizeFragment fragment = (CustomizeFragment) getFragmentManager()
				.findFragmentById(R.id.customizeFragment);
		fragment.loadSettings(guard.getChild().SubProfiles().get(position));
	}

}
