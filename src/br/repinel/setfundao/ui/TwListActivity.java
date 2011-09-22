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

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.repinel.R;
import br.repinel.setfundao.data.TwItem;
import br.repinel.setfundao.helper.AnalyticsHelper;
import br.repinel.setfundao.helper.UIHelper;

/**
 * @author Roque Pinel
 * 
 */
public class TwListActivity extends ListActivity {

	private ArrayList<TwItem> items = new ArrayList<TwItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tw_list);

		AnalyticsHelper.getInstance(this).trackPageView("/TwList");

		TextView titleView = (TextView) findViewById(R.id.bar_text);
		if (titleView != null && UIHelper.isPortrait(this)) {
			titleView.setText(getTitle());
		}

		TwItem twItem = new TwItem();
		twItem.username = "TranstornoRJ";
		twItem.text = "Caminhão enguiçado prejudica trânsito na Avenida Brasil http://t.co/MjOieVla @LeiSecaRJ";

		items.add(twItem);

		new TwFetcher().execute();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG).show();
	}

	/**
	 * Called when Home is clicked.
	 * 
	 * @param view
	 *            The view
	 */
	public void onHomeClick(View view) {
		AnalyticsHelper.getInstance(this).trackEvent(getTitle().toString(), "Click", "Home", 0);

		final Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private class TwFetcher extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progressDialog;

		/**
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(TwListActivity.this, "", getResources().getText(R.string.tw_loading).toString(), true);
		}

		/**
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... arg0) {
			Log.d(TwListActivity.class.getName(), "Fetching tweets...");

			Twitter twitter = new TwitterFactory().getInstance();

			String queryFilter = "fundao OR brasil OR vermelha OR amarela OR #AvBrasil OR #LinhaAmarela OR #LinhaVermelha from:CETRIO_ONLINE OR from:TranstornoRJ OR from:transitorj";

			Query query = new Query(queryFilter);

			try {
				QueryResult result = twitter.search(query);

				for (Tweet tweet : result.getTweets()) {
					Log.d(TwListActivity.class.getName(), tweet.getFromUser() + ": " + tweet.getText());

					TwItem twItem = new TwItem();
					twItem.username = tweet.getFromUser();
					twItem.text = tweet.getText();

					items.add(twItem);
				}
			} catch (TwitterException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();

			setListAdapter(new TWListAdapter(TwListActivity.this, R.layout.tw_list_item, items));
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

				// holder.imageView = (ImageView)
				// convertView.findViewById(R.id.tw_image);
				holder.usernameView = (TextView) convertView.findViewById(R.id.tw_username);
				holder.textView = (TextView) convertView.findViewById(R.id.tw_text);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (twItem != null) {
				holder.usernameView.setText(twItem.username);
				holder.textView.setText(twItem.text);
			}

			return convertView;
		}
	}

	/**
	 * @author Roque Pinel
	 * 
	 */
	public static class ViewHolder {
		// public ImageView imageView;
		public TextView usernameView;
		public TextView textView;
	}
}
