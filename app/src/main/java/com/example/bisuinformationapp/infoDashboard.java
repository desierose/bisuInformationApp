package com.example.bisuinformationapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class infoDashboard extends AppCompatActivity {

    FirebaseDatabase admin = FirebaseDatabase.getInstance();
    DatabaseReference myRef= admin.getReference("events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dashboard);

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
                    if (!snapshot.getValue().equals("")) { // Check if the value is not a string
                        admin events = snapshot.getValue(admin.class);
                        dataList.add(events);
                    }
                }
                // Set up the RecyclerView adapter with the retrieved data
                DataAdapter adapter = new DataAdapter(dataList);
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
            admin data = dataList.get(position);
            // Bind the data to the views in the ViewHolder
            holder.textView1.setText(data.getEventName());
            holder.textView2.setText(data.getDate());
            holder.textView3.setText(data.getLocation());
            holder.textView4.setText(data.getDescription());

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView1;
            public TextView textView2;
            public TextView textView3;
            public TextView textView4;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView1 = itemView.findViewById(R.id.textView1);
                textView2 = itemView.findViewById(R.id.textView2);
                textView3 = itemView.findViewById(R.id.textView3);
                textView4 = itemView.findViewById(R.id.textView4);

            }
        }
    }
}