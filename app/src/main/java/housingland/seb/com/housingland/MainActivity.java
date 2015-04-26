package housingland.seb.com.housingland;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity  {

    private TextView latituteField;
    private TextView longitudeField;
    private GoogleMap map;
    private Polygon polygon;


    double latv = 0.0, longv=0.0;

    private LocationManager locationManager;
    private String provider;

    private ArrayList<LatLng> samplePoints;
    private ArrayList<LatLng> addedPoints;
    int sindex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        samplePoints = new ArrayList<LatLng>();
        samplePoints.add(new LatLng(12.971843,77.593560));
        samplePoints.add(new LatLng(12.970735,77.592380));
        samplePoints.add(new LatLng(12.968874,77.590513));
        samplePoints.add(new LatLng(12.968088,77.588945));
        samplePoints.add(new LatLng(12.972835,77.590211));
        samplePoints.add(new LatLng(12.973044,77.590618));
        samplePoints.add(new LatLng(12.974591,77.591090));


        //latituteField = (TextView) findViewById(R.id.latv);
        //longitudeField = (TextView) findViewById(R.id.longv);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if(latv == 0.0){
                    map.setMyLocationEnabled(true);
                    //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(samplePoints.get(0), 15));
                    Log.i("GeoLoc","Moving map");
                }
                latv = location.getLatitude();
                longv =location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //map.getUiSettings().setMyLocationButtonEnabled(true);


    }

    public void addLoc(View v){
        // create marker
        if(sindex <samplePoints.size()-1){
            sindex++;
            MarkerOptions marker = new MarkerOptions().position(samplePoints.get(sindex)).title("Boundary " + (sindex + 1));
            map.addMarker(marker);
        }
    }

    public void getLoc(View v){
       // latituteField.setText(String.valueOf(latv));
        //longitudeField.setText(String.valueOf(longv));

        PolygonOptions rectOptions = new PolygonOptions();
        for(LatLng l : samplePoints){
            rectOptions.add(l);
        }
        rectOptions.add(samplePoints.get(0));
        Polygon polygon = map.addPolygon(rectOptions);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
