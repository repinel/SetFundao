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

package br.repinel.setfundao.helper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownServiceException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import br.repinel.R;
import br.repinel.setfundao.ui.exception.MainException;

/**
 * Helper methods to simplify the tasks.
 * 
 * @author Roque Pinel
 *
 */
public class ImageHelper {

	/**
	 * Download an image given a URL.
	 * 
	 * @param imageUrl The URL of the image.
	 * @param res The resource.
	 * @return The image.
	 * @throws MainException 
	 */
	public static Bitmap downloadImage(String imageUrl, Resources res) throws MainException {
		int connectionCounter = 0;

		int maxRetryConnection = res.getInteger(R.integer.max_retry_connection);

		Bitmap image = null;

		while (connectionCounter < maxRetryConnection) {
			image = realDownloadImage(imageUrl, res);

			if (image != null)
				break;

			connectionCounter++;

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// empty
			}
		}

		return image;
	}

	/**
	 * Responsable for the real download.
	 * 
	 * @param imageUrl The URL of the image.
	 * @param res The resource.
	 * @return The image.
	 * @throws MainException
	 */
	private static Bitmap realDownloadImage(String imageUrl, Resources res) throws MainException {
		Bitmap image = null;

		URL myFileUrl = null;

		try {
			myFileUrl = new URL(imageUrl);
		} catch (MalformedURLException e) {
			throw new MainException(res.getText(R.string.error_url));
		}

		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();

			InputStream is = conn.getInputStream();

			image = BitmapFactory.decodeStream(is);
		} catch (UnknownServiceException e) {
			Log.e(ImageHelper.class.getName(), e.getMessage());
			throw new MainException(res.getText(R.string.error_unknow_service));
		} catch (SocketTimeoutException e) {
			Log.e(ImageHelper.class.getName(), e.getMessage());
			throw new MainException(res.getText(R.string.error_timeout)); 
		} catch (IOException e) {
			Log.e(ImageHelper.class.getName(), e.getMessage());
			throw new MainException(res.getText(R.string.error_io));
		} catch (IllegalStateException e) {
			Log.e(ImageHelper.class.getName(), e.getMessage());
			throw new MainException(res.getText(R.string.error_illegal_state));
		}

		return image;
	}

	/**
	 * Save an image to filesystem.
	 * 
	 * @param activity The activity
	 * @param image The image to be saved
	 * @param filename The filename
	 * @throws MainException
	 */
	public static void saveImage(Activity activity, Bitmap image, String filename) throws MainException {
		Resources res = activity.getResources();

		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

			FileOutputStream fos = activity.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(bytes.toByteArray());
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e(ImageHelper.class.getName(), e.getMessage());
			throw new MainException(res.getText(R.string.error_file_not_found));
		} catch (IOException e) {
			Log.e(ImageHelper.class.getName(), e.getMessage());
			throw new MainException(res.getText(R.string.error_input_output));
		}
	}

	/**
	 * Load an image from filesystem.
	 * 
	 * @param activity The activity
	 * @param filename The filename
	 * @return The image loaded
	 * @throws MainException
	 */
	public static Bitmap loadImage (Activity activity, String filename) throws MainException {
		Bitmap image = BitmapFactory.decodeFile(activity.getFileStreamPath(filename).getAbsolutePath());

		return image;
	}
}
