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

package br.repinel.setfundao.ui.prefs;

import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import br.repinel.R;
import br.repinel.setfundao.data.TwFilterFacade;
import br.repinel.setfundao.helper.UIHelper;

/**
 * Allow the user to change the Tw Filters: words, hashtags and users.
 * 
 * @author Roque Pinel
 *
 */
public class TwFilters extends ListActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

	private String type;

	private ArrayAdapter<String> adapter;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_ok_add);

		this.type = this.getIntent().getExtras().getString(this.getString(R.string.tw_bundle_type));

		setTitle();

		loadData();

		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		this.findViewById(R.id.ok).setOnClickListener(this);
		this.findViewById(R.id.add).setOnClickListener(this);
	}

	/**
	 * Set the window title.
	 */
	private void setTitle() {
		String title = null;

		if (this.type.equals(this.getString(R.string.tw_words_filter)))
			title = this.getString(R.string.words_filter_);
		else if (this.type.equals(this.getString(R.string.tw_hashtags_filter)))
			title = this.getString(R.string.hashtags_filter_);
		else if (this.type.equals(this.getString(R.string.tw_users_filter)))
			title = this.getString(R.string.users_filter_);

		this.setTitle(this.getString(R.string.settings_tw) + " > " + title);
	}

	/**
	 * Load the data to be shown.
	 */
	private void loadData() {
		TwFilterFacade twFilterFacade = new TwFilterFacade(this);

		List<String> values = null;

		if (this.type.equals(this.getString(R.string.tw_words_filter)))
			values = twFilterFacade.selectAllTwFilterWords();
		else if (this.type.equals(this.getString(R.string.tw_hashtags_filter)))
			values = twFilterFacade.selectAllTwFilterHashtags();
		else if (this.type.equals(this.getString(R.string.tw_users_filter)))
			values = twFilterFacade.selectAllTwFilterUsers();

		this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

		this.setListAdapter(this.adapter);
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.add:
				View addView = getLayoutInflater().inflate(R.layout.input_ok_cancel, null, false);

				TextView entryLabel = (TextView) addView.findViewById(R.id.entry_label);

				if (this.type.equals(this.getString(R.string.tw_words_filter)))
					entryLabel.setText(this.getString(R.string.word));
				else if (this.type.equals(this.getString(R.string.tw_hashtags_filter)))
					entryLabel.setText(this.getString(R.string.hashtag));
				else if (this.type.equals(this.getString(R.string.tw_users_filter)))
					entryLabel.setText(this.getString(R.string.user));

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setView(addView);
				builder.setPositiveButton(this.getString(R.string.add),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							AlertDialog alertDialog = (AlertDialog) dialog;

							if (alertDialog == null || alertDialog.getCurrentFocus() == null)
								return;

							EditText entry = (EditText) alertDialog.getCurrentFocus().findViewById(R.id.entry);
							String value = entry.getText().toString().trim();

							TwFilterFacade twFilterFacade = new TwFilterFacade(TwFilters.this);
							boolean changed = false;

							if (type.equals(getString(R.string.tw_words_filter))) {
								if (twFilterFacade.getTwFilterWordId(value) < 1) {
									twFilterFacade.insertTwFilterWord(value);
									changed = true;
								} else {
									Log.w(TwFilters.class.getName(), "Word already stored.");
									UIHelper.showMessage(TwFilters.this, getString(R.string.error_tw_word_already_stored));
								}
							} else if (type.equals(getString(R.string.tw_hashtags_filter))) {
								if (twFilterFacade.getTwFilterHashtagId(value) < 1) {
									twFilterFacade.insertTwFilterHashtag(value);
									changed = true;
								} else {
									Log.w(TwFilters.class.getName(), "Hashtag already stored.");
									UIHelper.showMessage(TwFilters.this, getString(R.string.error_tw_hashtag_already_stored));
								}
							} else if (type.equals(getString(R.string.tw_users_filter))) {
								if (twFilterFacade.getTwFilterUserId(value) < 1) {
									twFilterFacade.insertTwFilterUser(value);
									changed = true;
								} else {
									Log.w(TwFilters.class.getName(), "User already stored.");
									UIHelper.showMessage(TwFilters.this, getString(R.string.error_tw_user_already_stored));
								}
							}

							if (changed) {
								adapter.add(value);
								adapter.sort(new Comparator<String>() {
									public int compare(String object1, String object2) {
										return object1.compareTo(object2);
									};
								});
								adapter.notifyDataSetChanged();
							}
						}
					});
				builder.setNegativeButton(this.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// just close the dialog
						}
					});
				builder.create();
				builder.show();
				break;
			case R.id.ok:
				this.finish();
				break;
			default:
				break;
		}
	}

	/**
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public final void onItemClick(final AdapterView<?> parent, final View view,	final int position, final long id) {
		// do nothing...
	}

	/**
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public final boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {

		final Builder builder = new Builder(this);
		builder.setItems(R.array.dialog_delete,
			new android.content.DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int which) {
					switch (which) {
						case 0:
							String value = adapter.getItem(position);

							TwFilterFacade twFilterFacade = new TwFilterFacade(TwFilters.this);
							boolean changed = false;

							if (type.equals(getString(R.string.tw_words_filter))) {
								twFilterFacade.deleteTwFilterWord(value);
								Log.d(TwFilters.class.getName(), "Word deleted: " + value);
								changed = true;
							} else if (type.equals(getString(R.string.tw_hashtags_filter))) {
								twFilterFacade.deleteTwFilterHashtag(value);
								Log.d(TwFilters.class.getName(), "Hashtag deleted: " + value);
								changed = true;
							} else if (type.equals(getString(R.string.tw_users_filter))) {
								twFilterFacade.deleteTwFilterUser(value);
								Log.d(TwFilters.class.getName(), "User deleted: " + value);
								changed = true;
							}

							if (changed) {
								adapter.remove(value);
								adapter.notifyDataSetChanged();
							}

							break;
						default:
							break;
					}
				}
			});
		//builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();

		return false;
	}
}
