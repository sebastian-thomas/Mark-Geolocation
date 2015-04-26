package housingland.seb.com.housingland;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements GoogleMap.OnMarkerClickListener {

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
        map.setOnMarkerClickListener(this);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("Marker --",marker.getTitle());
        Toast toast = Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(),"htemp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, 1);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("htemp.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);

                //viewImage.setImageBitmap(bitmap);

                String path = android.os.Environment
                        .getExternalStorageDirectory()
                        + File.separator
                        + "Phoenix" + File.separator + "default";
                f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Log.e("Camera","Error");
        }
    }
}
