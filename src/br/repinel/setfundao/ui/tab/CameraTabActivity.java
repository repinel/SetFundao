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
package br.repinel.setfundao.ui.tab;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import br.repinel.R;
import br.repinel.setfundao.helper.ImageHelper;
import br.repinel.setfundao.ui.exception.MainException;
import br.repinel.setfundao.ui.prefs.Preferences;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Camera Tab.
 * 
 * @author Flávio Costa
 *
 */
public class CameraTabActivity extends Activity implements OnClickListener {

	protected String photoURL;

	private final Handler imageHandler = new Handler();
	private final TabImageUpdater updateTab = new TabImageUpdater();

	private boolean stopScheduling;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.stopScheduling = false;

		setContentView(R.layout.tab);

		new ImageFetcher().execute();

		findViewById(R.id.cameraImage).setOnClickListener(this);

		// Ads
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest();
		//adRequest.setTesting(true);
		adView.loadAd(adRequest);
	}

	@Override
	protected void onStart() {
		super.onStart();

		scheduleUpdate();
	}

	@Override
	protected void onPause() {
		super.onPause();

		stopUpdate();
	}

	public void onClick(View v) {
		stopUpdate();

		this.stopScheduling = false;

		new ImageFetcher().execute();

		scheduleUpdate();
	}

	/**
	 * Show a message dialog.
	 * 
	 * @param message The message to be shown.
	 */
	protected void showMessage(String message) {
		View messageView = getLayoutInflater().inflate(R.layout.message, null, false);

		TextView textView = (TextView) messageView.findViewById(R.id.message);
		int defaultColor = textView.getTextColors().getDefaultColor();
		textView.setTextColor(defaultColor);
		textView.setText(message);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.app_icon);
		builder.setTitle(R.string.app_name);
		builder.setView(messageView);
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", null);
		builder.create();
		builder.show();
	}

	private void scheduleUpdate() {
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		final int updateInterval = Integer.parseInt(pref.getString(Preferences.PREFS_UPDATE_INTERVAL,
									getResources().getString(R.string.default_update_interval)));

		if (updateInterval > 0)
			imageHandler.postDelayed(updateTab, updateInterval);
	}

	private void stopUpdate() {
		imageHandler.removeCallbacks(updateTab);
	}

	private class TabImageUpdater implements Runnable {
		public void run() {
			new ImageFetcher().execute();

			if (!stopScheduling)
				scheduleUpdate();
		}
	}

	public boolean isStopScheduling() {
		return stopScheduling;
	}

	public void setStopScheduling(boolean paramStopScheduling) {
		stopScheduling = paramStopScheduling;
	}

	/**
	 * ImageFetcher.
	 * 
	 * Async image download.
	 * 
	 * @author Leonardo Marques
	 *
	 */
	private class ImageFetcher extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... voids) {
			Log.i(getClass().getName(), "fetching image: " + photoURL);

			Bitmap bmImg = null;

			try {
				bmImg = ImageHelper.downloadImage(photoURL, getResources());
			} catch (MainException e) {
				bmImg = null;
				
				showMessage(e.getMessage());

				// stop scheduling
				setStopScheduling(true);
			}
			
			return bmImg;
		}

		@Override
		protected void onPostExecute(Bitmap image) {
			ImageView imgView = (ImageView) findViewById(R.id.cameraImage);
			if (image != null) {
				imgView.setImageBitmap(image);
			} else {
				imgView.setImageResource(R.drawable.sem_imagem);
			}
		}
		
	}
}
