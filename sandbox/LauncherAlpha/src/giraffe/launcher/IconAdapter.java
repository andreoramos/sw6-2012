package giraffe.launcher;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class IconAdapter extends BaseAdapter {
	private int settingsLayoutParams = 85;
	private int settingsPadding = 8;
	
	private Context mContext;
	ArrayList<giraffe.launcher.ApplicationInfo> applications = new ArrayList<giraffe.launcher.ApplicationInfo>();
	
	public IconAdapter(Context c, ArrayList<ApplicationInfo> apps) {
	        mContext = c;
	        applications = apps;
	    }	
	
	//Settings
	public void setSettingsLayoutParams(int arg0){
		settingsLayoutParams = arg0;
	}
	public int getSettingsLayoutParams(){
		return settingsLayoutParams;
	}
	public void setSettingsPadding(int arg0){

	}
	public int getSettingsPadding(){
		return settingsPadding;
	}
	
	//original
	@Override
	public int getCount() {
		return applications.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		
		if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

		imageView.setImageDrawable(applications.get(position).icon);
        return imageView;
        
        
		//dosnt work!
//		//Bubbletrouble
//		final TextView textView = (TextView) convertView.findViewById(R.id.gridview);
//        textView.setCompoundDrawablesWithIntrinsicBounds(null, applications.get(position).icon, null, null);
//        textView.setText(applications.get(position).title);
//		
//        return convertView;
        
	}
}