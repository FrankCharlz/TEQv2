package com.mj.teqv2;


import java.util.ArrayList;
import java.util.HashMap;

import com.mj.swaliclasses.Introduction;
import com.mj.swaliclasses.Points;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SwaliFragmentAdapter extends FragmentPagerAdapter {
	private String qn_body;
	ArrayList<HashMap<String, String>> pointsArray;
	private Activity activity;

	public SwaliFragmentAdapter(FragmentManager fm, String _qn_body, ArrayList<HashMap<String, String>> pointsArray_, 
			Activity activity) {
		super(fm);
		this.qn_body = _qn_body; //parsed from the Swali.java class.. to  be parsed to Introduction..
		this.pointsArray = pointsArray_;
		this.activity = activity;
	}

	@Override
	public Fragment getItem(int index) {
		Fragment fragment = null;
		switch (index) {
		case 0: fragment = new Introduction(qn_body);
		break;

		case 1: fragment = new Points(activity, pointsArray);
		break;

		case 2: fragment = new Points(activity, pointsArray);
		break;

		case 3: fragment = new Points(activity, pointsArray);
		break;

		default:break;
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return 4;
	}


}
