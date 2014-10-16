package com.mj.teqv2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mj.teqv2.R;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Swali extends ActionBarActivity {
	SwaliFragmentAdapter sfa;
	ViewPager myViewPager;
	TextView swalibody;
	public int _qn_num;
	private String _qn_body;
	ArrayList<HashMap<String, String>> pointsArray;


	protected void onCreate(Bundle savedShits) {
		super.onCreate(savedShits);
		setContentView(R.layout.swali_layout);

		Intent fromMain = getIntent();
		_qn_num = fromMain.getIntExtra("QN_NUM", 0); //checking its c qn no. default=0..
		_qn_body = fromMain.getStringExtra("QN_BODY");
		
		//....prepare points.....//
		Cursor cursor = prepareDatabase();
		pointsArray = new ArrayList<HashMap<String, String>>();
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			HashMap<String, String> onePoint = new HashMap<String, String>();
			onePoint.put("POINT_HEAD", cursor.getString(cursor.getColumnIndex("point")));
			onePoint.put("POINT_BODY", cursor.getString(cursor.getColumnIndex("maelezo")));
			pointsArray.add(onePoint);
			
		}

		myViewPager = (ViewPager) findViewById(R.id.swali_pager);
		sfa = new SwaliFragmentAdapter(getSupportFragmentManager(), _qn_body, pointsArray, Swali.this);
		myViewPager.setAdapter(sfa);

		final ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setBackgroundDrawable(new ColorDrawable(0xff6669cf));
		bar.setStackedBackgroundDrawable(new ColorDrawable(0x076669ff));
		bar.setIcon(new ColorDrawable(0x00ffffff));
		bar.setTitle("Question "+_qn_num);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				myViewPager.setCurrentItem(tab.getPosition());

			}

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}


		};

		// Add 3 tabs, specifying the tab's text and TabListener
		bar.addTab(
				bar.newTab()
				.setText("Introduction")
				.setTabListener(tabListener));

		bar.addTab(
				bar.newTab()
				.setText("Points")
				.setTabListener(tabListener));

		bar.addTab(
				bar.newTab()
				.setText("Conclusion")
				.setTabListener(tabListener));

		bar.addTab(
				bar.newTab()
				.setText("Summary")
				.setTabListener(tabListener));


		//added tabs....***********************************************************\/////////

		myViewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						bar.setSelectedNavigationItem(position);
					}
				});


	}


	private Cursor prepareDatabase() {
		DatabaseHandler dh = new DatabaseHandler(Swali.this);
		try {
			dh.createDataBase();
		} catch (IOException ioe) { 
			throw new Error("Unable to create database");
			}

		try { dh.openDataBase(); } catch(SQLException sqle)   { throw sqle;}

		Log.i("Happy", "Finaaaly the database is usable...");

		Cursor cursor = dh.query("points_qn_"+_qn_num);
		return cursor;

	}

}
