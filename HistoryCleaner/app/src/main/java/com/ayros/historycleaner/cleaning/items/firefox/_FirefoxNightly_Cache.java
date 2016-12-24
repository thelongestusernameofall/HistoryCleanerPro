package com.ayros.historycleaner.cleaning.items.firefox;

import com.ayros.historycleaner.cleaning.Category;
import com.ayros.historycleaner.cleaning.CleanItem;
import com.ayros.historycleaner.helpers.Logger;
import com.ayros.historycleaner.helpers.RootHelper;

import java.io.IOException;

public class _FirefoxNightly_Cache extends CleanItem
{
	public _FirefoxNightly_Cache(Category parent)
	{
		super(parent);
	}

	@Override
	public String getDisplayName()
	{
		return "Cache";
	}

	@Override
	public String getPackageName()
	{
		return "org.mozilla.fennec";
	}

	@Override
	public void clean() throws IOException
	{
		String path = FirefoxUtils.getFirefoxDataPath(getPackageName());

		RootHelper.deleteFileOrFolder(path + "/Cache");
	}
}