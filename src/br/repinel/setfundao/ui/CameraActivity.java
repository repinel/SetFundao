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

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import br.repinel.R;
import br.repinel.setfundao.helper.AnalyticsHelper;
import br.repinel.setfundao.helper.ImageHelper;
import br.repinel.setfundao.helper.UIHelper;
import br.repinel.setfundao.ui.exception.MainException;

/**
 * Camera Tab.
 * 
 * @author Flávio Costa
 * @author Leonardo Marques
 * @author Roque Pinel
 *
 */
public class CameraActivity extends BaseActivity implements OnClickListener {

	public static String BUNDLE_INDEX = "index";

	private int index = -1;

	protected String photoURL;
	protected String photoFilename;

	private final Handler imageHandler = new Handler();
	private final TabImageUpdater updateTab = new TabImageUpdater();

	private boolean firstTime;

	private boolean stopScheduling;

	private Bitmap image;
	private Date lastFetchDate;

	/**
	 * @see br.repinel.setfundao.ui.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);

		this.firstTime = true;
		this.stopScheduling = false;
		this.image = null;

		this.index = this.getIntent().getExtras().getInt(BUNDLE_INDEX);

		AnalyticsHelper.getInstance(this).trackPageView(
			"/" + getResources().getStringArray(R.array.camera_names)[index]);

		this.photoURL = getResources().getStringArray(R.array.photo_urls)[index];
		this.photoFilename = getResources().getStringArray(R.array.photo_filenames)[index];

		new ImageFetcher().execute();

		TextView titleView = (TextView) findViewById(R.id.bar_text);
		if (titleView != null && UIHelper.isPortrait(this)) {
			titleView.setText(getResources().getStringArray(R.array.camera_names)[index]);
		}

		findViewById(R.id.cameraImage).setOnClickListener(this);
	}

	/**
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			if (image != null)
				ImageHelper.saveImage(CameraActivity.this, image, photoFilename);

				SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
				UIHelper.setLastFetchDate(getApplicationContext(), photoFilename, dateFormat.format(lastFetchDate));
		} catch (Exception e) {
			try {
				UIHelper.showMessage(CameraActivity.this, e.getMessage());
			} catch (Exception e1) {
				// empty
			}
		}
	}

	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		scheduleUpdate();
	}

	/**
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();

		stopUpdate();
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		AnalyticsHelper.getInstance(this).trackEvent(getResources().getStringArray(R.array.camera_names)[index], "Click", "Image", 0);

		doRefresh();
	}

	/**
	 * Called when Home is clicked.
	 * 
	 * @param v The view
	 */
	public void onHomeClick(View v) {
		AnalyticsHelper.getInstance(this).trackEvent(getResources().getStringArray(R.array.camera_names)[index], "Click", "Home", 0);

		final Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Called when Refresh is clicked.
	 * 
	 * @param v The view
	 */
	public void onRefreshClick(View v) {
		AnalyticsHelper.getInstance(this).trackEvent(getResources().getStringArray(R.array.camera_names)[index], "Click", "Refresh", 0);

		doRefresh();
	}

	/**
	 * Refresh the camera image.
	 */
	private void doRefresh() {
		stopUpdate();

		this.stopScheduling = false;

		new ImageFetcher().execute();

		scheduleUpdate();
	}

	/**
	 * Update the schedule time.
	 */
	private void scheduleUpdate() {
		final int updateInterval = UIHelper.getUpdateInterval(getApplicationContext(), getResources());

		if (updateInterval > 0)
			imageHandler.postDelayed(updateTab, updateInterval);
	}

	/**
	 * Stop update.
	 */
	private void stopUpdate() {
		imageHandler.removeCallbacks(updateTab);
	}

	/**
	 * Thread created to update the image.
	 * 
	 * @author Roque Pinel
	 *
	 */
	private class TabImageUpdater implements Runnable {
		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			new ImageFetcher().execute();

			if (!stopScheduling)
				scheduleUpdate();
		}
	}

	/**
	 * @return <code>true</code> if the schedule has stopped
	 */
	public boolean isStopScheduling() {
		return stopScheduling;
	}

