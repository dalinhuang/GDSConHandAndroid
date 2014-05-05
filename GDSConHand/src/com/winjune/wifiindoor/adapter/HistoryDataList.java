package com.winjune.wifiindoor.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.winjune.wifiindoor.R;

public class HistoryDataList extends ArrayAdapter<String> {

	private int resourceId;  
	private Context context;
	private ArrayList<String> items;
	 
	public HistoryDataList(Context context, int resource, ArrayList<String> items) {
		super(context, resource, items);
		this.context = context;
		this.resourceId = resource;
		this.items = items;
		// TODO Auto-generated constructor stub
	}
	
	public String getRecord(int position){
		return items.get(position);
	}
	
    @Override  
    public View getView(int position, View convertView, ViewGroup parent){  
        LayoutInflater vi = LayoutInflater.from(context);  

		View view=vi.inflate(R.layout.list_history, null);
		
		TextView historyRecord = (TextView) view.findViewById(R.id.history_record);
		historyRecord.setText(items.get(position));
		    
        return view;  
    } 
}