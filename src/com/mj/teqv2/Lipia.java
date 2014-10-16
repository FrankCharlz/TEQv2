package com.mj.teqv2;



import com.mj.teqv2.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Lipia extends ActionBarActivity{
	TextView ins;
	EditText codeField;
	Button activateBtn;
	MainActivity mainActivity;
	Button buyBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activation);

		Intent fromMain = getIntent();
		final long device_id = fromMain.getLongExtra("DEVICE_ID", 999999);

		codeField = (EditText) findViewById(R.id.activationCode);
		activateBtn = (Button) findViewById(R.id.activate) ;
		buyBtn = (Button) findViewById(R.id.buy) ;
		ins = (TextView) findViewById(R.id.activate_instruct);
		final android.support.v7.app.ActionBar bar = getSupportActionBar();
		
		bar.setBackgroundDrawable(new ColorDrawable(0xff3a3cf4));
		bar.setTitle("Activation");
		bar.setHomeButtonEnabled(true);
		bar.setDisplayUseLogoEnabled(false);
		bar.setDisplayShowHomeEnabled(true);
		bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		

		ins.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf"));
		codeField.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf"));
		activateBtn.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf"));
		
		String maelezo = getString(R.string.activation_instruction);
		ins.setText("Your device ID is "+device_id+".\n"+maelezo);

		activateBtn.setOnClickListener(new View.OnClickListener() {
			//for validation of the codes....
			@Override
			public void onClick(View v) {
				String theCode = codeField.getText().toString();
				if (theCode.length() == 6) {
					validate(theCode);
				} else {ins.setText("Acha uizi.. Provide proper codes (must have six figures)."); }


			}

			private void validate(String theCode) {
				int x = Integer.parseInt(theCode.substring(0, 2));
				int y = Integer.parseInt(theCode.substring(2, 4));
				int z = Integer.parseInt(theCode.substring(4));

				int dx = (20*x + 6*y + 19*z) % 97 ; 
				int dy = (93*x + 20*y + 6*z) % 97 ;
				int dz = (19*x + 93*y + 2*z) % 97 ;

				String nstr="", temp= null;
				int[] dxyz = {dx,dy,dz};
				for (int item : dxyz) {
					if (item<10) {temp = "0"+item; } else {temp = ""+item;}
					nstr = nstr + temp;

				}


				if ((Long.parseLong(nstr) - device_id)==0) {
					//if codes match...
					ins.setText("Congrats...!\nYour app was unlocked successfully.");
					
					SharedPreferences userData = getSharedPreferences("USER_DATA", getApplicationContext().MODE_PRIVATE);
					userData.edit().remove("UNLOCKED").apply();
					userData.edit().putBoolean("UNLOCKED", true).apply();
					
					activateBtn.setVisibility(View.GONE);
					codeField.setVisibility(View.GONE);
					
					ins.setBackgroundColor(Color.parseColor("#3000ff00"));
					ins.setPadding(44,44,44,44);
					ins.setTextSize(24f);
					ins.setGravity(Gravity.CENTER);
					
					buyBtn.setText("CONTINUE");
					buyBtn.setTextColor(Color.BLACK);
					buyBtn.setBackgroundColor(Color.parseColor("#d060ff60"));
					bar.setBackgroundDrawable(new ColorDrawable(0xd960ff60));
					
				} 
				else { ins.setText("You entered wrong codes. F**k u..! Try again. "+
						"\nDevice: "+device_id+" \nCode: "+theCode+
						"\nTry again"); }

			}


		});


	}

}
