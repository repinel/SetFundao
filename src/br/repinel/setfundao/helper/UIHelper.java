package br.repinel.setfundao.helper;

import br.repinel.R;
import br.repinel.setfundao.ui.prefs.Preferences;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

/**
 * UI Helper
 * 
 * @author Roque Pinel
 *
 */
public class UIHelper {
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
