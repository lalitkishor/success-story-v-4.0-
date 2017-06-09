package com.myapp.banna.sucessstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Lalit on 6/8/2017.
 */

public class ShowStoryAdapter extends ArrayAdapter<SStory> {
    private  Context context;
    private int resource;
    private ArrayList<SStory> data = null;
    public ShowStoryAdapter(Context context, int resource,ArrayList<SStory> data) {
        super(context, resource,data);
        this.context = context;
        this.resource= resource;
        this.data=data;
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent){
        View row =convertView;
        ShowStoryHolder holder =null;
        if (row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new ShowStoryHolder();
            holder.textTitle =(TextView)row.findViewById(R.id.storyTitleLabel);
            holder.textStory =(TextView)row.findViewById(R.id.storyLine);
            row.setTag(holder);
        }
        else{
            holder = (ShowStoryHolder)row.getTag();
        }
        SStory story = data.get(position);
        holder.textTitle.setText(story.title);
        holder.textStory.setText(story.storyLine);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ShowStoryActivity.class);
                i.putExtra("Story_Title",data.get(position).title);
                i.putExtra("Full_Story",data.get(position).storyLine);
                v.getContext().startActivity(i);
            }
        });
        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here

                        FirebaseDatabase.getInstance().getReference().child("UsersStuff").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                                    System.out.println("value of postsnapshot realvalue"+postSnapshot.getValue().toString());
                                    System.out.println("value of postsnapshot realvalue position"+data.get(position).storyId);
                                    if(postSnapshot.getValue().toString().equals(data.get(position).storyId)){
                                        System.out.println("value deleted"+postSnapshot.getValue().toString());
                                        postSnapshot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        FirebaseDatabase.getInstance().getReference().child("UserLike").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                                    for (DataSnapshot valueSnapshot:postSnapshot.getChildren()){
                                        if(valueSnapshot.getKey().equals(data.get(position).storyId)){
                                            valueSnapshot.getRef().removeValue();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    FirebaseDatabase.getInstance().getReference().child("UserPOSO").child(data.get(position).storyId).removeValue();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });
         return row;
    }
   private static class ShowStoryHolder{
        TextView textTitle;
       TextView textStory;
    }
}
