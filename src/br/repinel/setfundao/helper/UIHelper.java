/*
 * Copyright (C) 2013 Roque Pinel
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

package br.repinel.setfundao.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.repinel.R;
import br.repinel.setfundao.util.Constants;

/**
 * UI Helper
 * 
 * @author Roque Pinel
 *
 */
public class UIHelper {

	/**
	 * Show an about dialog that cites data sources.
	 */
	public static void showAbout(Activity activity) {
		View messageView = activity.getLayoutInflater().inflate(R.layout.about, null, false);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(R.drawable.app_icon);
		builder.setTitle(R.string.app_name_version);
		builder.setView(messageView);
		builder.create();
		builder.show();
	}

	/**
	 * Show a message dialog.
	 * 
	 * @param message The message to be shown.
	 */
	public static void showMessage(Activity activity, String message) {
		View messageView = activity.getLayoutInflater().inflate(R.layout.message, null, false);

		TextView textView = (TextView) messageView.findViewById(R.id.message);
		textView.setText(message);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(R.drawable.app_icon);
		builder.setTitle(R.string.app_name);
		builder.setView(messageView);
		builder.setCancelable(false);
		builder.setPositiveButton(activity.getResources().getString(R.string.btn_message_box_ok), null);
		builder.create();
		builder.show();
	}

	/**
	 * Show a toast message.
	 *
	 * @param message The message to be shown.
	 */
	public static void showMessage(Context context, String message) {
		UIHelper.showMessage(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * Show a toast message.
	 *
	 * @param message The message to be shown.
	 * @param duration The time length to display the message. See <code>android.widget.Toast</code>.
	 */
	public static void showMessage(Context context, String message, int duration) {
		Toast.makeText(context, message, duration).show();
	}

	/**
	 * @return <code>true</code> if the orientation is portrait.
	 */
	public static boolean isPortrait(Activity activity) {
		return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	/**
	 * @return <code>true</code> if the network is available.
	 */
	public static boolean isOnline(Activity activity) {
		 ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		 if (cm == null || cm.getActiveNetworkInfo() == null)
			 return false;
		 return cm.getActiveNetworkInfo().isConnectedOrConnecting();
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

		final int updateInterval = Integer.parseInt(pref.getString(res.getString(R.string.update_interval),
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

		final String lastFetchDate = pref.getString(Constants.PREFS_LAST_FETCH_DATE + camera, res.getString(R.string.default_last_fetch_date));

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
		editor.putString(Constants.PREFS_LAST_FETCH_DATE + camera, value);
		editor.commit();
	}

	/**
	 * Verify if the image has to be fetched on create.
	 * 
	 * @param context The activity context
	 * @param res The resources
	 * @return If the image has to be fetched on create
	 */
	public static boolean getFetchImageOnCreateActivity(Context context, Resources res) {
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

		final boolean fetchImageOnCreateActivity = pref.getBoolean(res.getString(R.string.fetch_image_on_create_activity),
				res.getBoolean(R.bool.default_fetch_image_on_create_activity));

		return fetchImageOnCreateActivity;
	}

	/**
	 * Verify if the tw has to be fetched on create.
	 * 
	 * @param context The activity context
	 * @param res The resources
	 * @return If the tw has to be fetched on create
	 */
	public static boolean getFetchTwOnCreateActivity(Context context, Resources res) {
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

		final boolean fetchTwOnCreateActivity = pref.getBoolean(res.getString(R.string.fetch_tw_on_create_activity),
				res.getBoolean(R.bool.default_fetch_tw_on_create_activity));

		return fetchTwOnCreateActivity;
	}
}
