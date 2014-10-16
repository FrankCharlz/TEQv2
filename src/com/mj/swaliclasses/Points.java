package com.mj.swaliclasses;

import java.util.ArrayList;
import java.util.HashMap;

import com.mj.teqv2.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/*/Find a way to parse that activity particulary LAYOUT INFLATOR from within this activity 
 * to avoid this ugly wai I have used ..
 * to inflate the layout...
 * 
 * LISTPOINTADAPTER is the copy and paste from MainActivity...
 * 
 * */
public class Points extends Fragment{
	ArrayList<HashMap<String, String>> pointsArray;
	private Activity activity;
	public TextView p_body;
	public TextView p_num;
	public TextView p_head;

	public Points(Activity activity, ArrayList<HashMap<String, String>> pointsArray_) {
		this.pointsArray = pointsArray_;
		this.activity = activity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(R.layout.s_points_fragment, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.listPoints);
		final ListPointsAdapter adapter = new ListPointsAdapter();
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				//Toast.makeText(activity, "Item clicked: "+id+"", Toast.LENGTH_SHORT).show();
				//pointsArray.get(pos).remove("POINT_BODY");
				//adapter.notifyDataSetChanged();

			}

		});


		return rootView;
	}

	public class ListPointsAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return pointsArray.size();
		}

		@Override
		public Object getItem(int pos) {
			return pos;
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}


		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface ubuntu = Typeface.createFromAsset(activity.getAssets(), "fonts/ubuntu.ttf");

		@Override
		public View getView(int pos, View v, ViewGroup parent) {
			View view = v;
			if (view == null) {
				view =  inflater.inflate(R.layout.points_fragment_list_item, null);

			}

			p_num   =  (TextView)view.findViewById(R.id.point_num); // namba
			p_head   =  (TextView)view.findViewById(R.id.point_head); // namba
			p_body = (TextView)view.findViewById(R.id.point_body); //swali content



			HashMap<String, String> swali = new HashMap<String, String>();
			swali = pointsArray.get(pos);
			String _head = swali.get("POINT_HEAD").toString(); 
			String _body = swali.get("POINT_BODY").toString(); 


			p_num.setText(""+(pos+1));
			p_head.setText(_head); 
			p_body.setText(_body);

			p_num.setTypeface(ubuntu);
			p_head.setTypeface(ubuntu);

			return view;
		}

	}

}
