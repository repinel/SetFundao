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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sheetrock.panda.changelog.ChangeLog;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.repinel.R;
import br.repinel.setfundao.core.TwItem;
import br.repinel.setfundao.core.TwUser;
import br.repinel.setfundao.data.TwFilterFacade;
import br.repinel.setfundao.data.TwItemFacade;
import br.repinel.setfundao.helper.AnalyticsHelper;
import br.repinel.setfundao.helper.ImageHelper;
import br.repinel.setfundao.helper.UIHelper;
import br.repinel.setfundao.ui.exception.MainException;
import br.repinel.setfundao.ui.prefs.Preferences;

/**
 * @author Roque Pinel
 * 
 */
public class TwListActivity extends ListActivity {

	private TWListAdapter twListAdapter;

	private boolean firstTime;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tw_list);

		this.firstTime = true;

		AnalyticsHelper.getInstance(this).trackPageView("/TwList");

		TextView titleView = (TextView) findViewById(R.id.bar_text);
		if (titleView != null && UIHelper.isPortrait(this)) {
			titleView.setText(getTitle());
		}

		this.twListAdapter = new TWListAdapter(TwListActivity.this, R.layout.tw_list_item, new ArrayList<TwItem>());
		setListAdapter(twListAdapter);

		new TwFetcher().execute();
	}

	/**
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		ArrayList<TwItem> items = this.twListAdapter.items;

		TwItemFacade TwItemFacade = new TwItemFacade(this);

		Log.d(getClass().getName(), "TwItem. Deleting all items");
		TwItemFacade.deleteAllTwItems();

		Log.d(getClass().getName(), "TwItem. Saving " + items.size() + " new items");

		for (TwItem item : items) {
			TwItemFacade.insertTwItem(item);
		}
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
	 * Called when Refresh is clicked.
	 * 
	 * @param v The view
	 */
	public void onRefreshClick(View v) {
		AnalyticsHelper.getInstance(this).trackEvent(getTitle().toString(), "Click", "Refresh", 0);

		new TwFetcher().execute();
	}

	/**
	 * Open the Map view.
	 * 
	 * @param v The view.
	 */
	public void onMapClick(View v) {
		AnalyticsHelper.getInstance(this).trackEvent(getClass().getName(), "Click", "Map", 0);
		Intent intent = new Intent(this, FundaoMapActivity.class);
		startActivity(intent);
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
	 * Put all filters together and build the query.
	 * 
	 * @return The query built
	 */
	private String getQueryFilter() {
		TwFilterFacade twFilterFacade = new TwFilterFacade(TwListActivity.this);

		List<String> words = twFilterFacade.selectAllTwFilterWords();
		List<String> hashtags = twFilterFacade.selectAllTwFilterHashtags();
		List<String> users = twFilterFacade.selectAllTwFilterUsers();

		StringBuilder sb = new StringBuilder();

		boolean isFirst = true;
		for (String word : words) {
			if (!isFirst)
				sb.append(" OR");
			sb.append(" ");
			sb.append(word);
			isFirst = false;
		}

		for (String hastag : hashtags) {
			if (!isFirst)
				sb.append(" OR");
			sb.append(" #");
			sb.append(hastag);
			isFirst = false;
		}

		isFirst = true;
		for (String user : users) {
			if (!isFirst)
				sb.append(" OR");
			sb.append(" from:");
			sb.append(user);
			isFirst = false;
		}

		return sb.toString().trim();
	}

	/**
	 * TwFetcher.
	 * 
	 * Async news download.
	 * 
	 * @author Roque Pinel
	 *
	 */
	private class TwFetcher extends AsyncTask<Void, Void, Void> {

		private String message; 

		private ArrayList<TwItem> items;

		/**
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			updateRefreshStatus(true);

			TwItemFacade TwItemFacade = new TwItemFacade(TwListActivity.this);

			if (firstTime) {
				Log.d(getClass().getName(), "TwItem. Loading all items");

				items = (ArrayList<TwItem>) TwItemFacade.selectAllTwItems();

				if (!items.isEmpty()) {
					twListAdapter.clear();

					for (TwItem twItem : items) {
						twListAdapter.add(twItem);
					}

					twListAdapter.notifyDataSetChanged();
				}
			}

			// no message
			message = null;
		}

		/**
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... arg0) {
			if (firstTime) {
				firstTime = false;

				// fetch new information only if the user wants
				if (!UIHelper.getFetchTwOnCreateActivity(TwListActivity.this, getResources()))
					return null;
			}

			Log.d(TwListActivity.class.getName(), "Fetching tweets...");

			Map<String, TwUser> userCache = new HashMap<String, TwUser>();

			Twitter twitter = new TwitterFactory().getInstance();

			String queryFilter = getQueryFilter();
			Log.d(TwListActivity.class.getName(), queryFilter);

			Query query = new Query(queryFilter);

			try {
				items = new ArrayList<TwItem>();

				if (!UIHelper.isOnline(TwListActivity.this))
					throw new MainException(getResources().getString(R.string.error_network));

				QueryResult result = twitter.search(query);

				for (Tweet tweet : result.getTweets()) {
					Log.d(TwListActivity.class.getName(), tweet.getFromUser() + ": " + tweet.getText());

					TwUser twUser;

					if (userCache.containsKey(tweet.getFromUser())) {
						twUser = userCache.get(tweet.getFromUser());
					} else {
						User user = twitter.showUser(tweet.getFromUser());

						twUser = new TwUser();
						twUser.username = tweet.getFromUser();
						twUser.profileImageURL = user.getProfileImageURL();

						userCache.put(tweet.getFromUser(), twUser);
					}

					TwItem twItem = new TwItem();
					twItem.text = tweet.getText();
					twItem.createdAt = tweet.getCreatedAt();
					twItem.user = twUser;

					items.add(twItem);
				}
			} catch (TwitterException e) {
				Log.e(TwListActivity.class.getName(), e.getMessage());
				message = getString(R.string.error_tw);
			} catch (Exception e) {
				Log.e(TwListActivity.class.getName(), e.getMessage());
				message = getString(R.string.error_network);
			}

			return null;
		}

		/**
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			updateRefreshStatus(false);

			if (message != null) {
				try {
					UIHelper.showMessage(TwListActivity.this, message);
				} catch (Exception e1) {
					// empty
				}
			} else if (!items.isEmpty()) {
				twListAdapter.clear();

				for (TwItem twItem : items) {
					twListAdapter.add(twItem);
				}

				twListAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * Adapter binding TwItem to View.
	 * 
	 * @author Roque Pinel
	 */
	public class TWListAdapter extends ArrayAdapter<TwItem> {
		private LayoutInflater mInflater;

		private ArrayList<TwItem> items;

		/**
		 * @param context
		 * @param textViewResourceId
		 * @param items
		 */
		public TWListAdapter(Context context, int textViewResourceId, ArrayList<TwItem> items) {
			super(context, textViewResourceId, items);

			this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			this.items = items;
		}

		/**
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TwItem twItem = items.get(position);

			if (twItem == null)
				return convertView;

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.tw_list_item, null);

				holder = new ViewHolder();

				holder.imageView = (ImageView) convertView.findViewById(R.id.tw_image);
				holder.usernameView = (TextView) convertView.findViewById(R.id.tw_username);
				holder.textView = (TextView) convertView.findViewById(R.id.tw_text);
				holder.createdAtView = (TextView) convertView.findViewById(R.id.tw_createdAt);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (twItem != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));

				if (twItem.user != null)
					holder.usernameView.setText(twItem.user.username);

				holder.textView.setText(twItem.text);

				long diffHours = (new Date().getTime() - twItem.createdAt.getTime()) / (60 * 60 * 1000);

				String createdAtStr;
				if (diffHours > 1)
					createdAtStr = getResources().getString(R.string.tw_date_and_hours_ago, dateFormat.format(twItem.createdAt), diffHours);
				else 
					createdAtStr = getResources().getString(R.string.tw_date_and_hour_ago, dateFormat.format(twItem.createdAt), diffHours);

				holder.createdAtView.setText(createdAtStr);

				try {
					if (twItem.user != null && twItem.user.profileImageURL != null && twItem.user.profileImage == null) {
						twItem.user.profileImage = ImageHelper.realDownloadImage(twItem.user.profileImageURL, getResources());
					}

					if (twItem.user.profileImage != null)
						holder.imageView.setImageBitmap(twItem.user.profileImage);
				} catch (MainException e) {
					try {
						Log.e(TwListActivity.class.getName(), e.getMessage());
						UIHelper.showMessage(TwListActivity.this, e.getMessage());
					} catch (Exception e1) {
						// empty
					}
				}
			}

			return convertView;
		}
	}

	/**
	 * @author Roque Pinel
	 * 
	 */
	public static class ViewHolder {
		public ImageView imageView;
		public TextView usernameView;
		public TextView textView;
		public TextView createdAtView;
	}
}
