package dk.aau.cs.giraf.oasis.app;

import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Department;

public class MyDepartmentsFrag extends ListFragment {

	Helper helper;
	List<Department> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.departments_view, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		helper = new Helper(getActivity().getApplicationContext());
		
		if (MainActivity.guardian != null) {
			list = helper.departmentsHelper.getDepartmentsByProfile(MainActivity.guardian);
			setListAdapter(new DepartmentListAdapter(getActivity().getApplicationContext(), list));
		} else {
			setListAdapter(null);
		}
	}

}
