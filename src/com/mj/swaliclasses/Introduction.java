package com.mj.swaliclasses;

import com.mj.teqv2.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Introduction extends Fragment{
	public static final String POINT_NUM = "point_num";
	int point_id;
	private String qn_body;
	TextView ph, pb, pn;

	public Introduction(String qn_body2) {
		this.qn_body = qn_body2;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(R.layout.s_introduction_fragment, container, false);


		pn = (TextView) rootView.findViewById(R.id.middle);
		pn.setText(qn_body);

		return rootView;
	}

}
