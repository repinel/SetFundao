/*
 * Copyright (C) 2013 Roque Pinel
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.Log;

import br.repinel.R;
import br.repinel.setfundao.core.TwAuth;
import br.repinel.setfundao.data.DataProvider;
import br.repinel.setfundao.helper.AnalyticsHelper;
import br.repinel.setfundao.helper.StringHelper;
import br.repinel.setfundao.helper.TwHelper;
import br.repinel.setfundao.helper.UIHelper;
import br.repinel.setfundao.util.Constants;

/**
 * Preferences.
 * 
 * @author Roque Pinel
 */
public class Preferences extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String BUNDLE_RESTORE = "restore";
	private static final String BUNDLE_TW_SIGN_OUT = "tw_sign_out";

	private static final String RESET_SETTINGS_XML_KEY = "reset_settings";
	private static final String TW_CATEGORY_XML_KEY = "twitter_category";
	private static final String TW_SIGN_OUT_XML_KEY = "twitter_sign_out";
	private static final String TW_SIGN_IN_XML_KEY = "twitter_sign_in";

	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AnalyticsHelper.getInstance(this).trackPageView("/Preferences");

		if (this.getIntent().getExtras() != null
			&& this.getIntent().getExtras().get(BUNDLE_RESTORE) != null
			&& this.getIntent().getExtras().getBoolean(BUNDLE_RESTORE)) {
			UIHelper.showMessage(getApplicationContext(), getString(R.string.reset_settings_message));
		} else if (this.getIntent().getExtras() != null
				&& this.getIntent().getExtras().get(BUNDLE_TW_SIGN_OUT) != null
				&& this.getIntent().getExtras().getBoolean(BUNDLE_TW_SIGN_OUT)) {
			UIHelper.showMessage(getApplicationContext(), getString(R.string.twitter_sign_out_message));
		} else {
			// handle Twitter OAUth URL before fixing preferences options
			handleTwOAuthURL();
		}

		// ensuring the default value...
		UIHelper.getFetchImageOnCreateActivity(this, getResources());
		UIHelper.getFetchTwOnCreateActivity(this, getResources());

		this.addPreferencesFromResource(R.xml.prefs);

		Preference p = this.findPreference(RESET_SETTINGS_XML_KEY);
		if (p != null) {
			p.setOnPreferenceClickListener(
					new Preference.OnPreferenceClickListener() {
						public boolean onPreferenceClick(final Preference preference) {
							resetSettingsDialog();
							return true;
						}
					});
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		TwAuth twAuth = new TwAuth(prefs.getString(Constants.PREF_TW_ACCESS_TOKEN, null), prefs.getString(Constants.PREF_TW_ACCESS_TOKEN_SECRET, null));

		// if not signed in to Twitter
		if (StringHelper.isBlank(twAuth.oauthAccessToken) || StringHelper.isBlank(twAuth.oauthAccessTokenSecret)) {
			((PreferenceGroup) findPreference(TW_CATEGORY_XML_KEY)).removePreference(this.findPreference(TW_SIGN_OUT_XML_KEY));
			p = this.findPreference(TW_SIGN_IN_XML_KEY);
			if (p != null) {
				p.setOnPreferenceClickListener(
						new Preference.OnPreferenceClickListener() {
							public boolean onPreferenceClick(final Preference preference) {
								twSignInDialog();
								return true;
							}
						});
			}
		} else {
			((PreferenceGroup) findPreference(TW_CATEGORY_XML_KEY)).removePreference(this.findPreference(TW_SIGN_IN_XML_KEY));
			p = this.findPreference(TW_SIGN_OUT_XML_KEY);
			if (p != null) {
				p.setOnPreferenceClickListener(
						new Preference.OnPreferenceClickListener() {
							public boolean onPreferenceClick(final Preference preference) {
								twSignOutDialog();
								return true;
							}
						});
			}
		}
	}

	/**
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO: think about a way to alert the other activities
	}

	private void twSignInDialog() {
		String authURL = TwHelper.getAuthURL();

		if (authURL != null)
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authURL)));
		else
			UIHelper.showMessage(getApplicationContext(), getString(R.string.error_tw_auth_url));
	}

	private void twSignOutDialog() {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.remove(Constants.PREF_TW_ACCESS_TOKEN);
		editor.remove(Constants.PREF_TW_ACCESS_TOKEN_SECRET);
		editor.commit();

		Intent intent = new Intent(this, Preferences.class);
		intent.putExtra(BUNDLE_TW_SIGN_OUT, true);
		startActivity(intent);
		finish();
	}

	private void handleTwOAuthURL() {
		Uri uri = getIntent().getData();

		if (uri != null && uri.toString().startsWith(Constants.TW_CALLBACK_URL)) {
			String oauthVerifier = uri.getQueryParameter(Constants.TW_EXTRA_OAUTH_VERIFIER);

			TwAuth twAuth = TwHelper.getAuth(oauthVerifier);

			if (twAuth != null) {
				SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
				editor.putString(Constants.PREF_TW_ACCESS_TOKEN, twAuth.oauthAccessToken);
				editor.putString(Constants.PREF_TW_ACCESS_TOKEN_SECRET, twAuth.oauthAccessTokenSecret);
				editor.commit();
			} else {
				UIHelper.showMessage(getApplicationContext(), getString(R.string.error_tw_auth));
			}
		}
	}

	/**
	 * Reset all settings.
	 */
	private void resetSettingsDialog() {
		AnalyticsHelper.getInstance(this).trackEvent(getClass().getName(), "Click", "ResetSettings", 0);

		Log.w(Preferences.class.getName(), "Resetting settings...");

		DataProvider dataProvider = new DataProvider(this);
		dataProvider.resetData();

		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.remove(getString(R.string.update_interval));
		editor.remove(getString(R.string.fetch_image_on_create_activity));
		editor.remove(getString(R.string.fetch_tw_on_create_activity));
		editor.commit();

		Intent intent = new Intent(this, Preferences.class);
		intent.putExtra(BUNDLE_RESTORE, true);
		startActivity(intent);
		finish();
	}
}
