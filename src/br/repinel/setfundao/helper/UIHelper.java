package br.repinel.setfundao.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;

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
}
