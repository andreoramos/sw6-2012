package dk.aau.cs.giraf.launcher;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.app.Activity;
import android.content.Intent;

	class ProfileLauncher extends Activity implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            ApplicationInfo app = (ApplicationInfo) parent.getItemAtPosition(position);
            Log.i("GIRAF", "v" + v.getContext().toString());
            Log.i("GIRAF", "p" + ProfileSelectActivity.class);
            Intent profileSelectIntent = new Intent(v.getContext(),ProfileSelectActivity.class);
            profileSelectIntent.putExtra("appComponentName", app.className.getPackageName() + "/" + app.className.getClassName());
			v.getContext().startActivity(profileSelectIntent);
    }  
}