package com.myapp.banna.sucessstory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.view.MenuItem;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.security.AccessController.getContext;
//import static com.myapp.banna.sucessstory.R.layout.activity_story_writing;


public class StoryWriting extends AppCompatActivity {
    // Firebase instance variable
    String evalue;
    private FirebaseDatabase mFireDatabase; // class in fire base databse api 'Entery point of firebase database'
    private DatabaseReference mDatabaseReference;// class reference the specific part of the database
    private DatabaseReference userRef;
    private DatabaseReference userStory;
    private FirebaseAuth mFirebaseAuth;
    private EditText title,story;
    private ImageButton mPhotoPickerButton;
    public StorageReference mStorageReference;
    public FirebaseStorage inspire_photo_Storage;
    private static final int RC_PHOTO_PICKER = 1;
    private int nuiqueId ;
    UserPOSO user= new UserPOSO();
    UsersInfo fbu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_writing);
         title = (EditText)findViewById(R.id.titleText);
         story= (EditText)findViewById(R.id.writeStory);
        mFireDatabase = mFireDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        inspire_photo_Storage=inspire_photo_Storage.getInstance();
        mDatabaseReference = mFireDatabase.getReference().child("UserPOSO");
        userRef= mFireDatabase.getReference().child("UsersInfo/"+mFirebaseAuth.getCurrentUser().getUid());
        userStory = mFireDatabase.getReference().child("UsersStuff").child(mFirebaseAuth.getCurrentUser().getUid());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done_item) {
            //mEditText=(EditText)findViewById(R.id.editText);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fbu =  dataSnapshot.getValue(UsersInfo.class);
                    System.out.println("Iiiiiiiii"+fbu.getUsersName());
                    user.SetUserName(fbu.getUsersName());
                    user.setPhotoUrl(fbu.getUsersPhotoUrl());
                    user.setName(title.getText().toString().trim());
                    user.setStory(story.getText().toString().trim());
                    user.setStoryLike("0");
                    user.setLikeState(0);
                    mDatabaseReference =mDatabaseReference.push();
                    user.setStoryId(mDatabaseReference.getKey());
                   // likeRef= mFireDatabase.getReference().child("StoryLike/"+mDatabaseReference.getKey());
                    //likeRef.setValue("0");
                    mDatabaseReference.setValue(user);
                    userStory.push().setValue(mDatabaseReference.getKey().toString());
                    //mFireDatabase.getReference().child("UserLike").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mDatabaseReference.getKey().toString()).child("likeState").setValue(0);
                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            //System.out.println("jgfdghjkl",);
            Intent i=new Intent(this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
