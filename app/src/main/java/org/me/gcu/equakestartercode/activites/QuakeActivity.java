package org.me.gcu.equakestartercode.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.equakestartercode.R;
import org.me.gcu.equakestartercode.models.Item;

public class QuakeActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private TextView locationText;
    private TextView dateText;
    private TextView latText;
    private TextView longText;
    private TextView depthText;
    private TextView magText;
    private TextView catagoryText;
    private TextView linkText;
    private Item selectedItem;
    private  MapView mapView;
    private LinearLayout infoLinear;
    private TextView errortext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quake_more);
        getSupportActionBar().setTitle("Quake Info");

        try {
            selectedItem = (Item)getIntent().getExtras().getSerializable("Item") ;
        }
        catch (Exception e) {
            selectedItem = null;
        }


        locationText = (TextView)findViewById(R.id.locationText);
        dateText = (TextView)findViewById(R.id.dateText);
        latText = (TextView)findViewById(R.id.latText);
        longText = (TextView)findViewById(R.id.longText);
        depthText = (TextView)findViewById(R.id.depthText);
        magText = (TextView)findViewById(R.id.magText);
        catagoryText = (TextView)findViewById(R.id.catagoryText);
        linkText = (TextView)findViewById(R.id.linkText);
        infoLinear = (LinearLayout)findViewById(R.id.infoLinear);
        errortext = (TextView)findViewById(R.id.errorText);

        if(selectedItem != null) {
            longText.setText(selectedItem.getGeoLong());
            magText.setText(selectedItem.getMagnitude());
            depthText.setText(selectedItem.getDepth());
            catagoryText.setText(selectedItem.getCategory());
            linkText.setText(selectedItem.getLink());
            locationText.setText(selectedItem.getLocation());
            latText.setText(selectedItem.getGeoLat());
            dateText.setText(selectedItem.getPubDate());
        }else
        {
            infoLinear.setVisibility(View.INVISIBLE);
            mapView.setVisibility(View.INVISIBLE);
            errortext.setVisibility(View.VISIBLE);
            locationText.setText("ERROR");
        }



        mapView = findViewById(R.id.itemLocation);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }
    //Draws map
    @Override
    public void onMapReady(GoogleMap googleMap){
        LatLng latLng = new LatLng(Float.parseFloat(selectedItem.getGeoLat()) , Float.parseFloat(selectedItem.getGeoLong()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        addMarker(selectedItem , googleMap);
    }
    //Adds marker to map
    //Cause this is reused function wanted to break out into own thing
    //But didn't know how to effectively
    private void addMarker(Item item , GoogleMap googleMap) {
        if (Float.parseFloat(item.getMagnitude()) < 1) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(item.getLocation()));
        } else if (Float.parseFloat(item.getMagnitude()) >= 1 && Float.parseFloat(item.getMagnitude()) < 3) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(item.getLocation()));
        } else if (Float.parseFloat(item.getMagnitude()) >= 3) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getGeoLat()) , Double.parseDouble(item.getGeoLong()))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(item.getLocation()));
        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    //Save map state on reorientated
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

