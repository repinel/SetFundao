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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Roque Pinel
 *
 */
public class TwFilterFacade {

	private DataProvider dataProvider;

	public TwFilterFacade(Context context) {
		this.dataProvider = new DataProvider(context);
	}

	/* **** Word **** */

	public long insertTwFilterWord(String word) {
		ContentValues values = new ContentValues();
		values.put(TwFilterWordData.WORD, word);

		return this.dataProvider.insert(TwFilterWordData.TABLE_NAME, values);
	}

	public long getTwFilterWordId(String word) {
		return this.dataProvider.getId(TwFilterWordData.TABLE_NAME,
			new String[] { TwFilterWordData.ID }, TwFilterWordData.WORD + " like ?",
			new String[] { word });
	}

	public void deleteTwFilterWord(String word) {
		this.dataProvider.delete(TwFilterWordData.TABLE_NAME, TwFilterWordData.WORD + " = ?", new String[] { word });
	}

	public List<String> selectAllTwFilterWords() {
		return selectAll(TwFilterWordData.TABLE_NAME, TwFilterWordData.WORD);
	}

	/* **** Hashtag **** */

	public long insertTwFilterHashtag(String hashtag) {
		ContentValues values = new ContentValues();
		values.put(TwFilterHashtagData.HASHTAG, hashtag);

		return this.dataProvider.insert(TwFilterHashtagData.TABLE_NAME, values);
	}

	public long getTwFilterHashtagId(String hashtag) {
		return this.dataProvider.getId(TwFilterHashtagData.TABLE_NAME,
			new String[] { TwFilterHashtagData.ID }, TwFilterHashtagData.HASHTAG + " like ?",
			new String[] { hashtag });
	}

	public void deleteTwFilterHashtag(String hashtag) {
		this.dataProvider.delete(TwFilterHashtagData.TABLE_NAME, TwFilterHashtagData.HASHTAG + " = ?", new String[] { hashtag });
	}

	public List<String> selectAllTwFilterHashtags() {
		return selectAll(TwFilterHashtagData.TABLE_NAME, TwFilterHashtagData.HASHTAG);
	}

	/* **** User **** */

	public long insertTwFilterUser(String username) {
		ContentValues values = new ContentValues();
		values.put(TwFilterUserData.USERNAME, username);

		return this.dataProvider.insert(TwFilterUserData.TABLE_NAME, values);
	}

	public long getTwFilterUserId(String username) {
		return this.dataProvider.getId(TwFilterUserData.TABLE_NAME,
			new String[] { TwFilterUserData.ID }, TwFilterUserData.USERNAME + " like ?",
			new String[] { username });
	}

	public void deleteTwFilterUser(String username) {
		this.dataProvider.delete(TwFilterUserData.TABLE_NAME, TwFilterUserData.USERNAME + " = ?", new String[] { username });
	}

	public List<String> selectAllTwFilterUsers() {
		return selectAll(TwFilterUserData.TABLE_NAME, TwFilterUserData.USERNAME);
	}

	/* **** COMMONS **** */

	private List<String> selectAll(String tableName, String columnName) {
		SQLiteDatabase db = this.dataProvider.databaseHelper.getReadableDatabase();

		List<String> list = new ArrayList<String>();
		Cursor cursor = db.query(tableName, new String[] { columnName }, null, null, null, null, columnName + " asc");
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		if (db.isOpen())
			db.close();

		return list;
	}
}
