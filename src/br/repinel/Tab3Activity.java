package br.repinel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Third tab.
 * 
 * @author Roque Pinel
 *
 */
public class Tab3Activity extends Activity implements OnClickListener {

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab);

		Resources res = getResources();
		String[] urls = res.getStringArray(R.array.urls);

		Log.i(Tab3Activity.class.getName(), "onCreate:" + urls[2]);

		try {
			Bitmap bmImg = ImageHelper.downloadImage(urls[2], res);
			
			ImageView imgView = (ImageView) findViewById(R.id.cameraImage);

			if (bmImg != null) {
				imgView.setImageBitmap(bmImg);
			} else {
				imgView.setImageResource(R.drawable.sem_imagem);
			}

			imgView.setOnClickListener(this);
		} catch (MainException e) {
			ImageView imgView = (ImageView) findViewById(R.id.cameraImage);
			imgView.setImageResource(R.drawable.sem_imagem);
			imgView.setOnClickListener(this);

			showMessage(e.getMessage());
		}
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		Resources res = getResources();
		String[] urls = res.getStringArray(R.array.urls);

		Log.i(Tab3Activity.class.getName(), "onClick:" + urls[2]);

		try {
			Bitmap bmImg = ImageHelper.downloadImage(urls[2], res);
			
			ImageView imgView = (ImageView) findViewById(R.id.cameraImage);

			if (bmImg != null) {
				imgView.setImageBitmap(bmImg);
			} else {
				imgView.setImageResource(R.drawable.sem_imagem);
			}

			imgView.setOnClickListener(this);
		} catch (MainException e) {
			ImageView imgView = (ImageView) findViewById(R.id.cameraImage);
			imgView.setImageResource(R.drawable.sem_imagem);
			imgView.setOnClickListener(this);

			showMessage(e.getMessage());
		}
	}

	/**
	 * Show a message dialog.
	 * 
	 * @param message The message to be shown.
	 */
	private void showMessage(String message) {
		View messageView = getLayoutInflater().inflate(R.layout.message, null, false);

		TextView textView = (TextView) messageView.findViewById(R.id.message);
		int defaultColor = textView.getTextColors().getDefaultColor();
		textView.setTextColor(defaultColor);
		textView.setText(message);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.app_icon);
		builder.setTitle(R.string.app_name);
		builder.setView(messageView);
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", null);
		builder.create();
		builder.show();
	}
}