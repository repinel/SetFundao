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

import br.repinel.R;
import br.repinel.setfundao.ui.prefs.Preferences;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * The main activity. It shows the tabs and the first image. Also responsable
 * for the menu.
 * 
 * @author Roque Pinel
 * 
 */
public class MainTabWidget extends TabActivity {

	private static final String[] TABS = {"Tab1", "Tab2", "Tab3", "Tab4"};

	/**
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources();
		TabHost tabHost = getTabHost();

		String[] tabNames = res.getStringArray(R.array.tab_names);

		for (int i = 0; i < TABS.length; i++) {
			TabHost.TabSpec tab = tabHost.newTabSpec(TABS[i]);

			ComponentName tabActivity = new ComponentName("br.repinel", "br.repinel.setfundao.ui.tab." + TABS[i] + "Activity");

			tab.setContent(new Intent().setComponent(tabActivity));
			// tab.setIndicator(tabNames[i], res.getDrawable(R.drawable.ic_tab_1));
			tab.setIndicator(tabNames[i]);

			tabHost.addTab(tab);

			Log.i(MainTabWidget.class.getName(), "creating " + TABS[i]);
		}
	}

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		Log.i(MainTabWidget.class.getName(), "onCreateOptionsMenu");

		return true;
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_settings:
				this.startActivity(new Intent(this, Preferences.class));
				return true;
			case R.id.main_about: {
				Log.i(MainTabWidget.class.getName(), "onOptionsItemSelected:about");
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

		TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
		int defaultColor = textView.getTextColors().getDefaultColor();
		textView.setTextColor(defaultColor);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.app_icon);
		builder.setTitle(R.string.app_name_version);
		builder.setView(messageView);
		builder.create();
		builder.show();
	}
}
