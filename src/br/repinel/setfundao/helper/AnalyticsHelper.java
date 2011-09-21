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

package br.repinel.setfundao.helper;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Google Analytics Helper
 */
/**
 * @author Roque Pinel
 *
 */
public class AnalyticsHelper {
	/**
	 * Development mode.
	 * TODO: change to false before release a new version
	 */
	private static final boolean DEV_MODE = true;

	// Google Analytics code
	private static final String UACODE = "UA-21761105-9";

	private static final String PREFS_FIRST_RUN_KEY = "first_run";

	private static final int VISITOR_SCOPE = 1;

	private GoogleAnalyticsTracker tracker;

	private static AnalyticsHelper INSTANCE;

	/**
	 * @param context The activity context
	 * @return The INSTANCE
	 */
	public static AnalyticsHelper getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new AnalyticsHelper(context);
		}

		return INSTANCE;
	}

	/**
	 * Singleton constructor.
	 * 
	 * @param context The activity context
	 */
	private AnalyticsHelper(Context context) {
		System.out.println(">> " + Build.VERSION.RELEASE);
		if (DEV_MODE)
			return;

		tracker = GoogleAnalyticsTracker.getInstance();

		tracker.startNewSession(UACODE, 20, context);

		Log.d(getClass().getName(), "Initializing Analytics");

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		final boolean firstRun = prefs.getBoolean(PREFS_FIRST_RUN_KEY, true);
		if (firstRun) {
			Log.d(getClass().getName(), "Analytics firstRun");

			String apiLevel = Build.VERSION.RELEASE;
			String model = Build.MODEL;
			tracker.setCustomVar(1, "apiLevel", apiLevel, VISITOR_SCOPE);
			tracker.setCustomVar(2, "model", model, VISITOR_SCOPE);

			prefs.edit().putBoolean(PREFS_FIRST_RUN_KEY, false).commit();
		}
	}

	/**
	 * Stops the sesseion.
	 */
	public void stopSession() {
		if (DEV_MODE)
			return;

		tracker.stopSession();
	}

	/**
	 * Track an event.
	 * 
	 * @param category
	 * @param action
	 * @param label
	 * @param value
	 */
	public void trackEvent(final String category, final String action, final String label, final int value) {
		if (DEV_MODE)
			return;

		new AsyncTask<Void, Void, Void>() {

			/**
			 * @see android.os.AsyncTask#doInBackground(Params[])
			 */
			@Override
			protected Void doInBackground(Void... voids) {
				try {
					tracker.trackEvent(category, action, label, value);
					Log.d(getClass().getName(), "SetFundao Analytics trackEvent: "
							+ category + " / " + action + " / " + label + " / " + value);
				} catch (Exception e) {
					Log.w(getClass().getName(), "SetFundao Analytics trackEvent error: "
							+ category + " / " + action + " / " + label + " / " + value, e);
				}
				return null;
			}
		}.execute();
	}

	/**
	 * Track a page view.
	 * 
	 * @param path
	 */
	public void trackPageView(final String path) {
		if (DEV_MODE)
			return;

		new AsyncTask<Void, Void, Void>() {

			/**
			 * @see android.os.AsyncTask#doInBackground(Params[])
			 */
			@Override
			protected Void doInBackground(Void... voids) {
				try {
					tracker.trackPageView(path);
					Log.d(getClass().getName(), "SetFundao Analytics trackPageView: " + path);
				} catch (Exception e) {
					Log.w(getClass().getName(), "SetFundao Analytics trackPageView error: " + path, e);
				}
				return null;
			}
		}.execute();
	}
}
