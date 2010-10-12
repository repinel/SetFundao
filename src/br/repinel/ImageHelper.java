package br.repinel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownServiceException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
	 * @return The image.
	 * @throws MainException 
	 */
	public static Bitmap downloadImage(String imageUrl, Resources res) throws MainException {
		Bitmap bmImg = null;

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

			bmImg = BitmapFactory.decodeStream(is);
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

		return bmImg;
	}
}
