package dk.aau.cs.giraf.oasis.app;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Department;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class MyChildrenFrag extends ExpandableListFragment {

	Helper helper;
	List<Profile> list;
	TextView tvHeader;
	ChildListAdapter adapter;
	Profile childProfile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.children_view, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		helper = new Helper(getActivity().getApplicationContext());

		Button b = (Button) getView().findViewById(R.id.bAddProfile);
		b.setVisibility(View.GONE);

		if (MainActivity.guardian != null) {
			updateList();
		} else {
			setListAdapter(null);
		}

		getExpandableListView().setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				MainActivity.child = adapter.getChild(groupPosition, childPosition);
				Intent direct = new Intent(getActivity(), FragParentTab.class);
				direct.putExtra("tabView", FragParentTab.TABCHILD);
				startActivity(direct);
				return false;
			}
		});

		registerForContextMenu(getExpandableListView());

	}

	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo info =
				(ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		int type =
				ExpandableListView.getPackedPositionType(info.packedPosition);
		int group =
				ExpandableListView.getPackedPositionGroup(info.packedPosition);
		int child =
				ExpandableListView.getPackedPositionChild(info.packedPosition);
		//Only create a context menu for child items
		if (type == 1) {
			childProfile = adapter.getChild(group, child);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Fjern Barn");
			builder.setMessage("Sikker p� at du vil fjerne Barnet?");
			builder.setPositiveButton("Ja", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					helper.profilesHelper.removeChildAttachmentToGuardian(childProfile, MainActivity.guardian);
					updateList();
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("Nej", null);
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	private void updateList() {
		adapter = new ChildListAdapter(getActivity().getApplicationContext());

		list = helper.profilesHelper.getChildrenByGuardian(MainActivity.guardian);

		List<Department> depList = helper.departmentsHelper.getDepartments();

		for(Department d : depList) {
			ArrayList<Profile> result = new ArrayList<Profile>();
			List<Profile> pList = helper.profilesHelper.getChildrenByDepartment(d);		

			for(Profile p : pList) {
				for (Profile p2 : list) {
					if (p.getId() == p2.getId()) {
						result.add(p);
					}
				}
			}
			if (!result.isEmpty()) {
				adapter.AddGroup(d, result);
			}
		}

		setListAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateList();
	}

}
