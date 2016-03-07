package in.learner.android.letuslearn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {


    final String loginurl="http://192.168.1.100/loginl.php";

    static String token="";
    static String username="";
    static String mail="";
    static String phone="";
    static String gender="";
    CircularProgressButton circularButton1;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         gps=new GPSTracker(MainActivity.this);
        final EditText Uname=(EditText) findViewById(R.id.Uname);
        final EditText Pword=(EditText) findViewById(R.id.Pword);
        TextView register=(TextView) findViewById(R.id.register);


        Pword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    circularButton1.performClick();
                    handled = true;
                }
                return handled;
            }
        });


        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String islogged=pref.getString("isloggedin",null);
        if(islogged!=null && islogged.equals("true")){
            token=pref.getString("Token",null);
            username=pref.getString("User",null);
            mail=pref.getString("mail",null);
            phone=pref.getString("phone",null);
            gender=pref.getString("gender",null);
            Intent i=new Intent(MainActivity.this,MapsActivity.class);
            startActivity(i);

        }


        register.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,Register.class);
                startActivity(i);


            }
        });






        circularButton1 = (CircularProgressButton) findViewById(R.id.circularButton1);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                circularButton1.setProgress(50);

                String user=Uname.getText().toString();
                String pass=Pword.getText().toString();

                double latitude=0,longitude=0;

                if(gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }else{

                        gps.showSettingsAlert();

                }


                final ConnectionDetector cd = new ConnectionDetector(MainActivity.this);
                if(cd.isConnectingToInternet()){


                    JSONObject jsonobject;
                    final JSONParser jParser1 = new JSONParser();
                    List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                    params1.add(new BasicNameValuePair("password",pass));
                    params1.add(new BasicNameValuePair("uname", user));
                    params1.add(new BasicNameValuePair("lat", "" +latitude));
                    params1.add(new BasicNameValuePair("long", "" + longitude));


                    jsonobject = jParser1.makeHttpRequest(loginurl, "POST", params1);

                    try{
                        if(jsonobject!=null){

                            String result=jsonobject.getString("success");
                            String message=jsonobject.getString("message");

                            if(result.equals("1")) {

                                    SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    token = jsonobject.getString("id");



                                    username = user;
                                    editor.putString("id", token);


                                    editor.putString("isloggedin", "true");
                                    editor.commit();
                                Intent i=new Intent(MainActivity.this,MapsActivity.class);
                                circularButton1.setProgress(100);
                                startActivity(i);





                                }
                            }

                            else{
                                Toast.makeText(MainActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                                circularButton1.setProgress(-1);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        circularButton1.setProgress(0);
                                        circularButton1.setIndeterminateProgressMode(true);


                                    }
                                }, 3000);

                            }

                        } catch (JSONException e) {
                        e.printStackTrace();
                    }









                }else{
                    circularButton1.setProgress(-1);
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            circularButton1.setProgress(0);
                            circularButton1.setIndeterminateProgressMode(true);

                        }
                    }, 3000);
                }








            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/













    }






