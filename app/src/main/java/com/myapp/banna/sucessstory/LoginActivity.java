package com.myapp.banna.sucessstory;
import android.app.ProgressDialog;
import android.content.pm.PackageInstaller;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.auth.AuthUI;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.app.Activity.RESULT_OK;

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private FirebaseDatabase mFireDatabase;
    CallbackManager mCallbackManager = CallbackManager.Factory.create();
    UsersInfo usersInfo = new UsersInfo();
    AccessToken accessToken;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // LoginManager.getInstance().logOut();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        Button skipButton = (Button) findViewById(R.id.skipButton);
        loginButton.setReadPermissions("email","user_photos","public_profile");
        mAuth = FirebaseAuth.getInstance();
        mFireDatabase = mFireDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        //AccessToken.getCurrentAccessToken().getPermissions();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("I'm login onSuccess");
                handleFacebookAccessToken(loginResult.getAccessToken());
                 accessToken = loginResult.getAccessToken();

                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        if(mAuth.getCurrentUser()!=null){
                            UsersRef = mFireDatabase.getReference().child("UsersInfo/"+mAuth.getCurrentUser().getUid());
                        }
                        else{
                            // UsersRef = mFireDatabase.getReference().child("UsersInfo/"+mAuth.getCurrentUser().getUid());
                        }
                        System.out.println("system"+graphResponse.getJSONObject().optString("name"));
                        graphResponse.getJSONObject().optString("email");
                        //user.(user.optString("email"));

                        System.out.println("yeandar ki bat hai"+user.optString("name"));
                        usersInfo.setUsersName(user.optString("name"));
                        if (graphResponse != null) {
                            try {
                                JSONObject data = graphResponse.getJSONObject();
                                if (data.has("id")) {
                                   // String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    String profilePicUrl="https://graph.facebook.com/"+data.optString("id")+"/picture?type=large";
                                    usersInfo.setUsersPhotoUrl(profilePicUrl);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        FirebaseUser Fbuser = mAuth.getCurrentUser();
                        if(Fbuser !=null){
                            usersInfo.setUsersId(Fbuser.getUid());
                            UsersRef.setValue(usersInfo);
                        }

                    }
                }).executeAsync();
                if(usersInfo!= null){
                    System.out.println("reference sahi chal rha hai"+usersInfo+usersInfo.getUsersId()+usersInfo.getUsersPhotoUrl()+usersInfo.getUsersName());
                }
                System.out.println("yeandarki bat h"+UsersRef);

                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                System.out.println("sghsgfhghfg");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode ,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        System.out.println("jai shree ram");
    }
    private void handleFacebookAccessToken(AccessToken token) {
       // Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // Sign in success, update UI with the signed-in user's information
                        //    Log.d(TAG, "signInWithCredential:success");

                          //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
