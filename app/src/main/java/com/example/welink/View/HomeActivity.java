package com.example.welink.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.welink.Fragments.AllUserFragment;
import com.example.welink.Fragments.HomeFragment;
import com.example.welink.Fragments.NotificationFragment;
import com.example.welink.Fragments.PostFragment;
import com.example.welink.Fragments.ProfileFragment;
import com.example.welink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;
    private HomeFragment homeFragment;
    private AllUserFragment allUserFragment;
    private ProfileFragment profileFragment;
    private NotificationFragment notificationFragment;
    private PostFragment postFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        mainNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        //getting the fragments
        homeFragment = new HomeFragment();
        allUserFragment = new AllUserFragment();
        profileFragment = new ProfileFragment();
        notificationFragment = new NotificationFragment();
        postFragment = new PostFragment();
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        addFragment(homeFragment);
                        return true;
                    case R.id.friendList:
                        addFragment(allUserFragment);
                        return true;
                    case R.id.profile:
                        addFragment(profileFragment);
                        return true;
                    case R.id.notification:
                        addFragment(notificationFragment);
                        return true;
                    case R.id.post:
                        addFragment(postFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            CheckUserExistance();
        }


        addFragment(homeFragment);
    }

    private void CheckUserExistance() {
        final String currentUserId = mAuth.getCurrentUser().getUid();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //authenticated but not authorized
                if(! dataSnapshot.hasChild(currentUserId)){
                    Intent intent = new Intent(HomeActivity.this , profileSetupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
