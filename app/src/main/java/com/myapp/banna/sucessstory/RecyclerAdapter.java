package com.myapp.banna.sucessstory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lalit on 1/23/2017.
 */

public  class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.UserViewHolder>{
    private static final int AD_VIEW_TYPE =1 ;
    ArrayList<UserPOSO> userInfo;
    FirebaseUser currentUser;
    private static final int AD_VIEW_ID = 1;
    private FirebaseDatabase mFireDatabase;
    private DatabaseReference UsersRef;
    private ArrayList<String> likeStoryID = new ArrayList<String>();
    RecyclerAdapter(ArrayList<UserPOSO> userInfo){
        this.userInfo = userInfo;
    }
    public class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView userName,storyTitle,fullStory,numberOfLike;
        public Button likeButton;

        public UserViewHolder(final View itemView) {
            super(itemView);
            userName  =(TextView)itemView.findViewById(R.id.name_user);
            itemImage =(ImageView)itemView.findViewById(R.id.user_pic);
            numberOfLike = (TextView) itemView.findViewById(R.id.numberOfLike);
            storyTitle = (TextView)itemView.findViewById(R.id.storyTitle);
            fullStory = (TextView)itemView.findViewById(R.id.fullStory);
            likeButton = (Button)itemView.findViewById(R.id.likeButton);
            if (MainActivity.authState()!=null) {
                if(FirebaseDatabase.getInstance().getReference().child("UserLike").child(FirebaseAuth.getInstance().getCurrentUser().getUid())!=null) {
                    FirebaseDatabase.getInstance().getReference().child("UserLike").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            likeStoryID.clear();
                            for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                                likeStoryID.add(postSnapshot.getKey());
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = new Intent(v.getContext(),ShowStoryActivity.class);
                    System.out.println("Item id aabc"+userInfo.get(getAdapterPosition()).getStory());
                    i.putExtra("Story_Title",userInfo.get(getPosition()).getName());
                    i.putExtra("Full_Story",userInfo.get(getPosition()).getStory());
                    v.getContext().startActivity(i);
                }
            });
        }
    }
    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder{

        public NativeExpressAdViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main,viewGroup,false);
                UserViewHolder userViewHolder = new UserViewHolder(v);
                return userViewHolder;
        }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.UserViewHolder holder, final int position) {
        if(check(userInfo.get(position).getStoryId())){
            System.out.println("lalitBanna");
            holder.likeButton.setEnabled(false);
            holder.likeButton.setText("Liked");
            holder.likeButton.setTextColor(Color.parseColor("#b57a3f"));
        }
        else {
            holder.likeButton.setEnabled(true);
            holder.likeButton.setText("Like");
            holder.likeButton.setTextColor(Color.parseColor("#000000"));
        }
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.authState()!=null) {
                        if (check(userInfo.get(position).getStoryId())) {
                            System.out.println("This is not Empty");
                        } else {
                            MainActivity.setLike(userInfo.get(position).getStoryId());
                        }
                    }
                    else {
                        Toast.makeText(holder.itemView.getContext(),"Need SignIn",Toast.LENGTH_LONG).show();
                    }
                }
            });

        Picasso.with(holder.itemImage.getContext()).load(userInfo.get(position).getPhotoUrl()).into(holder.itemImage);
        //  holder.itemImage.setImageBitmap(bitmap);
        System.out.println("Item of age" + position);
        holder.userName.setText(userInfo.get(position).getUserName());
        holder.storyTitle.setText(userInfo.get(position).getName());
        holder.fullStory.setText(userInfo.get(position).getStory());
        holder.numberOfLike.setText(userInfo.get(position).getStoryLike() + " Like");

    }

    @Override
    public int getItemCount() {
        return userInfo.toArray().length;
    }
    public boolean check(String stroyId){
        for(String object : likeStoryID){
            System.out.print("value of story Id   "+object+"    kjgkj");
            if(object.equals(stroyId)){
                return true;
            }

        }
        return false;
    }
}
