package com.winjune.wifiindoor.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.poi.EventManager;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlayhouseInfo;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class EventListByTimeFragment extends ListFragment {

	private OnFragmentInteractionListener mListener;
	private ArrayList<PlayhouseInfo> mEventItems;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public EventListByTimeFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Calendar currentTime = Calendar.getInstance();
		int currentHour = 9;
		int stopHour = 17; // 17:00 is the close time
		
		mEventItems = EventManager.getTodayEventListByTime(currentHour, stopHour);
		
		setListAdapter(new EventListByTime(getActivity(),
				R.layout.list_event_by_time, mEventItems));
	}

/*	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
*/
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			//mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
		}
			
		Intent i = new Intent(getActivity(), PlayhouseInfoActivity.class); 
		Bundle bundle = new Bundle();
		bundle.putInt(PlayhouseInfoActivity.BUNDLE_KEY_POI_ID, mEventItems.get(position).id);
		
		i.putExtras(bundle);
		startActivity(i);	
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(String id);
	}
	
	public class EventListByTime extends ArrayAdapter<PlayhouseInfo> {

		private int resourceId;  
		private Context context;
		 
		public EventListByTime(Context context, int resource, ArrayList<PlayhouseInfo> items) {
			super(context, resource, items);
			this.context = context;
			this.resourceId = resource;
			// TODO Auto-generated constructor stub
		}
		
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent){  
	        LayoutInflater vi = LayoutInflater.from(context);  
 
			View view=vi.inflate(R.layout.list_event_by_time, null);
			
			TextView title = (TextView) view.findViewById(R.id.event_title);
			title.setText(mEventItems.get(position).getLabel());
			
			TextView timeAndPlace = (TextView)view.findViewById(R.id.event_schedule);
			String place = POIManager.getHallLabel(mEventItems.get(position).getHall());
			timeAndPlace.setText(place);
				        
	        return view;  
	    }   		
		
		
		
	}

}
