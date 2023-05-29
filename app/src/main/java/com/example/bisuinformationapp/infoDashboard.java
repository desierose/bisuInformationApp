package com.example.bisuinformationapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class infoDashboard extends AppCompatActivity {

    FirebaseDatabase admin = FirebaseDatabase.getInstance();
    DatabaseReference myRef = admin.getReference("events");
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dashboard);
        storageRef = FirebaseStorage.getInstance().getReference();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set an empty adapter initially
        DataAdapter adapter = new DataAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Retrieve the data from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<admin> dataList = new ArrayList<>();
                // Iterate over the children of the dataSnapshot and populate the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String eventID = snapshot.child("eventID").getValue(String.class);
                    String eventName = snapshot.child("eventName").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String fileId = snapshot.child("document/files/").getValue(String.class);

                    admin event = new admin(eventID, eventName, date, location, description,fileId);
                    dataList.add(event);
                }
                // Set up the RecyclerView adapter with the retrieved data
                adapter.setData(dataList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during data retrieval
                Log.e(TAG, "Failed to retrieve data. " + databaseError.getMessage());
            }
        });
    }

    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
        private List<admin> dataList;

        public DataAdapter(List<admin> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_data, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            admin event = dataList.get(position);
            // Bind the data to the views in the ViewHolder
            holder.textView1.setText(event.getEventName());
            holder.textView2.setText(event.getDate());
            holder.textView3.setText(event.getLocation());
            holder.textView4.setText(event.getDescription());

            // Retrieve and load the image using Glide
            String fileId = event.getFileId();
            if (fileId != null) {
                StorageReference fileRef = storageRef.child(fileId);

                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.bisu) // Placeholder image while loading
                        .error(R.drawable.bisu) // Image to display on error
                        .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache image

                Glide.with(holder.itemView.getContext())
                        .load(fileRef)
                        .apply(requestOptions)
                        .into(holder.imageView);
            } else {
                // If no file ID is available, you can set a default image
                holder.imageView.setImageResource(R.drawable.bisu);
            }
        }
            @Override
        public int getItemCount() {
            return dataList.size();
        }

        public void setData(List<admin> newDataList) {
            dataList = newDataList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView1;
            public TextView textView2;
            public TextView textView3;
            public TextView textView4;
            public ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView1 = itemView.findViewById(R.id.textView1);
                textView2 = itemView.findViewById(R.id.textView2);
                textView3 = itemView.findViewById(R.id.textView3);
                textView4 = itemView.findViewById(R.id.textView4);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}