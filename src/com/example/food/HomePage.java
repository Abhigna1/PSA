package com.example.food;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.speech.tts.UtteranceProgressListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HomePage extends Activity {

	private TextView textResult1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		
		textResult1=(TextView)findViewById(R.id.txt_home);
		
		 Intent intent = getIntent();
		 final String pId  = intent.getStringExtra("p_id");
		 textResult1.setText(pId);
		 
		 
		 Button sugar=(Button)findViewById(R.id.btn_Sugar);
		 sugar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intr5=new Intent(HomePage.this,SugarLevel.class);
				Bundle bund = new Bundle();
				intr5.putExtra("p_id",pId);	
				startActivity(intr5);
			}
		});
		 
		 Button fat=(Button)findViewById(R.id.btn_Fat);
		 fat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intr6=new Intent(HomePage.this,FatLevel.class);
				Bundle bund = new Bundle();
				intr6.putExtra("p_id",pId);	
				startActivity(intr6);
			}
		});
		 
		 Button carbo=(Button)findViewById(R.id.btn_Carbo);
		 carbo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intr7=new Intent(HomePage.this,CarboLevel.class);
				Bundle bund = new Bundle();
				intr7.putExtra("p_id",pId);	
				startActivity(intr7);
			}
		});
		 Button back=(Button)findViewById(R.id.btn_Back);
		 back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intr8=new Intent(HomePage.this,MainActivity.class);
				startActivity(intr8);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

}
