package com.ayros.historycleaner.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.ayros.historycleaner.R;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity
{
	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabHost = getTabHost();

		TabSpec cleanSpec = tabHost.newTabSpec("Clear");
		cleanSpec.setIndicator("Clear");
		Intent cleanIntent = new Intent(this, CleanActivity.class);
		cleanSpec.setContent(cleanIntent);
		tabHost.addTab(cleanSpec);

		TabSpec profileSpec = tabHost.newTabSpec("Profiles");
		profileSpec.setIndicator("Profiles");
		Intent profileIntent = new Intent(this, ProfileActivity.class);
		profileSpec.setContent(profileIntent);
		tabHost.addTab(profileSpec);

		tabHost.setOnTabChangedListener(new OnTabChangeListener()
		{
			@Override
			public void onTabChanged(String tabId)
			{
				MainActivity.this.invalidateOptionsMenu();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		String tabTag = getTabHost().getCurrentTabTag();
		Activity activity = getLocalActivityManager().getActivity(tabTag);

		return activity.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		String tabTag = getTabHost().getCurrentTabTag();
		Activity activity = getLocalActivityManager().getActivity(tabTag);

		if (activity instanceof CleanActivity || activity instanceof ProfileActivity)
		{
			return activity.onOptionsItemSelected(item);
		}

		return false;
	}
}
