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
import android.view.View;
import br.repinel.R;
import br.repinel.setfundao.helper.AnalyticsHelper;
import br.repinel.setfundao.ui.tab.CameraTabActivity;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

/**
 * The Home Activity.
 * 
 * @author Roque Pinel
 *
 */
public class HomeActivity extends BaseActivity {
	/**
	 * @see br.repinel.setfundao.ui.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		AnalyticsHelper.getInstance(this).trackPageView("/Home");

		/*
		 * AdMob Ads
		 */
		AdView adView = (AdView) findViewById(R.id.adView);
		if (adView != null) {
			AdRequest adRequest = new AdRequest();
			//adRequest.setTesting(true);
			adView.loadAd(adRequest);
		}

		/*
		 * Android Change Log
		 */
		ChangeLog changeLog = new ChangeLog(this);
		if (changeLog.firstRun())
			changeLog.getLogDialog().show();
	}

	/**
	 * Called when Camera1 is clicked.
	 * 
	 * @param v The View
	 */
	public void onCamera1Click(View v) {
		Intent intent = new Intent(this, CameraTabActivity.class);
		intent.putExtra(CameraTabActivity.BUNDLE_INDEX, 0);
		startActivity(intent);
	}

	/**
	 * Called when Camera2 is clicked.
	 * 
	 * @param v The View
	 */
	public void onCamera2Click(View v) {
		Intent intent = new Intent(this, CameraTabActivity.class);
		intent.putExtra(CameraTabActivity.BUNDLE_INDEX, 1);
		startActivity(intent);
	}

	/**
	 * Called when Camera3 is clicked.
	 * 
	 * @param v The View
	 */
	public void onCamera3Click(View v) {
		Intent intent = new Intent(this, CameraTabActivity.class);
		intent.putExtra(CameraTabActivity.BUNDLE_INDEX, 2);
		startActivity(intent);
	}

	/**
	 * Called when Camera4 is clicked.
	 * 
	 * @param v The View
	 */
	public void onCamera4Click(View v) {
		Intent intent = new Intent(this, CameraTabActivity.class);
		intent.putExtra(CameraTabActivity.BUNDLE_INDEX, 3);
		startActivity(intent);
	}
}
