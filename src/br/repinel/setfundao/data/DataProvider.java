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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Provides all data.
 * 
 * @author Roque Pinel
 *
 */
public class DataProvider {
	public static final String PACKAGE = "br.repinel.setfundao";
	public static final String AUTHORITY = PACKAGE + ".provider";

	public  static final String DATABASE_NAME = "setfundao.db";
	public static final int DATABASE_VERSION = 14;

	protected DatabaseHelper databaseHelper;

	/**
	 * @param context
	 */
	public DataProvider(Context context) {
		this.databaseHelper = new DatabaseHelper(context);
	}

	/**
	 * Reset all data.
	 */
	public void resetData() {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		databaseHelper.onCreate(db);

		if (db.isOpen())
			db.close();
	}

	/**
	 * Insert.
	 * 
	 * @param tableName The table name.
	 * @param values The values to be inserted.
	 * @return The id of the element inserted
	 */
	public long insert(final String tableName, final ContentValues values) {
		final SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
		long id = db.insert(tableName, null, values);

		if (db.isOpen())
			db.close();

		return id;
	}

	/**
	 * Delete.
	 * 
	 * @param tableName The table name.
	 * @param selection The selection.
	 * @param selectionArgs The selection's arguments.
	 * @return The number of elements deleted, otherwise 0.
	 */
	public int delete(final String tableName, final String selection, final String[] selectionArgs) {
		final SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
		int ret = db.delete(tableName, selection, selectionArgs);

		if (db.isOpen())
			db.close();

		return ret;
	}


	/**
	 * Get id of the item already inserted.
	 * Return 0 or less if could not find the element.
	 * 
	 * @param tableName The table name.
	 * @param columns The columns to be returned
	 * @param selection The selection.
	 * @param selectionArgs The selection's arguments.
	 * @return The id
	 */
	public long getId(final String tableName, final String[] columns, final String selection, final String[] selectionArgs) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);
		if (cursor.moveToFirst())
			return cursor.getLong(0);

		if (db.isOpen())
			db.close();

		return -1;
	}
}
