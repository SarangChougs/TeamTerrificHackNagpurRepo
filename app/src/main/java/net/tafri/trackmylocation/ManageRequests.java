package net.tafri.trackmylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageRequests extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RequestAdapter mAdapter;
    List<Request> mRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRequests = new ArrayList<>();
        mAdapter = new RequestAdapter(this, mRequests);
        mRecyclerView.setAdapter(mAdapter);
        getRequests();
    }

    public void getRequests() {
        FirebaseDatabase.getInstance().getReference("Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = true;
                mRequests.clear();
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    if (!requestSnapshot.child("status").getValue().toString().equals("Requested")) {
                        continue;
                    }
                    double userLatitude = Double.parseDouble(requestSnapshot.child("userCoordinates").child("latitude").getValue().toString());
                    double userLongitude = Double.parseDouble(requestSnapshot.child("userCoordinates").child("longitude").getValue().toString());
                    double distance = distance(userLatitude, userLongitude, GlobalClass.currentUserLatitude, GlobalClass.currentUserLongitude);
                    if (distance >= 10)
                        continue;
                    flag = false;
                    Request request = new Request();
                    request.setUsername(requestSnapshot.child("username").getValue().toString());
                    request.setUserId(requestSnapshot.child("userId").getValue().toString());
                    request.setUserMobile(requestSnapshot.child("userMobile").getValue().toString());
                    mRequests.add(request);
                }
                mAdapter.notifyDataSetChanged();
                if (flag)
                    Toast.makeText(ManageRequests.this, "No requests", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.CustomViewHolder> {
        private Context mContext;
        private List<Request> list;

        public RequestAdapter(Context context, List<Request> requests) {
            mContext = context;
            list = requests;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.request_info_item, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, final int position) {

            Request request = list.get(position);
            holder.Name.setText(request.getUsername());
            holder.UserDetails.setText(request.getUserMobile());
            holder.AcceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptRequest(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {

            TextView Name, UserDetails;
            ImageView AcceptBtn;

            public CustomViewHolder(View itemView) {
                super(itemView);
                Name = itemView.findViewById(R.id.name);
                UserDetails = itemView.findViewById(R.id.address);
                AcceptBtn = itemView.findViewById(R.id.Accept);
            }
        }
    }

    public void acceptRequest(int position) {
        //method to accept the request
        Request request = mRequests.get(position);
        HashMap<String, String> map = new HashMap<>();
        map.put("driveName", GlobalClass.user.getName());
        map.put("driverId", GlobalClass.user.getUid());
        map.put("driverMobileNo", GlobalClass.user.getMobileNo());

        GlobalClass.RequestedUserId = request.getUserId();

        FirebaseDatabase.getInstance().getReference("Requests/" + request.getUserId() + "/status").setValue("Accepted");
        FirebaseDatabase.getInstance().getReference("Requests/" + request.getUserId() + "/driverInfo").setValue(map);
        //Open Maps Activity
        startActivity(new Intent(getApplicationContext(), DriverMapsActivity.class));
        finish();
    }

    private double distance(double lat1, double long1, double lat2, double long2) {
        // Calculate longitude difference
        double longDiff;
        if (long1 > long2)
            longDiff = long1 - long2;
        else
            longDiff = long2 - long1;

        //Calculate distance
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(longDiff));

        dist = Math.acos(dist);

        //Convert distance rad to degree
        dist = rad2deg(dist);

        //distance in km
        dist = dist * 60 * 1.1515 * 1.6094344;

        return dist;

    }

    //convert degree to radian
    private static double deg2rad(double lat1) {
        return (lat1 * Math.PI / 180.0);
    }

    //convert degree to radian
    private static double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }
}