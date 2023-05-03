package com.example.jasus.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jasus.Adapter.CallAdapter;
import com.example.jasus.Adapter.SmsAdapter;
import com.example.jasus.LocalData;
import com.example.jasus.Model.CallModel;
import com.example.jasus.Model.SmsModel;
import com.example.jasus.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SmsFragment extends Fragment {

    RecyclerView recyclerView;
    SmsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sms, container, false);

        recyclerView = view.findViewById(R.id.sms_recycler);

        String deviceId = LocalData.getDeviceID(getContext());

        if (!deviceId.isEmpty()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId).child("Sms");


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int size = (int) snapshot.getChildrenCount();

                    TextView calls = view.findViewById(R.id.total_sms);
                    String s = String.valueOf(size);
                    calls.setText("(" + s + ")");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            FirebaseRecyclerOptions<SmsModel> options
                    = new FirebaseRecyclerOptions.Builder<SmsModel>()
                    .setQuery(reference, SmsModel.class)
                    .build();

            adapter = new SmsAdapter(options);
            recyclerView.setAdapter(adapter);
        }



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}