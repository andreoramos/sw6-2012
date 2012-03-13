package sw6.autism.admin;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class Stats extends ListActivity {

	private DataSource datasource;
	ArrayAdapter<String> adapter;
	List<String> values;
	int _position;
	Intent direct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		datasource = new DataSource(this);

		values = datasource.getAllStats();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			datasource.insertStat("stat", "owner");
			direct = new Intent("sw6.autism.admin.STATS");
			finish();
			startActivity(direct);
			break;
		case R.id.delete:
			datasource.clearStatsTable();
			direct = new Intent("sw6.autism.admin.STATS");
			finish();
			startActivity(direct);
			break;
		}
	}
}
