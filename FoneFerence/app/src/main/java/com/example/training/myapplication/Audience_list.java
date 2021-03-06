package com.example.training.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Audience_list extends Activity {

    RecyclerView audiencerecycle;
    FirebaseDatabase firebaseDatabase;

   ArrayList<String> AudianceList;





    DatabaseReference audienceDbRef;
    ArrayList<HashMap<String, String>> AudianceListUpdated = new ArrayList<HashMap<String,String>>();
    DatabaseReference AudianceDetailsRef=FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_audiance_list);
        audiencerecycle=findViewById(R.id.audience_recycle);
        audiencerecycle.setLayoutManager(new LinearLayoutManager(this));
        AudianceList = new ArrayList<String>();
        firebaseDatabase=FirebaseDatabase.getInstance();

               audienceDbRef=firebaseDatabase.getReference("events/" +
                "/-LHHN8oiT_8gU_rf7JJP" +
                "/Audiance");


        audienceDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String value = snap.getValue(String.class);
                        Log.i("Tag", "Value " + value);
                        AudianceList.add(value);

                    }
                    getProfileDetails();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





//        audiencerecycle.setAdapter(new AudienceAdapter(arrayList));

    }

    private void getProfileDetails() {
        Log.d("test","getProfileDetails");
        for(int i=0;i<AudianceList.size();i++){
            final String userId = AudianceList.get(i);
            DatabaseReference ProfileDetailsRef=FirebaseDatabase.getInstance().getReference("Users/"+userId);
            ProfileDetailsRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Profile profile=dataSnapshot.getValue(Profile.class);
                    HashMap<String,String> phash=new HashMap<>();
                    phash.put("Name",profile.getName());
                    phash.put("JobTitle",profile.getJobTitle());
                    phash.put("userid",userId);
                    AudianceListUpdated.add(phash);
                    audiencerecycle.setAdapter(new AudienceAdapter(AudianceListUpdated, getApplicationContext()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
