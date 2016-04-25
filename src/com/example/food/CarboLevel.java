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

public class CarboLevel extends Activity {
	
	private TextView textResult;
	private EditText editCarboLevel;
	private EditText editCarboProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carbo_level);
		
		editCarboProduct=(EditText)findViewById(R.id.edit_CLProductID);
		textResult=(TextView)findViewById(R.id.text_CLResult);
		editCarboLevel=(EditText)findViewById(R.id.edit_CLCarboLevel);
		
		 Intent intent = getIntent();
		 final String pId  = intent.getStringExtra("p_id");
		 editCarboProduct.setText(pId);
		 
		 Button CLBack=(Button)findViewById(R.id.btn_CLBack);
		 CLBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent inte7=new Intent(CarboLevel.this,HomePage.class);
				Bundle bund = new Bundle();
				inte7.putExtra("p_id",pId);
				startActivity(inte7);
			}
		});
	}

	public void CheckCarbo(View view)
	{
		String ProductID=editCarboProduct.getText().toString();
		String CarboLevel=editCarboLevel.getText().toString();
		checkCarbo(ProductID,CarboLevel);
	}
	
	private void checkCarbo(final String ProductID, String CarboLevel) {
		 
        class checkCarboAsync extends AsyncTask<String, Void, String>{
 
            private Dialog loadingDialog;
 
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(CarboLevel.this, "Please wait", "Loading...");
            }
 
            @Override
            protected String doInBackground(String... params) {
                String productID = params[0];
                String carboLevel = params[1];
 
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("p_id", productID));
                nameValuePairs.add(new BasicNameValuePair("carbohydrate", carboLevel));
                String result = null;
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                    		"http://www.hemalatha.890m.com/food/checkcarbo.php");
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
                    textResult.setText("Carbohydrate Level is Low this Product..Eat this Food..");
                }else {
                	textResult.setText("Carbohydrate Level is High this Product..Dont Eat this Food..");
                }
            }
        }
 
        checkCarboAsync ca2 = new checkCarboAsync();
        ca2.execute(ProductID, CarboLevel);
 
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carbo_level, menu);
		return true;
	}

}
