package com.mj.teqv2;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.mj.teqv2.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	static final String KEY_Q_NUM = "number";
	static final String KEY_Q_DETAIL = "question";
	static final String KEY_POINT = "point";

	ListView list;
	ListAdapter adapter;
	Typeface ubuntu, gothic;
	TextView title;
	ArrayList<HashMap<String, String>> qnArrayList;
	
	SharedPreferences userData;
	private long device_id;
	private boolean  unlocked;
	public Cursor maswali;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		firstLaunchOnly();
		device_id = userData.getLong("DEVICE_ID", 0); //getting device_id..
		
		Toast.makeText(getApplicationContext(), "ID is "+device_id, Toast.LENGTH_SHORT).show();
		
		android.support.v7.app.ActionBar bar = getSupportActionBar();
		bar.hide();
		
		title = (TextView) findViewById(R.id.title);
		gothic = Typeface.createFromAsset(getAssets(), "fonts/gothic.ttf");
		ubuntu = Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf");
		
		setTaito();
		prepareDatabase();//preparing the database...
		
		//populate qnArrayList...
		qnArrayList = new ArrayList<HashMap<String, String>>();
		
		int i = 0;
		for (maswali.moveToFirst(); !maswali.isAfterLast(); maswali.moveToNext()) {
			HashMap<String, String> oneQn = new HashMap<String, String>();
			oneQn.put(KEY_Q_DETAIL, maswali.getString(maswali.getColumnIndex("qn")));
			qnArrayList.add(oneQn);
		}
		
		//initialize adapter and link to the list..
		adapter = new ListAdapter();
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		
		//setting list click listener...
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				unlocked = userData.getBoolean("UNLOCKED", true);
				if (pos>2 && !unlocked) {
					//sends user to Kulipia.. parse DEVICE_ID
					Intent intent = new Intent("android.intent.action.LIPIA");
					intent.putExtra("DEVICE_ID", device_id);
					startActivity(intent);
				}
				else {
					//sends user to qns...
					Intent intent = new Intent("android.intent.action.SWALI");
					intent.putExtra("QN_NUM", (pos+1));
					intent.putExtra("QN_BODY", qnArrayList.get(pos).get(KEY_Q_DETAIL));
					startActivity(intent);
				}
			}
		});
		
		
	}
	
	private void firstLaunchOnly() {
		userData = getSharedPreferences("USER_DATA", getApplicationContext().MODE_PRIVATE);
		
		if (!userData.contains("DEVICE_ID")) {
		long init_device_id = generateSixFiguresLong();
		userData.edit().putLong("DEVICE_ID", init_device_id).apply();
		userData.edit().putBoolean("UNLOCKED", false).apply();
		}
		
		
		
	}

	private long generateSixFiguresLong() {
		Random rn = new Random();
		int a = 10 + rn.nextInt(86); // 10 to 96
		int b = 1 + rn.nextInt(96); // 1 to 96
		int c = 1 + rn.nextInt(96); // 1 to 96
		
		return 10000*a + 100*b + c;
	}

	public void setTaito() {
		String taito = "TEQII\n Making Learning Easier";
		SpannableString taito2 = new SpannableString(taito);
		taito2.setSpan(new RelativeSizeSpan(1.5f), 0,3, 0);
		taito2.setSpan(new SubscriptSpan(), 3, 5,0);
		title.setText(taito2);
		title.setTypeface(ubuntu);
	}
	
	private void prepareDatabase() {
		DatabaseHandler dh = new DatabaseHandler(MainActivity.this);
		try {
			dh.createDataBase();
		} catch (IOException ioe) { throw new Error("Unable to create database"); }

		try { dh.openDataBase(); } catch(SQLException sqle)   { throw sqle;}

		Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
		Log.e("happy", "Final dtabase in main..");

		maswali = dh.query("maswali");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class ListAdapter extends BaseAdapter {
		//class deals with the listView populating it.....and everything else./.
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

		@Override
		public int getCount() {
			return qnArrayList.size();
		}

		@Override
		public Object getItem(int pos) {
			return pos;
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View v, ViewGroup parent) {
			View view = v;
			if (view == null) {view =  inflater.inflate(R.layout.main_list_item, null); }
			
			TextView q_num   =  (TextView)view.findViewById(R.id.q_num); // namba
	        TextView q_detail = (TextView)view.findViewById(R.id.q_detail); //swali content
	        
	        HashMap<String, String> swali = new HashMap<String, String>();
	        swali = qnArrayList.get(pos);
	        String toDisp = swali.get(KEY_Q_DETAIL).toString(); 
	       
	        if (toDisp.length()>93) {toDisp = toDisp.substring(0, 80)+"...";} //punguza sentes ndefu
	        
	        q_num.setText(""+(pos+1));
	        q_num.setTypeface(ubuntu);
	        
	        q_detail.setText(toDisp);
	        q_detail.setTypeface(ubuntu); 
			
			return view;
			
		}
		
		
	}
}
