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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import br.repinel.R;
import br.repinel.setfundao.helper.AnalyticsHelper;
import br.repinel.setfundao.helper.UIHelper;
import br.repinel.setfundao.ui.prefs.Preferences;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/**
 * Show the traffic map near the Fundao.
 * 
 * @author Roque Pinel
 *
 */
public class FundaoMapActivity extends MapActivity {

	private MapView mapView;

	private GeoPoint gp;

	private MapController mapController;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		AnalyticsHelper.getInstance(this).trackPageView("/Map");

		TextView titleView = (TextView) findViewById(R.id.bar_text);
		if (titleView != null && UIHelper.isPortrait(this)) {
			titleView.setText(getTitle());
		}

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setTraffic(true);

		gp = new GeoPoint(getResources().getInteger(R.integer.map_ufrj_lat),
			getResources().getInteger(R.integer.map_ufrj_lon));

		mapController = mapView.getController();
		mapController.setZoom(getResources().getInteger(R.integer.map_zoom));
		mapController.setCenter(gp);
		mapController.animateTo(gp);
	}

	/**
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		AnalyticsHelper.getInstance(this).trackEvent(getClass().getName(), "Click", "Menu", 0);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		Log.d(getClass().getName(), "onCreateOptionsMenu");

		return true;
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.full_log:
				AnalyticsHelper.getInstance(this).trackEvent(getClass().getName(), "Click", "FullLog", 0);
				Log.d(getClass().getName(), "onOptionsItemSelected:log");
				ChangeLog changeLog = new ChangeLog(this);
				changeLog.getFullLogDialog().show();
				return true;
			case R.id.item_settings:
				AnalyticsHelper.getInstance(this).trackEvent(getClass().getName(), "Click", "Settings", 0);
				Log.d(getClass().getName(), "onOptionsItemSelected:preferences");
				this.startActivity(new Intent(this, Preferences.class));
				return true;
			case R.id.main_about: {
				AnalyticsHelper.getInstance(this).trackEvent(getClass().getName(), "Click", "About", 0);
				Log.d(getClass().getName(), "onOptionsItemSelected:about");
				UIHelper.showAbout(this);
				return true;
			}
		}
		return false;
	}

	/**
	 * Called when Home is clicked.
	 * 
	 * @param view The view
	 */
	public void onHomeClick(View view) {
		AnalyticsHelper.getInstance(this).trackEvent(getTitle().toString(), "Click", "Home", 0);

		final Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Open the Tw view.
	 * 
	 * @param v The view.
	 */
	public void onTwClick(View v) {
		AnalyticsHelper.getInstance(this).trackEvent(getClass().getName(), "Click", "TwList", 0);
		Intent intent = new Intent(this, TwListActivity.class);
		startActivity(intent);
	}
}
