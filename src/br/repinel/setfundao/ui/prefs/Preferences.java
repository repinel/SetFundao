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

package br.repinel.setfundao.ui.prefs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import br.repinel.R;
import br.repinel.setfundao.data.DataProvider;
import br.repinel.setfundao.helper.AnalyticsHelper;
import br.repinel.setfundao.helper.UIHelper;

/**
 * Preferences.
 * 
 * @author Roque Pinel
 */
public class Preferences extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	private static String BUNDLE_RESTORE = "restore";

	public static final String PREFS_LAST_FETCH_DATE = "last_fetch_date_";

	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AnalyticsHelper.getInstance(this).trackPageView("/Preferences");

		if (this.getIntent().getExtras() != null
			&& this.getIntent().getExtras().get(BUNDLE_RESTORE) != null
			&& this.getIntent().getExtras().getBoolean(BUNDLE_RESTORE))
			UIHelper.showMessage(this, getString(R.string.reset_settings_message));

		// ensuring the default value...
		UIHelper.getFetchImageOnCreateActivity(this, getResources());
		UIHelper.getFetchTwOnCreateActivity(this, getResources());

		this.addPreferencesFromResource(R.xml.prefs);

		Preference p = this.findPreference("reset_settings");
		if (p != null) {
			p.setOnPreferenceClickListener(// .
					new Preference.OnPreferenceClickListener() {
						public boolean onPreferenceClick(final Preference preference) {
							resetSettingsDialog();
							return true;
						}
					});
		}
	}

	/**
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO: think about a way to alert the other activities
	}

	/**
	 * Reset all settings.
	 */
	private void resetSettingsDialog() {
		AnalyticsHelper.getInstance(this).trackEvent(getClass().getName(), "Click", "ResetSettings", 0);

		Log.w(Preferences.class.getName(), "Resetting settings...");

		DataProvider dataProvider = new DataProvider(this);
		dataProvider.resetData();

		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.remove(getString(R.string.update_interval));
		editor.remove(getString(R.string.fetch_image_on_create_activity));
		editor.remove(getString(R.string.fetch_tw_on_create_activity));
		editor.commit();

		Intent intent = new Intent(this, Preferences.class);
		intent.putExtra(BUNDLE_RESTORE, true);
		startActivity(intent);
		finish();
	}
}
