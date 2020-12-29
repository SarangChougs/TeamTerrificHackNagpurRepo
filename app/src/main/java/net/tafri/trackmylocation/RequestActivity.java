package net.tafri.trackmylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RequestActivity extends AppCompatActivity {

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker, dummyMarker;
    LocationListener locationListener;
    int flag = 1;
    Button requestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        requestBtn = findViewById(R.id.request);
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBtn.setText("Requesting...");
                setRequest();
            }
        });
    }

    public void setRequest() {
        Request request = new Request("" + GlobalClass.user.getName(),
                "" + GlobalClass.user.getUid(),
                "" + GlobalClass.user.getMobileNo(),
                "",
                "",
                "",
                "Requested");
        FirebaseDatabase.getInstance().getReference("Requests/" + GlobalClass.user.getUid()).setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    requestBtn.setText("Requested");
                    updateLocation();
                } else {
                    System.out.println("Request registration failed");
                }
            }
        });
    }

    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(RequestActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (flag == 0)
                        return;
                    flag = 0;
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    HashMap<String, Double> map = new HashMap<>();
                    map.put("latitude", latitude);
                    map.put("longitude", longitude);
                    FirebaseDatabase.getInstance().getReference("Requests/" + GlobalClass.user.getUid() + "/userCoordinates")
                            .setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        setDelay(5000);
                                    } else {
                                        System.out.println("User Location Update Failed");
                                    }
                                }
                            });
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            ActivityCompat.requestPermissions(RequestActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        updateLocation();
    }

    public void attachListeners() {
        //method to check status of user request
    }

    public void setDelay(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                flag = 1;
            }
        }, delay);
    }
}