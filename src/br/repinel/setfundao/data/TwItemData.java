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

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Roque Pinel
 *
 */
public class TwItemData {
	private static final String TAG = "TwItemData";

	/** Table name. */
	public static final String TABLE_NAME = "tw_item";

	public static final String ID = "id";
	public static final String USER_ID = "user_id";
	public static final String TEXT = "text";
	public static final String CREATED_AT = "created_at";

	public static void onCreate(final SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ USER_ID + " INTEGER REFERENCES "
					+ TwFilterUserData.TABLE_NAME + "(" + TwFilterUserData.ID + "),"
				+ TEXT + " TEXT,"
				+ CREATED_AT + " LONG"
				+ ");");
	}

	public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
