package com.example.food;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SugarLevel extends Activity {

	
	private TextView textResult;
	private EditText editSugarLevel;
	private EditText editTempProduct;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sugar_level);
		
		editTempProduct=(EditText)findViewById(R.id.edit_SLProductID);
		textResult=(TextView)findViewById(R.id.text_SLResult);
		editSugarLevel=(EditText)findViewById(R.id.edit_SLSugarLevel);
		
		 Intent intent = getIntent();
		 final String pId  = intent.getStringExtra("p_id");
		 editTempProduct.setText(pId);
		 
		 Button SLBack=(Button)findViewById(R.id.btn_SLBack);
		 SLBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent inte5=new Intent(SugarLevel.this,HomePage.class);
				Bundle bund = new Bundle();
				inte5.putExtra("p_id",pId);	
				startActivity(inte5);
			}
		});

	}
	
	public void Check(View view)
	{
		String ProductID=editTempProduct.getText().toString();
		String SugarLevel=editSugarLevel.getText().toString();
		check(ProductID,SugarLevel);
	}
	
	private void check(final String ProductID, String SugarLevel) {
		 
        class checkAsync extends AsyncTask<String, Void, String>{
 
            private Dialog loadingDialog;
 
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(SugarLevel.this, "Please wait", "Loading...");
            }
 
            @Override
            protected String doInBackground(String... params) {
                String productID = params[0];
                String sugarLevel = params[1];
 
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("p_id", productID));
                nameValuePairs.add(new BasicNameValuePair("sugar", sugarLevel));
                String result = null;
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                    		"http://www.hemalatha.890m.com/food/check.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();
                if(s.equalsIgnoreCase("1")){
                    textResult.setText("Sugar Level is Low this Product..Eat this Food..");
                }else {
                	textResult.setText("Sugar Level is High this Product..Dont Eat this Food..");
                }
            }
        }
 
        checkAsync ca = new checkAsync();
        ca.execute(ProductID, SugarLevel);
 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sugar_level, menu);
		return true;
	}

}
