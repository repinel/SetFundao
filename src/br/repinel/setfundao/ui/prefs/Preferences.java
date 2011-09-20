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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import br.repinel.R;

/**
 * Preferences.
 * 
 * @author Roque Pinel
 */
public class Preferences extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	/** {@link Preferences}: update interval. */
	public static final String PREFS_UPDATE_INTERVAL = "update_interval";
	public static final String PREFS_LAST_FETCH_DATE = "last_fetch_date_";

	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(R.xml.prefs);
	}

	/**
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		
	}

	/**
	 * Get the time interval to update the image.
	 * 
	 * @param context The activity context
	 * @param res The resources
	 * @return The update interval
	 */
	public static int getUpdateInterval(Context context, Resources res) {
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

		final int updateInterval = Integer.parseInt(pref.getString(Preferences.PREFS_UPDATE_INTERVAL,
				res.getString(R.string.default_update_interval)));

		return updateInterval;
	}

	/**
	 * Get the date of the last fetched image of a camera.
	 * 
	 * @param context The activity context
	 * @param res The resources
	 * @param camera The camera name
	 * @return The date
	 */
	public static String getLastFetchDate(Context context, Resources res, String camera) {
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

		final String lastFetchDate = pref.getString(Preferences.PREFS_LAST_FETCH_DATE + camera, res.getString(R.string.default_last_fetch_date));

		return lastFetchDate;
	}

	/**
	 * Set the date of the last fetched image of a camera.
	 * 
	 * @param context The activity context
	 * @param camera The camera name
	 * @param value The value to be setted
	 */
	public static void setLastFetchDate(Context context, String camera, String value) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

		SharedPreferences.Editor editor = pref.edit();
		editor.putString(Preferences.PREFS_LAST_FETCH_DATE + camera, value);
		editor.commit();
	}
}
