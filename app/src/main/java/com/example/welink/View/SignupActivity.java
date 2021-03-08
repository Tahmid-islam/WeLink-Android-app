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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    private Button registerButton;
    private ImageView PersonImage;
    private EditText ConEditText, EmailEditText,PassEditText;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String userRandomKey, downloadImageUrl;
    private StorageReference userImageRef;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;


    /// --------------------------- user data upload to database starts here ---------------------------////////////

    public void onRegister(View view){
        ValidateUser();
    }


    private void ValidateUser()
    {
        String email = EmailEditText.getText().toString();
        String pass = PassEditText.getText().toString();
        String conPass = ConEditText.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email Is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Password Is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(!pass.equals(conPass)){
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignupActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, profileSetupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SignupActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });



            //StoreUserInformation();
        }
    }


    private void StoreUserInformation()
    {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
       // saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        //saveCurrentTime = currentTime.format(calendar.getTime());

       // userRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = userImageRef.child(ImageUri.getLastPathSegment() + userRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(SignupActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(SignupActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(SignupActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            //SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    /*public void SaveProductInfoToDatabase(){

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("phone", phone);
        userMap.put("occupation", occupation);
        userMap.put("image", downloadImageUrl);
        userMap.put("gender", gender);
        userMap.put("age", age);
        userMap.put("password", pass);

        User user = new User(age,email, gender, downloadImageUrl,  name,  occupation,  pass,  phone);

        userRef.child(email).setValue(user);

    }*/


    ///-------------------------user data upload to database ends here -----------------------////



    /// -------------------------image choosing block starts here ------------------------------------ ///

    public void onImageView(View view){
        OpenGallery();
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            PersonImage.setImageURI(ImageUri);
        }
    }


    /// -------------------------image choosing block ends here ------------------------- ///





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();


        //button and edittext
        EmailEditText = findViewById(R.id.email);
        PassEditText = findViewById(R.id.password);
        ConEditText = findViewById(R.id.confirmPass);


        registerButton = (Button) findViewById(R.id.signUpBtn);


        userImageRef = FirebaseStorage.getInstance().getReference().child("User Images");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

    }


}
