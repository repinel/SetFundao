/*
 * Copyright (C) 2011 Roque Pinel
 *
 * This file is part of SetFundao.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 */

package br.repinel.setfundao.ui;

import sheetrock.panda.changelog.ChangeLog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import br.repinel.R;
import br.repinel.setfundao.helper.AnalyticsHelper;
import br.repinel.setfundao.ui.prefs.Preferences;

/**
 * Base class to all activities.
 * 
 * @author Roque Pinel
 *
 */
public class BaseActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		AnalyticsHelper.getInstance(getActivity()).stopSession();
	}

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		Log.i(getClass().getName(), "onCreateOptionsMenu");

		return true;
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.full_log:
				AnalyticsHelper.getInstance(getActivity()).trackEvent(getClass().getName(), "Click", "FullLog", 0);
				ChangeLog changeLog = new ChangeLog(this);
				changeLog.getFullLogDialog().show();
				return true;
			case R.id.item_settings:
				AnalyticsHelper.getInstance(getActivity()).trackEvent(getClass().getName(), "Click", "Settings", 0);
				this.startActivity(new Intent(this, Preferences.class));
				return true;
			case R.id.main_about: {
				AnalyticsHelper.getInstance(getActivity()).trackEvent(getClass().getName(), "Click", "About", 0);
				Log.i(getClass().getName(), "onOptionsItemSelected:about");
				showAbout();
				return true;
			}
		}
		return false;
	}

	/**
	 * Show an about dialog that cites data sources.
	 */
	private void showAbout() {
		View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

		TextView textView = (TextView) messageView.findViewById(R.id.about_description);
		int defaultColor = textView.getTextColors().getDefaultColor();
		textView.setTextColor(defaultColor);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.app_icon);
		builder.setTitle(R.string.app_name_version);
		builder.setView(messageView);
		builder.create();
		builder.show();
	}

	/**
	 * @return The current Activity. The own class.
	 */
	protected Activity getActivity() {
		return this;
	}
}
