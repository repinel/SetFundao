package br.repinel.setfundao.ui.prefs;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import br.repinel.R;

public class TwWords extends ListActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

	//private TwWordsAdapter adapter;

	String[] words = new String[] { "fundao", "vermelha", "amarela"};

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setTitle(this.getString(R.string.settings_tw)
			+ " > " + this.getString(R.string.words_filter_));

		this.setContentView(R.layout.list_ok_add);

		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, words));

		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		this.findViewById(R.id.ok).setOnClickListener(this);
		this.findViewById(R.id.add).setOnClickListener(this);
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.add:
				//Intent intent = new Intent(this, TwWordsEdit.class);
				//this.startActivity(intent);
				break;
			case R.id.ok:
				this.finish();
				break;
			default:
				break;
		}
	}

	/**
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public final void onItemClick(final AdapterView<?> parent, final View view,	final int position, final long id) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public final boolean onItemLongClick(final AdapterView<?> parent,
			final View view, final int position, final long id) {

		// TODO Auto-generated method stub

		return false;
	}
}
