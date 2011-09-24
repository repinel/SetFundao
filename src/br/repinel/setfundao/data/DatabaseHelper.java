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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Helper.
 * 
 * @author Roque Pinel
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private Context context;

	/**
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DataProvider.DATABASE_NAME, null, DataProvider.DATABASE_VERSION);
		this.context = context;
	}

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		DataProvider.TwUserData.onCreate(db);
		DataProvider.TwItemData.onCreate(db);
		DataProvider.TwFilterWordData.onCreate(context, db);
		DataProvider.TwFilterHashtagData.onCreate(context, db);
		DataProvider.TwFilterUserData.onCreate(context, db);
	}

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		DataProvider.TwUserData.onUpgrade(db, oldVersion, newVersion);
		DataProvider.TwItemData.onUpgrade(db, oldVersion, newVersion);
		DataProvider.TwFilterWordData.onUpgrade(context, db, oldVersion, newVersion);
		DataProvider.TwFilterHashtagData.onUpgrade(context, db, oldVersion, newVersion);
		DataProvider.TwFilterUserData.onUpgrade(context, db, oldVersion, newVersion);
	}
}
