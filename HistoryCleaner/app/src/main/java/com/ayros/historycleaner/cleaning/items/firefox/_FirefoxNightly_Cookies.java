package com.ayros.historycleaner.cleaning.items.firefox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ayros.historycleaner.Globals;
import com.ayros.historycleaner.cleaning.Category;
import com.ayros.historycleaner.cleaning.CleanItem;
import com.ayros.historycleaner.helpers.DBHelper;
import com.ayros.historycleaner.helpers.Logger;

public class _FirefoxNightly_Cookies extends CleanItem
{
	public _FirefoxNightly_Cookies(Category parent)
	{
		super(parent);
	}

	@Override
	public String getDisplayName()
	{
		return "Cookies";
	}

	@Override
	public String getPackageName()
	{
		return "org.mozilla.fennec";
	}

	@Override
	public List<String[]> getSavedData() throws IOException
	{
		String path = FirefoxUtils.getFirefoxDataPath(getPackageName());

		return DBHelper.queryDatabase
		(
			path + "/cookies.sqlite",
			new String[] { "Domain", "Cookie Name", "Cookie Value" },
			"moz_cookies",
			new String[] { "baseDomain", "name", "value" },
			null
		);
	}

	@Override
	public void clean() throws IOException
	{
		String path = FirefoxUtils.getFirefoxDataPath(getPackageName());

		DBHelper.updateDatabase
		(
			path + "/cookies.sqlite",
			new String[]
			{
				"DELETE FROM moz_cookies;",
			}
		);
	}
}