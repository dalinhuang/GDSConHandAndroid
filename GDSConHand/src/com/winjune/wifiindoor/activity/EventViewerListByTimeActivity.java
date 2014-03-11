package com.winjune.wifiindoor.activity;


import com.winjune.wifiindoor.R;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventViewerListByTimeActivity extends ExpandableListActivity{
	  	@Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	          
	       setContentView(R.layout.event_list);  
	          
	        Adapter1 ada=new Adapter1(); //Adapter1的定义下面，自定义视图是由它实现的  
	        setListAdapter((ExpandableListAdapter) ada);   
	    }  
	  
	    public class Adapter1 extends BaseExpandableListAdapter {   
	      
	    	//初始化一些数据用于显示分组的标题，这个例子不是为了说明数据如何存取，所以这里用固定数据，使例子更突出重点。
	        private String[] groups =   
	        {  
	            "10:00 - 11:00",  
	            "11:00 - 12:00",  
	            "13:00 - 14:00",  
	        };  
	        
	      //初始化一些数据用于显示每个分组下的数据项，这个例子不是为了说明数据如何存取，所以这里用固定数据，使例子更突出重点。
	        private String[][] children =   
	        {  
	            { "name1" },  
	            { "name21", "name21" },  
	            { "name31", "name32", "name33" },  
	        };  
	          
	        @Override  
	        public Object getChild(int groupPosition, int childPosition) {  
	            return children[groupPosition][childPosition];  //获取数据，这里不重要，为了让例子完整，还是写上吧  
	        }  
	      
	        @Override  
	        public long getChildId(int groupPosition, int childPosition) {  
	            return childPosition; //  
	        }  
	      
	        @Override  
	        public int getChildrenCount(int groupPosition) {  
	            return children[groupPosition].length; //  
	        }  
	      
	        @Override  
	        public View getChildView(int groupPosition, int childPosition,  boolean isLastChild, View convertView, ViewGroup parent) {  
	            //重点在这里  
	            LayoutInflater inflate=LayoutInflater.from(EventViewerListByTimeActivity.this);  
	            View view=inflate.inflate(R.layout.event_list_child, null); //用childlayout这个layout作为条目的视图  
	              	              
	            TextView name=(TextView)view.findViewById(R.id.EventTitle); //childlayout有一个用于显示名字的视图  
	            name.setText(children[groupPosition][childPosition]); //给这个视图数据  
	              
	            TextView description=(TextView)view.findViewById(R.id.EventInfo); //childlayout有一个用于显示描述的视图，在name视图的下面，  
	            description.setTextKeepState("description");  //这里只是简单的把它的数据设为description  
	              
	            ImageView mycursor=(ImageView)view.findViewById(R.id.myCursor);//childlayout还有一个小图标，在右侧，你可以给它一个单击事件，以弹出对当前条目的菜单。  
	              
	            return view;  
	        }  
	      
	        @Override  
	        public Object getGroup(int groupPosition) {  
	            return groups[groupPosition];  
	        }  
	      
	        @Override  
	        public int getGroupCount() {  
	            return groups.length;  
	        }  
	      
	        @Override  
	        public long getGroupId(int groupPosition) {  
	            return groupPosition;  
	        }  
	      
	        //父列表中的某一项的View  
	        @Override  
	        public View getGroupView(int groupPosition, boolean isExpanded,  View convertView, ViewGroup parent) {  
	            //这里的处理方法和getChildView()里的类似，不再重复说了  
	              
	            LayoutInflater inflate=LayoutInflater.from(EventViewerListByTimeActivity.this);  
	            View view=inflate.inflate(R.layout.event_list_group, null);  //用grouplayout这个layout作为条目的视图  
	              
	            TextView groupName=(TextView)view.findViewById(R.id.GroupTitle);  
	            String group="test group";  
	            groupName.setText(group);  
	              
	            TextView groupCount=(TextView)view.findViewById(R.id.EventCount);  
	            groupCount.setText("["+children[groupPosition].length+"]");  
	              
	            return view;  
	        }  
	      
	        @Override  
	        public boolean hasStableIds() {  
	            return true;  
	        }  
	      
	        @Override  
	        public boolean isChildSelectable(int groupPosition, int childPosition) {  
	            return true;  
	        }  
	          
	    }  
}
