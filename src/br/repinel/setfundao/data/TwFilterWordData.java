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

package br.repinel.setfundao.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.repinel.R;

/**
 * @author Roque Pinel
 *
 */
public class TwFilterWordData {
	private static final String TAG = "TwFilterWordData";

	/** Table name. */
	public static final String TABLE_NAME = "tw_filter_word";

	public static final String ID = "id";
	public static final String WORD = "word";

	public static void onCreate(final Context context, final SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ WORD + " TEXT"
				+ ");");

		String[] words = context.getResources().getStringArray(R.array.default_tw_filter_words);
		for (int i = 0; i < words.length; i++) {
			TwFilterWordData.insertWord(db, words[i]);
			Log.d(DataProvider.class.getName(), "TwFilterWord: " + words[i]);
		}
	}

	public static void onUpgrade(final Context context, final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(context, db);
	}

	private static long insertWord (final SQLiteDatabase db, final String word) {
		ContentValues values = new ContentValues();
		values.put(WORD, word);
		return db.insert(TABLE_NAME, null, values);
	}
}
