package com.example.welink.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welink.View.LoginActivity;
import com.example.welink.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private CircleImageView imageUpdate;
    TextView emailText,genderText,logoutText,updateText;
    EditText nameEdit, occEdit, phoneEdit,ageEdit;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_profile, container, false);

        //getting all the things needed
        updateText = rootview.findViewById(R.id.updateText);
        logoutText = rootview.findViewById(R.id.logoutText);
        imageUpdate = rootview.findViewById(R.id.imageUpdate);
        emailText = rootview.findViewById(R.id.emailEdit);
        genderText = rootview.findViewById(R.id.genderEdit);
        nameEdit = rootview.findViewById(R.id.nameUpdate);
        occEdit = rootview.findViewById(R.id.occupationUpdate);
        phoneEdit = rootview.findViewById(R.id.phoneUpdate);
        ageEdit = rootview.findViewById(R.id.ageUpdate);
        mAuth = FirebaseAuth.getInstance();
        //first setting up the current setting to the profile
        setUserInfo();

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ProfileFragment.this.getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEdit.getText().toString();
                final String occ = occEdit.getText().toString();
                final String phone = phoneEdit.getText().toString();
                final String age = ageEdit.getText().toString();
                final String uid = mAuth.getCurrentUser().getUid();
                final DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                updateRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String snapMail = dataSnapshot.child("mail").getValue().toString();
                        String snapImage = dataSnapshot.child("image").getValue().toString();
                        String snapgender = dataSnapshot.child("gender").getValue().toString();
                        HashMap<Object, String> updateMap = new HashMap<>();
                        updateMap.put("name", name);
                        updateMap.put("occupation", occ);
                        updateMap.put("phone", phone);
                        updateMap.put("age", age);
                        updateMap.put("mail", snapMail);
                        updateMap.put("image", snapImage);
                        updateMap.put("gender", snapgender);
                        updateRef.setValue(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    setUserInfo();
                                    Toast.makeText(ProfileFragment.this.getContext(), "Account Information Updated", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ProfileFragment.this.getContext(), "Update Failed ! ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        return rootview;
    }


    private void setUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid().toString();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    /// Retriving the values from firebase datebase for the current user
                    String mail = dataSnapshot.child("mail").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String occ = dataSnapshot.child("occupation").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String gender = dataSnapshot.child("gender").getValue().toString();
                    String age = dataSnapshot.child("age").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    ///setting the information to the current layout
                    Picasso.with(ProfileFragment.this.getContext()).load(image).fit().centerInside().into(imageUpdate);
                    emailText.setText("Email : " + mail);
                    nameEdit.setText(name);
                    occEdit.setText(occ);
                    phoneEdit.setText(phone);
                    genderText.setText("Gender : " + gender);
                    ageEdit.setText(age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
