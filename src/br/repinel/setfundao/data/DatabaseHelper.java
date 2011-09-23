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

package br.repinel.setfundao.data;

import br.repinel.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private Context context;

	public DatabaseHelper(Context context) {
		super(context, DataProvider.DATABASE_NAME, null, DataProvider.DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DataProvider.TwUserData.onCreate(db);
		DataProvider.TwItemData.onCreate(db);
		DataProvider.TwFilterWordData.onCreate(db);
		DataProvider.TwFilterHashtagData.onCreate(db);
		DataProvider.TwFilterUserData.onCreate(db);

		String[] words = this.context.getResources().getStringArray(R.array.default_tw_filter_words);
		for (int i = 0; i < words.length; i++)
			DataProvider.TwFilterWordData.insertWord(db, words[i]);

		String[] hashtags = this.context.getResources().getStringArray(R.array.default_tw_filter_hashtags);
		for (int i = 0; i < hashtags.length; i++)
			DataProvider.TwFilterHashtagData.insertHashtag(db, hashtags[i]);

		String[] users = this.context.getResources().getStringArray(R.array.default_tw_filter_users);
		for (int i = 0; i < users.length; i++)
			DataProvider.TwFilterUserData.insertUser(db, users[i]);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		DataProvider.TwUserData.onUpgrade(db, oldVersion, newVersion);
		DataProvider.TwItemData.onUpgrade(db, oldVersion, newVersion);
		DataProvider.TwFilterWordData.onUpgrade(db, oldVersion, newVersion);
		DataProvider.TwFilterHashtagData.onUpgrade(db, oldVersion, newVersion);
		DataProvider.TwFilterUserData.onUpgrade(db, oldVersion, newVersion);
	}
}
