package com.example.food;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "p_id";
    private static final String TAG_NAME = "p_name";
    private static final String TAG_ENERGY = "energy";
    private static final String TAG_PROTEIN ="protein";
    private static final String TAG_FAT = "fat";
    private static final String TAG_CARBOHYDRATE = "carbohydrate";
    private static final String TAG_SUGAR ="sugar";
    private static final String TAG_DESCRIPTION ="description";
   //private EditText number;
    JSONArray products = null;
 
    ArrayList<HashMap<String, String>> productList;
	private TextView productId;
	private TextView productName;
	private TextView productEnergy;
	private TextView productProtein;
	private TextView productFat;
	private TextView productCarbohydrate;
	private TextView productSugar;
	private TextView Result;
	String myJSON;
	String pId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		HandleClick hc = new HandleClick();
	    findViewById(R.id.btn_barcodescan).setOnClickListener(hc);
	    productId=(TextView)findViewById(R.id.txt_barcode);
	    productName=(TextView)findViewById(R.id.txt_name);
	    productEnergy=(TextView)findViewById(R.id.txt_energy);
	    productProtein=(TextView)findViewById(R.id.txt_protein);
	    productFat=(TextView)findViewById(R.id.txt_fat);
	    productCarbohydrate=(TextView)findViewById(R.id.txt_carbohy);
	    productSugar=(TextView)findViewById(R.id.txt_sugar);
	    Result=(TextView)findViewById(R.id.txt_result);
	    
	    Button chec=(Button)findViewById(R.id.btn_MCheck);
	    chec.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intr4=new Intent(MainActivity.this,HomePage.class);
				Bundle bund = new Bundle();
				intr4.putExtra("p_id",pId);	
				startActivity(intr4);
			}
		});
	    
	    
	    //number=(EditText)findViewById(R.id.editText1);
	}

	private class HandleClick implements OnClickListener{
	    public void onClick(View arg0) {
	      Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	      switch(arg0.getId()){
	        case R.id.btn_barcodescan:
	          intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
	        break;        
	       
	      }
	      startActivityForResult(intent, 0);    //Barcode Scanner to scan for us
	    }
	  }
	  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	     // TextView tvStatus=(TextView)findViewById(R.id.tvStatus);
	      
	      if (resultCode == RESULT_OK) {
	        //tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
	        productId.setText(intent.getStringExtra("SCAN_RESULT"));
	      } else if (resultCode == RESULT_CANCELED) {
	       // tvStatus.setText("Press a button to start a scan.");
	        productId.setText("Scan cancelled.");
	      }
	    }
	  }
	 
	  public void Search(View view)
		{
			//String barc=number.getText().toString();
			String barc=productId.getText().toString();
			Search(barc);
		}
	  
	  
	  private void Search(final String barcode) {
			
	        class SearchAsync extends AsyncTask<String, Void, String>{
		            private Dialog loadingDialog;
	 
	            @Override
	            protected void onPreExecute() {
	                super.onPreExecute();
	                loadingDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...");
	            }
	 
	            @Override
	            protected String doInBackground(String... params) {
	                String barcode1 = params[0];
	                
	                String barc=productId.getText().toString();
	                InputStream is = null;
	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	                nameValuePairs.add(new BasicNameValuePair("product_id", barcode1));
	                String result = null;
	                try
	                {
	                    HttpClient httpClient = new DefaultHttpClient();
	                    HttpPost httpPost = new HttpPost(
	                            "https://github.com/Abhigna1/PSA.git");
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
	                myJSON=result;
	                
	                if(s.equalsIgnoreCase("fail"))
	                {
	                	
	                	//Result.setText("Bad");
	                	Toast.makeText(getApplicationContext(), "Bad", Toast.LENGTH_LONG).show();
	            	}
	                 
	             else 
	             {
	            	 showList();
	            	// Result.setText("Good");
	            	 Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_LONG).show();
	             }
	            }
	        }
	 
	        SearchAsync la = new SearchAsync();
	        la.execute(barcode);
	     }
	  
	  
	  protected void showList(){
	        try {
	            JSONObject jsonObj = new JSONObject(myJSON);
	            products = jsonObj.getJSONArray(TAG_RESULTS);
	            for(int i=0;i<products.length();i++){
	                JSONObject c = products.getJSONObject(i);
	                pId = c.getString(TAG_ID);
	                String pName = c.getString(TAG_NAME);
	                String pEnergy = c.getString(TAG_ENERGY);
	                String pProtein = c.getString(TAG_PROTEIN);
	                String pFat = c.getString(TAG_FAT);
	                String pCarbohydrate= c.getString(TAG_CARBOHYDRATE);
	                String pSugar = c.getString(TAG_SUGAR);
	                String pDescription = c.getString(TAG_DESCRIPTION);
	                
	                
	                productId.setText(pId);
	                productName.setText(pName);
	                productEnergy.setText(pEnergy);
	                productProtein.setText(pProtein);
	                productFat.setText(pFat);
	                productCarbohydrate.setText(pCarbohydrate);
	                productSugar.setText(pSugar);
	                Result.setText(pDescription);
	                               
	 
	                /*Intent inte8=new Intent(MainActivity.this,SugarLevel.class);
	                //Bundle bund = new Bundle();

	                inte8.putExtra("p_id",pId);	              
	                startActivity(inte8);
	               
	                }*/
	                /*HashMap<String,String> products1 = new HashMap<String,String>();
	                
	                products1.put(TAG_ID,pId);
	                products1.put(TAG_NAME,pName);
	                products1.put(TAG_PRICE,pEnergy);
	                products1.put(TAG_DESCRIPTION,pProtein);
	                
	 
	                productList.add(products1);*/
	            
	            } 
	 
	        }
	        catch (JSONException e) {
	            e.printStackTrace();
	        }
	 
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
