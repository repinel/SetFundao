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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import br.repinel.setfundao.core.TwItem;
import br.repinel.setfundao.core.TwUser;

/**
 * @author Roque Pinel
 *
 */
public class TwItemFacade {
	private DataProvider dataProvider;

	public TwItemFacade(Context context) {
		this.dataProvider = new DataProvider(context);
	}

	public long insertTwItem(TwItem twItem) {
		long userId = getTwUserId(twItem.user.username);

		if (userId < 1)
			userId = insertTwUser(twItem.user);

		long itemId = insertTwItem(twItem.text, twItem.createdAt, userId);

		return itemId;
	}

	private long insertTwItem(String text, Date createdAt, long userId) {
		ContentValues values = new ContentValues();
		values.put(TwItemData.TEXT, text);
		values.put(TwItemData.CREATED_AT, createdAt.getTime());
		values.put(TwItemData.USER_ID, userId);

		return this.dataProvider.insert(TwItemData.TABLE_NAME, values);
	}

	public long insertTwUser(TwUser twUser) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		twUser.profileImage.compress(Bitmap.CompressFormat.PNG, 100, baos);

		ContentValues values = new ContentValues();
		values.put(TwUserData.USERNAME, twUser.username);
		values.put(TwUserData.PROFILE_IMAGE, baos.toByteArray());

		return this.dataProvider.insert(TwUserData.TABLE_NAME, values);
	}

	public long getTwUserId(String username) {
		return this.dataProvider.getId(TwUserData.TABLE_NAME,
			new String[] { TwUserData.ID }, TwUserData.USERNAME + " = ?",
			new String[] { username });
	}

	public void deleteAllTwItems() {
		SQLiteDatabase db = this.dataProvider.databaseHelper.getWritableDatabase();

		db.delete(TwItemData.TABLE_NAME, null, null);
		db.delete(TwUserData.TABLE_NAME, null, null);
	}

	public List<TwItem> selectAllTwItems() {
		SQLiteDatabase db = this.dataProvider.databaseHelper.getReadableDatabase();

		List<TwItem> list = new ArrayList<TwItem>();

		//Cursor cursor = db.rawQuery("SELECT i.text, i.created_at, u.username, u.profile_image FROM tw_item i, tw_user u WHERE i.user_id = u.id ORDER BY i.created_at DESC", null);
		Cursor cursor = db.rawQuery(
			"SELECT i." + TwItemData.TEXT + ", i." + TwItemData.CREATED_AT + ", u." + TwUserData.USERNAME + ", u." + TwUserData.PROFILE_IMAGE
			+ " FROM " + TwItemData.TABLE_NAME + " i, " + TwUserData.TABLE_NAME + " u"
			+ " WHERE i." + TwItemData.USER_ID + " = u." + TwUserData.ID
			+ " ORDER BY i." + TwItemData.CREATED_AT + " DESC", null);

		if (cursor.moveToFirst()) {
			do {
				byte[] profileImageByte = cursor.getBlob(3);

				TwUser twUser = new TwUser();
				twUser.username = cursor.getString(2);
				twUser.profileImage = BitmapFactory.decodeByteArray(profileImageByte, 0, profileImageByte.length);

				TwItem twItem = new TwItem();
				twItem.text = cursor.getString(0);
				twItem.createdAt = new Date(cursor.getLong(1));
				twItem.user = twUser;

				list.add(twItem);
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