	/**
	 * Change the stop scheduling condition.
	 * 
	 * @param stopScheduling The value to be setted
	 */
	public void setStopScheduling(boolean stopScheduling) {
		this.stopScheduling = stopScheduling;
	}

	/**
	 * Set the date of the last fetched camera image.
	 * 
	 * @param date The date to be setted
	 */
	private void setLastFetchDate(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
			setLastFetchDate(dateFormat.format(date));
		}
		else
			setLastFetchDate("");
	}

	/**
	 * Set the date of the last fetched camera image.
	 * 
	 * @param value The value to be setted
	 */
	private void setLastFetchDate(String value) {
		TextView lastFetchDateView = (TextView) findViewById(R.id.last_fetch_date); 

		if (lastFetchDateView != null) {
			lastFetchDateView.setText(value);
		}
	}

	/**
	 * Set the image information.
	 * 
	 * @param value The value to be setted
	 */
	private void setImageInfo(String value) {
		TextView imageInfoView = (TextView) findViewById(R.id.image_info);

		if (imageInfoView != null) {
			imageInfoView.setText(value);
		}
	}

	/**
	 * Update the refresh status. Show a progress circle when updating the image.
	 * 
	 * @param working If it is updating or not.
	 */
	private void updateRefreshStatus(boolean working) {
		View refreshView = findViewById(R.id.btn_refresh);
		View refreshProgressView = findViewById(R.id.refresh_progress);

		if (refreshView != null && refreshProgressView != null) {
			refreshView.setVisibility(working ? View.GONE : View.VISIBLE);
			refreshProgressView.setVisibility(working ? View.VISIBLE : View.GONE);
		}
	}

	/**
	 * ImageFetcher.
	 * 
	 * Async image download.
	 * 
	 * @author Leonardo Marques
	 * @author Roque Pinel
	 *
	 */
	private class ImageFetcher extends AsyncTask<Void, Void, Bitmap> {
		private String message; 

		/**
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			updateRefreshStatus(true);

			Log.d(getClass().getName(), "restoring image: " + photoFilename);

			ImageView imgView = (ImageView) findViewById(R.id.cameraImage);

			// load last image
			try {
				if (firstTime) {
					Bitmap image = ImageHelper.loadImage(CameraActivity.this, photoFilename);
	
					if (image != null) {
						imgView.setImageBitmap(image);
		
						final String lastFetchDate = UIHelper.getLastFetchDate(getApplicationContext(), getResources(), photoFilename);
		
						setLastFetchDate(lastFetchDate);
						setImageInfo(getResources().getString(R.string.image_stored_message));
					}

					firstTime = false;
				}
			} catch (MainException e) {
				e.printStackTrace();

				imgView.setImageResource(R.drawable.sem_imagem);
				setLastFetchDate(new Date());
			}

			// no message
			message = null;
		}

		/**
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Bitmap doInBackground(Void... voids) {
			Log.d(getClass().getName(), "fetching image: " + photoURL);

			// fetch new image
			try {
				if (!UIHelper.isOnline(CameraActivity.this))
					throw new MainException(getResources().getString(R.string.error_network));

				image = ImageHelper.downloadImage(photoURL, getResources());
				lastFetchDate = new Date();
			} catch (MainException e) {
				message = e.getMessage();

				// stop scheduling
				setStopScheduling(true);
			} catch (Exception e) {
				// TODO: improve exception handler

				// stop scheduling
				setStopScheduling(true);
			}

			return image;
		}

		/**
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Bitmap image) {
			ImageView imgView = (ImageView) findViewById(R.id.cameraImage);
			if (image != null) {
				imgView.setImageBitmap(image);

				setLastFetchDate(lastFetchDate);
				setImageInfo(null);
			} else if (message != null) {
				try {
					setImageInfo(getResources().getString(R.string.image_stored_message));
					UIHelper.showMessage(CameraActivity.this, message);
				} catch (Exception e1) {
					// empty
				}
			}

			updateRefreshStatus(false);
		}
	}
}
