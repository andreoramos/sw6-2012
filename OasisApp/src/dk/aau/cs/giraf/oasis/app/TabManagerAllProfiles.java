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

public class TabManagerAllProfiles extends Fragment implements OnTabChangeListener {

	public static final String TAB_PROFILES = "Profiles";
	public static final String TAB_GUARDIANS = "Guardians";
	public static final String TAB_CHILDREN = "Children";

	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.threetabsview, null);
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

		updateTab(TAB_PROFILES, R.id.threetab_1);
		
		Button b = (Button) getView().findViewById(R.id.b3Tab);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
	}

	private void setupTabs() {
		mTabHost.setup();
		mTabHost.addTab(newTab(TAB_PROFILES, R.string.tab_profiles, R.id.threetab_1));
		mTabHost.addTab(newTab(TAB_GUARDIANS, R.string.tab_guardians, R.id.threetab_2));
		mTabHost.addTab(newTab(TAB_CHILDREN, R.string.tab_children, R.id.threetab_3));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {

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
		if (TAB_PROFILES.equals(tabId)) {
			updateTab(tabId, R.id.threetab_1);
			mCurrentTab = 0;
			return;
		}
		if (TAB_GUARDIANS.equals(tabId)) {
			updateTab(tabId, R.id.threetab_2);
			mCurrentTab = 1;
			return;
		}
		if (TAB_CHILDREN.equals(tabId)) {
			updateTab(tabId, R.id.threetab_3);
			mCurrentTab = 2;
			return;
		}
	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();

		if (TAB_PROFILES.equals(tabId)) {
			fm.beginTransaction()
			.replace(placeholder, new ProfilesFrag(), tabId)
			.commit();
			return;
		}
		if (TAB_GUARDIANS.equals(tabId)) {
			fm.beginTransaction()
			.replace(placeholder, new GuardiansFrag(), tabId)
			.commit();
			return;
		}
		if (TAB_CHILDREN.equals(tabId)) {
			fm.beginTransaction()
			.replace(placeholder, new ChildrenFrag(), tabId)
			.commit();
			return;
		}
	}
}