package dk.aau.cs.giraf.oasis.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabManagerApp extends Fragment implements OnTabChangeListener {

	public static final String TAB_MYAPP = "MyApp";
	public static final String TAB_MYAPP1 = "MyApp1";
	public static final String TAB_MYAPP2 = "MyApp2";
	public static final String TAB_MYAPP3 = "MyApp3";

	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.tabappview, null);
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		setupTabs();
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(mCurrentTab);

		updateTab(TAB_MYAPP, R.id.tabA_1);

		Button b = (Button) getView().findViewById(R.id.bTabApp);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragParentTab.switchTab(FragParentTab.TABPROFILE);
			}
		});
	}

	private void setupTabs() {
		mTabHost.setup();
		mTabHost.addTab(newTab(TAB_MYAPP, R.string.nameLabel, R.id.tabA_1));
		mTabHost.addTab(newTab(TAB_MYAPP1, R.string.nameLabel, R.id.tabA_2));
		mTabHost.addTab(newTab(TAB_MYAPP2, R.string.nameLabel, R.id.tabA_3));
		mTabHost.addTab(newTab(TAB_MYAPP3, R.string.nameLabel, R.id.tabA_4));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		//		Log.d(TAG, "buildTab(): tag=" + tag);

		View indicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.tab, (ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
		((TextView) indicator.findViewById(R.id.tvtab)).setText(labelId);

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		//		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		if (TAB_MYAPP.equals(tabId)) {
			updateTab(tabId, R.id.tabA_1);
			mCurrentTab = 0;
			return;
		}
		if (TAB_MYAPP1.equals(tabId)) {
			updateTab(tabId, R.id.tabA_2);
			mCurrentTab = 1;
			return;
		}
		if (TAB_MYAPP2.equals(tabId)) {
			updateTab(tabId, R.id.tabA_3);
			mCurrentTab = 2;
			return;
		}
		if (TAB_MYAPP3.equals(tabId)) {
			updateTab(tabId, R.id.tabA_4);
			mCurrentTab = 3;
			return;
		}
	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();

		if (TAB_MYAPP.equals(tabId)) {
			fm.beginTransaction()
			.replace(placeholder, new MyProfileFrag(), tabId)
			.commit();
			return;
		}
		if (TAB_MYAPP1.equals(tabId)) {
			fm.beginTransaction()
			.replace(placeholder, new MyChildrenFrag(), tabId)
			.commit();
			return;
		}
		if (TAB_MYAPP2.equals(tabId)) {
			fm.beginTransaction()
			.replace(placeholder, new MyDepartmentsFrag(), tabId)
			.commit();
			return;
		}
		if (TAB_MYAPP3.equals(tabId)) {
			fm.beginTransaction()
			.replace(placeholder, new MyDepChildrenFrag(), tabId)
			.commit();
			return;
		}
	}
}