package com.myapp.banna.sucessstory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UnknownFormatFlagsException;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFireDatabase;

    private DatabaseReference mDatabaseReference;
    // A Native Express ad is placed in every nth position in the RecyclerView.
    public static final int ITEMS_PER_AD = 2;
     // The Native Express ad unit ID.
        private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";private TextView textView;
    private String str;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private boolean menuShow,MmenuShow = false;
    private static String numberOfLike;
    private static boolean mProcessLike;
    private static String stateLike="0";
    private  static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private int maxNumber;
   // public final ArrayAdapter<UserPOSO> = new ArrayAdapter()

    public final ArrayList<UserPOSO> allUserInfo = new ArrayList<UserPOSO>();
    public final ArrayList<UsersInfo>fbUsers = new ArrayList<UsersInfo>();
    public static final ArrayList<LikePOSO> allLikes = new ArrayList<LikePOSO>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireDatabase = mFireDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabaseReference = mFireDatabase.getReference().child("UserPOSO");

        Window window = this.getWindow();

//// clear FLAG_TRANSLUCENT_STATUS flag:
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//// finally change the color
       // window.setStatusBarColor(ContextCompat.getColor(this,R.color.transparent));
        //final String userId = mDatabaseReference.push().getKey();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView)findViewById(R.id.name_user);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
//        p.setAnchorId(findViewById(R.id.onlyFloating).getId());
//        p.anchorGravity= Gravity.BOTTOM|Gravity.RIGHT|Gravity.END;
//        fab.setLayoutParams(p);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser()!=null){
                Intent intent = new Intent(MainActivity.this,StoryWriting.class);
                startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"Need Sign In",Toast.LENGTH_LONG).show();
                }
            }
        });
        final RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_layout);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        rv.setLayoutManager(llm);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            allUserInfo.clear();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                maxNumber= (int) dataSnapshot.getChildrenCount();
                System.out.println("value of postSnapshot currently"+postSnapshot.getChildrenCount());
                UserPOSO user =  postSnapshot.getValue(UserPOSO.class);
                allUserInfo.add(user);
            }
            RecyclerView.Adapter adapter = new RecyclerAdapter(allUserInfo);
            System.out.println("ye hui na baat"+adapter.getItemCount());
            rv.setAdapter(adapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getMessage());
        }
    });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

            if (id == R.id.action_settings) {
                if(mAuth.getCurrentUser()==null){
                    Toast.makeText(this,"Need Signin",Toast.LENGTH_LONG).show();
                }
                else {
                Intent i = new Intent(MainActivity.this,AccountActivity.class);
                startActivity(i);
                }
            }


            if(id==R.id.login){
                if(mAuth.getCurrentUser()==null){
                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(this,"Already Signin",Toast.LENGTH_LONG).show();
                }

            }

        return super.onOptionsItemSelected(item);
    }

public static void setLike(final String nextUrl){
    final DatabaseReference storyLike;
    final int[] a = new int[1];
    storyLike = mDatabase.getReference().child("UserPOSO").child(nextUrl).child("storyLike");
    mProcessLike=true;
    System.out.println("value of aa"+mProcessLike);
    storyLike.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //UserPOSO use =  dataSnapshot.getValue(UserPOSO.class);
            if(mProcessLike){
                a[0]= Integer.parseInt(dataSnapshot.getValue().toString()) +1 ;
                System.out.println("value of a"+ a[0]);
                storyLike.setValue(Integer.toString(a[0]));
                mDatabase.getReference().child("UserLike").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(nextUrl).child("likeState").setValue(1);
                mProcessLike=false;
            }
            //MainActivity.setUserLikeValue(Integer.toString(a[0]),nextUrl);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getMessage());
        }
    });

}
    public static FirebaseUser authState(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
