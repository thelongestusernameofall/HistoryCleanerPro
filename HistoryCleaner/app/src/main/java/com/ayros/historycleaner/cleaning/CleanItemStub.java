package com.ayros.historycleaner.cleaning;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayros.historycleaner.Globals;
import com.ayros.historycleaner.R;
import com.ayros.historycleaner.helpers.Helper;
import com.google.common.base.Optional;

public abstract class CleanItemStub implements CleanItem
{
	protected final Category parentCat;
	protected ViewGroup itemView = null;
	protected CheckBox itemEnabled = null;
	private volatile boolean disableWarning = false;

	public CleanItemStub(Category parent)
	{
		parentCat = parent;
	}

	public CleanItemStub(Category parent, String packageName)
	{
		parentCat = parent;
	}

	public String getDataPath()
	{
		return "/data/data/" + getPackageName();
	}

	public Drawable getIcon()
	{
		return Helper.getPackageIcon(getPackageName());
	}

	public int getUniqueId()
	{
		return getUniqueName().hashCode();
	}

	public String getUniqueName()
	{
		return parentCat.getName() + ":" + getDisplayName();
	}

	/**
	 * A warning message to be displayed when the item is selected. Should be
	 * used for cleaning volatile items such as SMS history, saved passwords,
	 * etc.
	 */
	public Optional<String> getWarningMessage()
	{
		return Optional.absent();
	}

	public ViewGroup getView()
	{
		return itemView;
	}

	// Override
	public List<String[]> getSavedData() throws IOException, UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getRequiredPermissions()
	{
		return new HashSet<>();
	}

	public boolean isApplicable()
	{
		return Helper.isPackageInstalled(getPackageName());
	}

	public boolean isChecked()
	{
		return itemEnabled.isChecked();
	}

	public boolean isRootRequired()
	{
		return true;
	}

	public boolean killProcess()
	{
		if (getPackageName() == null || getPackageName().length() == 0)
		{
			return false;
		}

		ActivityManager manager = (ActivityManager)Globals.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		manager.killBackgroundProcesses(getPackageName());

		return true;
	}

	/**
	 * Whether the process should be killed after the item is cleaned. By default it returns true.
	 * @return
	 */
	public boolean killProcessAfterCleaning()
	{
		return true;
	}

	/**
	 * Should be called after cleaning the item. By default it kills the
	 * application's process
	 */
	public void postClean()
	{
		if (killProcessAfterCleaning())
		{
			killProcess();
		}
	}

	public boolean runOnUIThread()
	{
		return false;
	}

	public void setChecked(boolean checked)
	{
		// setEnabled(checked) not working?
		if (checked != itemEnabled.isChecked())
		{
			disableWarning = true;
			itemEnabled.toggle();
			disableWarning = false;
		}
	}

	public View getItemView(final Context c, boolean showDivider)
	{
		itemView = (ViewGroup)View.inflate(c, R.layout.category_item, null);

		itemEnabled = (CheckBox)itemView.findViewById(R.id.enabled);

		itemView.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				itemEnabled.toggle();
			}
		});

		ImageView itemIcon = (ImageView)itemView.findViewById(R.id.item_icon);
		itemIcon.setImageDrawable(getIcon());
		itemIcon.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				itemEnabled.toggle();
			}
		});

		TextView itemName = (TextView)itemView.findViewById(R.id.item_name);
		itemName.setText(getDisplayName());
		itemName.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				itemEnabled.toggle();
			}
		});

		itemEnabled.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked && !disableWarning && getWarningMessage().isPresent())
				{
					new AlertDialog.Builder(c)
							.setTitle("Warning")
							.setMessage(getWarningMessage().get())
							.setPositiveButton(android.R.string.ok, null)
							.show();
				}
			}
		});

		if (!showDivider)
		{
			View divider = (View)itemView.findViewById(R.id.dividerLine);
			divider.setVisibility(View.GONE);
		}

		return itemView;
	}
}
