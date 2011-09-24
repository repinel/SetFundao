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

import br.repinel.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataProvider {
	public static final String PACKAGE = "br.repinel.setfundao";
	public static final String AUTHORITY = PACKAGE + ".provider";

	public  static final String DATABASE_NAME = "setfundao.db";
	public static final int DATABASE_VERSION = 10;

	public static final class TwUserData {
		private static final String TAG = "TwUserData";

		/** Table name. */
		public static final String TABLE_NAME = "tw_user";

		public static final String ID = "id";
		public static final String USERNAME = "username";
		public static final String PROFILE_IMAGE_URL = "profile_image_url";

		public static void onCreate(final SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ USERNAME + " TEXT,"
					+ PROFILE_IMAGE_URL + " TEXT"
					+ ");");
		}

		public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
			Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	public static final class TwItemData {
		private static final String TAG = "TwItemData";

		/** Table name. */
		public static final String TABLE_NAME = "tw_item";

		public static final String ID = "id";
		public static final String USER_ID = "user_id";
		public static final String TEXT = "text";
		public static final String CREATED_AD = "created_at";

		public static void onCreate(final SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ USER_ID + " INTEGER,"
					+ TEXT + " TEXT,"
					+ CREATED_AD + " LONG"
					+ ");");
		}

		public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
			Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	public static final class TwFilterWordData {
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
				DataProvider.TwFilterWordData.insertWord(db, words[i]);
				Log.d(DataProvider.class.getName(), "TwFilterWord: " + words[i]);
			}
		}

		public static void onUpgrade(final Context context, final SQLiteDatabase db, final int oldVersion, final int newVersion) {
			Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(context, db);
		}

		public static long insertWord (final SQLiteDatabase db, final String value) {
			SQLiteStatement insertStmt = db.compileStatement(
					"INSERT INTO " + TwFilterWordData.TABLE_NAME
					+ "(" + TwFilterWordData.WORD + ") values (?)");

			insertStmt.bindString(1, value);

			return insertStmt.executeInsert();
		}
	}

	public static final class TwFilterHashtagData {
		private static final String TAG = "TwFilterHashtagData";

		/** Table name. */
		public static final String TABLE_NAME = "tw_filter_hashtag";

		public static final String ID = "id";
		public static final String HASHTAG = "hashtag";

		public static void onCreate(final Context context, final SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ HASHTAG + " TEXT"
					+ ");");

			String[] hashtags = context.getResources().getStringArray(R.array.default_tw_filter_hashtags);
			for (int i = 0; i < hashtags.length; i++) {
				DataProvider.TwFilterHashtagData.insertHashtag(db, hashtags[i]);
				Log.d(DataProvider.class.getName(), "TwFilterHashtag: " + hashtags[i]);
			}
		}

		public static void onUpgrade(final Context context, final SQLiteDatabase db, final int oldVersion, final int newVersion) {
			Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(context, db);
		}

		public static long insertHashtag(final SQLiteDatabase db, final String value) {
			SQLiteStatement insertStmt = db.compileStatement(
					"INSERT INTO " + TwFilterHashtagData.TABLE_NAME
					+ "(" + TwFilterHashtagData.HASHTAG + ") values (?)");

			insertStmt.bindString(1, value);

			return insertStmt.executeInsert();
		}
	}

	public static final class TwFilterUserData {
		private static final String TAG = "TwFilterUserData";

		/** Table name. */
		public static final String TABLE_NAME = "tw_filter_user";

		public static final String ID = "id";
		public static final String USERNAME = "username";

		public static void onCreate(final Context context, final SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ USERNAME + " TEXT"
					+ ");");

			String[] users = context.getResources().getStringArray(R.array.default_tw_filter_users);
			for (int i = 0; i < users.length; i++) {
				DataProvider.TwFilterUserData.insertUser(db, users[i]);
				Log.d(DataProvider.class.getName(), "TwFilterUser: " + users[i]);
			}
		}

		public static void onUpgrade(final Context context, final SQLiteDatabase db, final int oldVersion, final int newVersion) {
			Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(context, db);
		}

		public static long insertUser(final SQLiteDatabase db, final String value) {
			SQLiteStatement insertStmt = db.compileStatement(
					"INSERT INTO " + TwFilterUserData.TABLE_NAME
					+ "(" + TwFilterUserData.USERNAME + ") values (?)");

			insertStmt.bindString(1, value);

			return insertStmt.executeInsert();
		}
	}

	private DatabaseHelper databaseHelper;

	public DataProvider(Context context) {
		this.databaseHelper = new DatabaseHelper(context);
	}

	/**
	 * Reset all data.
	 */
	public void resetData() {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		databaseHelper.onCreate(db);
	}

	public long insertTwItem(TwItem twItem) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		SQLiteStatement insertStmt = db.compileStatement(
				"INSERT INTO " + TwUserData.TABLE_NAME
				+ "(" + TwUserData.USERNAME + "," + TwUserData.PROFILE_IMAGE_URL
				+ ") values (?,?)");

			insertStmt.bindString(1, twItem.username);
			insertStmt.bindString(2, twItem.profileImageURL.toString());

		long userId = insertStmt.executeInsert();

		if (userId < 0)
			throw new RuntimeException("Erro ao inserir usuário.");

		insertStmt = db.compileStatement(
			"INSERT INTO " + TwItemData.TABLE_NAME
			+ "(" + TwItemData.USER_ID + "," + TwItemData.TEXT + "," + TwItemData.CREATED_AD
			+ ") values (?,?,?)");

		insertStmt.bindLong(1, userId);
		insertStmt.bindString(2, twItem.text);
		insertStmt.bindLong(3, twItem.createdAt.getTime());

		return insertStmt.executeInsert();
	}

	public void deleteAllTwItems() {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		db.delete(TwItemData.TABLE_NAME, null, null);
		db.delete(TwUserData.TABLE_NAME, null, null);
	}

	/* **** FILTERS **** */

	public long insertTwFilterWord(String value) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		return TwFilterWordData.insertWord(db, value);
	}

	public long getTwFilterWord(String value) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		Cursor cursor = db.query(TwFilterWordData.TABLE_NAME, new String[] { TwFilterWordData.ID },
				TwFilterWordData.WORD + " = '" + value + "'", null, null, null, null);
		if (cursor.moveToFirst())
			return cursor.getLong(0);

		return -1;
	}

	public void deleteTwFilterWord(String value) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("DELETE FROM " + TwFilterWordData.TABLE_NAME
			+ " WHERE " + TwFilterWordData.WORD + " = '" + value + "'");
	}

	public List<String> selectAllTwFilterWords() {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		List<String> list = new ArrayList<String>();
		Cursor cursor = db.query(TwFilterWordData.TABLE_NAME, new String[] { TwFilterWordData.WORD },
				null, null, null, null, TwFilterWordData.WORD + " asc");
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0)); 
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public long insertTwFilterHashtag(String value) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		return TwFilterHashtagData.insertHashtag(db, value);
	}

	public long getTwFilterHashtag(String value) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		Cursor cursor = db.query(TwFilterHashtagData.TABLE_NAME, new String[] { TwFilterWordData.ID },
				TwFilterHashtagData.HASHTAG + " = '" + value + "'", null, null, null, null);
		if (cursor.moveToFirst())
			return cursor.getLong(0);

		return -1;
	}

	public void deleteTwFilterHashtag(String value) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("DELETE FROM " + TwFilterHashtagData.TABLE_NAME
			+ " WHERE " + TwFilterHashtagData.HASHTAG + " = '" + value + "'");
	}

	public List<String> selectAllTwFilterHashtags() {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		List<String> list = new ArrayList<String>();
		Cursor cursor = db.query(TwFilterHashtagData.TABLE_NAME, new String[] { TwFilterHashtagData.HASHTAG },
				null, null, null, null, TwFilterHashtagData.HASHTAG + " asc");
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0)); 
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public long insertTwFilterUser(String value) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		return TwFilterUserData.insertUser(db, value);
	}

	public long getTwFilterUser(String value) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		Cursor cursor = db.query(TwFilterUserData.TABLE_NAME, new String[] { TwFilterUserData.ID },
				TwFilterUserData.USERNAME + " = '" + value + "'", null, null, null, null);
		if (cursor.moveToFirst())
			return cursor.getLong(0);

		return -1;
	}

	public void deleteTwFilterUser(String value) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("DELETE FROM " + TwFilterUserData.TABLE_NAME
			+ " WHERE " + TwFilterUserData.USERNAME + " = '" + value + "'");
	}

	public List<String> selectAllTwFilterUsers() {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		List<String> list = new ArrayList<String>();
		Cursor cursor = db.query(TwFilterUserData.TABLE_NAME, new String[] { TwFilterUserData.USERNAME },
				null, null, null, null, TwFilterUserData.USERNAME + " asc");
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0)); 
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}
}
