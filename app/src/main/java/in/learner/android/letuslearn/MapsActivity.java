package in.learner.android.letuslearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final String url="http://192.168.1.100/get.php";

   ArrayList<MarkerOptions> mo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mo=new ArrayList<MarkerOptions>();
        // Obtain the192.168.43.230 SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


      /*  mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lang))
                .title(bus.getText().toString())
                .snippet(time));
        LatLng b = new LatLng(lat, lang);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(b));*/

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new LoginUser().execute();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String temp=marker.getSnippet();
                String[] a=temp.split("\n");
                Intent i=new Intent(MapsActivity.this,details.class);
                i.putExtra("name",a[0]);
                i.putExtra("phone",a[1]);
                i.putExtra("mail",a[2]);
                startActivity(i);
                return false;
            }
        });
        // Add a marker in Sydney and move the camera

    }




    private class LoginUser extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;
        //EditText temp;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(MapsActivity.this);
            nDialog.setTitle("Logging in ...");
            nDialog.setMessage("Please Wait..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


            JSONObject jsonobject;
            final JSONParser jParser2 = new JSONParser();
            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            params2.add(new BasicNameValuePair("id",MainActivity.token));



            jsonobject = jParser2.makeHttpRequest(url, "GET", params2);

            try{

                String message = jsonobject.getString("success").toString();
                if( !(new String(message).equals("0"))){
		JSONArray l=jsonobject.getJSONArray("learn");
				JSONArray j=jsonobject.getJSONArray("teach");
		for(int i=0;i<l.length();i++){
		JSONObject temp=l.getJSONObject(i);
            Double lat=new Double(temp.getString("lat"));
            Double lang=new Double(temp.getString("long"));
            String title=temp.getString("topic");
            String info=temp.getString("uname") +"\n" +  temp.getString("mobile")+"\n" +temp.getString("email");
            LatLng ll=new LatLng(lat, lang);
            MarkerOptions markerOptions=new MarkerOptions().position(ll).title(title).snippet(info);
            mo.add(markerOptions);


        }
                    for(int i=0;i<j.length();i++){
                        JSONObject temp=j.getJSONObject(i);
                        Double lat=new Double(temp.getString("lat"));
                        Double lang=new Double(temp.getString("long"));
                        String title=temp.getString("topic");
                        String info=temp.getString("uname");
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lang))
                                .title(title)
                                .snippet(info));
                    }
		
                    



                    return true;
                }

            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                Iterator<MarkerOptions> iterator1 = mo.iterator();
                while(iterator1.hasNext()){
                    mMap.addMarker(iterator1.next());
                }
                GPSTracker g=new GPSTracker(MapsActivity.this);
                if(g.canGetLocation()) {
                    LatLng temp=new LatLng(g.getLatitude(),g.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
                }else{
                    g.showSettingsAlert();
                }
/*
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lang))
                        .title(bus.getText().toString())
                        .snippet(time));
                LatLng b = new LatLng(lat, lang);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(b));

*/



            }
            else{
                nDialog.dismiss();
                //  temp.setText("");
                Toast.makeText(MapsActivity.this, "No Data Found", Toast.LENGTH_LONG);

            }
        }
    }


}
