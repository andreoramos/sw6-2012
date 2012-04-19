package sw6.oasis.views;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import dk.aau.cs.giraf.oasis.lib.Helper;

public class MainActivity extends FragmentActivity {
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        View main_layout = super.findViewById(android.R.id.content).getRootView();
        main_layout.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);

		super.setContentView(R.layout.main);
		
		Helper helper = new Helper(this);
		helper.CreateDummyData();
		this.initialisePaging();
	}

	/**
	 * Initialize the fragments to be paged
	 */
	private void initialisePaging() {
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, ViewAuthUsers.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewProfiles.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewHasGuardian.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewDepartments.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewHasSubDepartment.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewHasDepartment.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewApps.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewListOfApps.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewMedia.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewHasLink.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewMediaProfileAccess.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewMediaDepartmentAccess.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewTags.class.getName()));
		fragments.add(Fragment.instantiate(this, ViewHasTag.class.getName()));
		
		fragments.add(Fragment.instantiate(this, ViewMedia.class.getName()));
		
		this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

		ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPagerAdapter);
	}
}