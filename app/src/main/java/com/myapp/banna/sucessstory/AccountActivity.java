package com.myapp.banna.sucessstory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {
    private FirebaseDatabase mFireDatabase;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    String[] accountList={"Name : ","Your Stuff","Liked Video","Signout"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.account_list_view, accountList);
       final ListView listView = (ListView) findViewById(R.id.account_list);
        mAuth = FirebaseAuth.getInstance();
        mFireDatabase = mFireDatabase.getInstance();
        mDatabaseReference=mFireDatabase.getReference().child("UsersInfo/"+mAuth.getCurrentUser().getUid());
        mDatabaseReference.child("usersName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accountList[0]=accountList[0]+ dataSnapshot.getValue().toString();
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    Intent i = new Intent(AccountActivity.this,UserStoryActivity.class);
                    startActivity(i);
                }
                if (position==2){
                    Intent i = new Intent(AccountActivity.this,LikedVideo.class);
                    startActivity(i);
                }
                if(position==3){
                    mAuth.signOut();
                    Toast.makeText(AccountActivity.this,"Succesfully Logout",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
