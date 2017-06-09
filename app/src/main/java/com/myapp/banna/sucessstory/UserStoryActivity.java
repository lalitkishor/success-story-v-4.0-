package com.myapp.banna.sucessstory;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserStoryActivity extends AppCompatActivity {
    public ArrayList<SStory> allStorys = new ArrayList<SStory>();
    private FirebaseDatabase mFireDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference userStory;
    private DatabaseReference myStuffRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_story);
        mFireDatabase = mFireDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        final ListView listView = (ListView)findViewById(R.id.user_story_list);
        final ShowStoryAdapter adapter = new ShowStoryAdapter(this,
                R.layout.story_title, allStorys);
        userStory = mFireDatabase.getReference().child("UsersStuff").child(mFirebaseAuth.getCurrentUser().getUid());
        userStory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allStorys.clear();
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    myStuffRef =mFireDatabase.getReference().child("UserPOSO/"+postSnapshot.getValue().toString());
                    myStuffRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getKey()!=null){
                                allStorys.add(new SStory(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("story").getValue().toString(),dataSnapshot.child("storyId").getValue().toString()));
                                listView.setAdapter(adapter);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        listView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener(){
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                return false;
//            }
//        });

    }
}
