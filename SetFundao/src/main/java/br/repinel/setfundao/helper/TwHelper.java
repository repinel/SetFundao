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

package br.repinel.setfundao.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import br.repinel.setfundao.core.TwAuth;
import br.repinel.setfundao.util.Constants;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * TwHelper
 *
 * @author Roque Pinel.
 */
public class TwHelper {
	private static final String TW_CONSUMER_KEY = "yzdFI7mFTXL1uxjzfXeoEg";

	private static final String TW_CONSUMER_SECRET = "3hOzhz07RiAQZRHTetSkJuCwSZGCnRDd0I3QlYK5hI";

	private static RequestToken requestToken;

	private static Twitter twitter;

	static {
		twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(TW_CONSUMER_KEY, TW_CONSUMER_SECRET);
	}

	/**
	 * @return The Twitter API not authenticated.
	 */
	public static Twitter getTwitter() {
		return TwHelper.getTwitter(null, null);
	}

	/**
	 * @return The Twitter API authenticated if <code>token</code> and <code>tokenSecret</code> are valid.
	 */
	public static Twitter getTwitter(String token, String tokenSecret) {
		if (!StringHelper.isBlank(token) && !StringHelper.isBlank(tokenSecret)) {
			AccessToken accessToken = new AccessToken(token, tokenSecret);
			twitter.setOAuthAccessToken(accessToken);
		}
		return twitter;
	}

	/**
	 * @return The OAuth authentication URL.
	 */
	public static String getAuthURL() {
		String authURL = null;

		try {
			TwHelper.getTwitter().setOAuthAccessToken(null);
			requestToken = TwHelper.getTwitter().getOAuthRequestToken(Constants.TW_CALLBACK_URL);

			authURL = requestToken.getAuthenticationURL();
		} catch (TwitterException e) {
			Log.d(TwHelper.class.getName(), e.getMessage());
		}

		return authURL;
	}

	/**
	 * @param oauthVerifier The Twitter user's PIN.
	 *
	 * @return The authentication information.
	 */
	public static TwAuth getAuth(String oauthVerifier) {
		TwAuth twAuth = null;

		try {
			AccessToken accessToken = TwHelper.getTwitter().getOAuthAccessToken(requestToken, oauthVerifier);

			twAuth = new TwAuth(accessToken.getToken(), accessToken.getTokenSecret());
		} catch (Exception e) {
			Log.d(TwHelper.class.getName(), e.getMessage());
		}

		return twAuth;
	}
}
