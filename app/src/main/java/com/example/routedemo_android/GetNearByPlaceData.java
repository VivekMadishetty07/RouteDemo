package com.example.routedemo_android;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearByPlaceData extends AsyncTask<Object, String, String> {
    GoogleMap googleMap;
    String placeData;
    String url;


    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        FetchUrl fetchUrl = new FetchUrl();
        try{
            placeData = fetchUrl.readUrl( url );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return placeData;
    }


    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearByPlaceList = null;
        DataParser parser = new DataParser();
        nearByPlaceList = parser.parse( s );
        showNearByPlaces( nearByPlaceList );
    }


    private void  showNearByPlaces(List<HashMap<String, String>> nearbyList){
        for(int i = 0; i <nearbyList.size();i++)
        {
            // Log.i("MainActivity", String.valueOf( nearbyList.size()));
            HashMap<String, String> place = nearbyList.get( i );

            String placeName = place.get( "placeName" );
            String vicinity = place.get( "vicinity" );
            double lat = Double.parseDouble( place.get( "lat" ) );
            double lng = Double.parseDouble( place.get( "lng" ) );
            String reference = place.get( "reference" );

            LatLng location = new LatLng( lat, lng );
            MarkerOptions marker = new MarkerOptions().position( location )
                    .title( placeName )

                    .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) );
            googleMap.addMarker( marker );

            CameraPosition cameraPosition = CameraPosition.builder()
                    .target( location )
                    .zoom( 15 )
                    .bearing( 0 )
                    .tilt( 45 )
                    .build();
            googleMap.animateCamera( CameraUpdateFactory.newCameraPosition( cameraPosition ) );


        }
    }
}
