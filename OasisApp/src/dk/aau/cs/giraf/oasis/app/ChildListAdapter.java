package dk.aau.cs.giraf.oasis.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class ChildListAdapter extends BaseExpandableListAdapter {


	private final HashMap<String, ArrayList<Profile>> myData = new HashMap<String, ArrayList<Profile>>();
	private final HashMap<Integer, String> lookUp = new HashMap<Integer, String>();
	private final Context context;
	private LayoutInflater mInflater;
	
	public ChildListAdapter(final Context con)
	{
		context = con;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public boolean AddGroup(final String groupName, final ArrayList<Profile> list)
	{
		final ArrayList<Profile> prev = myData.put(groupName, list);

		if (prev != null)
			return false;

		lookUp.put(myData.size() - 1, groupName);

		notifyDataSetChanged();
		return true;
	}

	@Override
	public Profile getChild(int groupPos, int childPos) 
	{
		if (lookUp.containsKey(groupPos))
		{
			final String m = lookUp.get(groupPos);
			final ArrayList<Profile> data = myData.get(m);

			return data.get(childPos);
		}

		return null;
	}

	@Override
	public long getChildId(int groupPos, int childPos) 
	{
		return 0;
	}

	@Override
	public View getChildView(int groupPos, int childPos, boolean isLastChild, 
			View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.children_list_childitem, null);

			holder = new ViewHolder();
			holder.tvFirstName = (TextView) convertView.findViewById(R.id.tvItemFirstName);
			holder.tvMiddleName = (TextView) convertView.findViewById(R.id.tvItemMiddleName);
			holder.tvSurName = (TextView) convertView.findViewById(R.id.tvItemSurName);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Profile p = getChild(groupPos, childPos);
		holder.tvFirstName.setText("\t" + p.getFirstname());
		holder.tvMiddleName.setText("\t" + p.getMiddlename());
		holder.tvSurName.setText("\t" + p.getSurname());

		return convertView;
	}

	static class ViewHolder {
		TextView tvChildHeader, tvFirstName, tvMiddleName, tvSurName;
	}

	@Override
	public int getChildrenCount(int groupPos) 
	{
		if (lookUp.containsKey(groupPos))
			return myData.get(lookUp.get(groupPos)).size();

		return 0;
	}

	@Override
	public String getGroup(int groupPos) 
	{
		if (lookUp.containsKey(groupPos))
			//			return myData.get(lookUp.get(groupPos));
			return lookUp.get(groupPos);

		return null;
	}

	@Override
	public int getGroupCount() 
	{
		return myData.size();
	}

	@Override
	public long getGroupId(int groupPos) 
	{
		return 0;
	}

	@Override
	public View getGroupView(int groupPos, boolean isExpanded, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.children_list_groupitem, null);

			holder = new ViewHolder();
			holder.tvChildHeader = (TextView) convertView.findViewById(R.id.tvGroupItem);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvChildHeader.setText(getGroup(groupPos));

		return convertView;
	}

	@Override
	public boolean hasStableIds() 
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPos, int childPos) 
	{
		return true;
	}
}
