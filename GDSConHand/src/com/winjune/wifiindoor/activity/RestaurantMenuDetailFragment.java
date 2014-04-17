package com.winjune.wifiindoor.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.id;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.dummy.DummyContent;

/**
 * A fragment representing a single MenuItem detail screen. This fragment is
 * either contained in a {@link RestaurantInfoActivity} in two-pane mode (on
 * tablets) or a {@link RestaurantMenuDetailActivity} on handsets.
 */
public class RestaurantMenuDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RestaurantMenuDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_menuitem_detail,
				container, false);

		// Show the dummy content as text in a TextView.
/*		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.menuitem_detail))
					.setText(mItem.content);
		}
*/
		return rootView;
	}
}
