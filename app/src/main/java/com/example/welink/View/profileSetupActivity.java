package com.example.welink.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.welink.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class profileSetupActivity extends AppCompatActivity {
    ImageView profileImage;
    EditText NameEdit, OccEdit,GenderEdit,PhoneEdit,AgeEdit;
    Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserId,downloadImageUrl,saveCurrentDate,saveCurrentTime,productRandomKey;
    final static int galleryPick = 1;
    private Uri ImageUri;
    private StorageReference userImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);


        profileImage = findViewById(R.id.profilePicture);
        NameEdit = findViewById(R.id.name);
        OccEdit = findViewById(R.id.occupation);
        GenderEdit = findViewById(R.id.gender);
        PhoneEdit = findViewById(R.id.phone);
        AgeEdit = findViewById(R.id.age);
        saveButton = findViewById(R.id.saveButton);
        userImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreImage();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, galleryPick);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== galleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            profileImage.setImageURI(ImageUri);
        }
    }



    private void StoreImage()
    {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = userImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(profileSetupActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(profileSetupActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(profileSetupActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                            saveUserInfo();
                        }
                    }
                });
            }
        });
    }




    private void saveUserInfo() {
        String name = NameEdit.getText().toString();
        String occupation = OccEdit.getText().toString();
        String gender = GenderEdit.getText().toString();
        String phone = PhoneEdit.getText().toString();
        String age = AgeEdit.getText().toString();
        String mail = mAuth.getCurrentUser().getEmail();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Insert Username!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(occupation)){
            Toast.makeText(this, "Please Insert Occupation!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(gender)){
            Toast.makeText(this, "Please Insert Gender!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Insert Phone No.!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(age)){
            Toast.makeText(this, "Please Insert Age!", Toast.LENGTH_SHORT).show();
        }else{
            HashMap userMap = new HashMap();
            userMap.put("name", name);
            userMap.put("occupation", occupation);
            userMap.put("gender", gender);
            userMap.put("phone", phone);
            userMap.put("age", age);
            userMap.put("mail", mail);
            userMap.put("image", downloadImageUrl);

            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(profileSetupActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(profileSetupActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(profileSetupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }



}
