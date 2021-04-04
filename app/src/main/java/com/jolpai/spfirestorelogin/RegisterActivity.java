package com.jolpai.spfirestorelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().setTitle("Register");

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String confirmPassword = editTextConfirmPassword.getText().toString();
            if(isFormValid(name,email,password,confirmPassword)){
                register(name,email,password);
            }
        });
    }

    private boolean isFormValid(String name, String email, String password, String confirmPassword){
        if(name.equalsIgnoreCase("")){
            showMessage("Please type your name.");
        }else if(email.equalsIgnoreCase("")){
            showMessage("Please type your email address");
        }else if(password.equalsIgnoreCase("")){
            showMessage("Please type password");
        }else if(password.length()>6){
            showMessage("Password should be at least 6 character");
        }else if(confirmPassword.equalsIgnoreCase("")){
            showMessage("Confirm password is empty!!");
        }else if(password.equalsIgnoreCase(confirmPassword)){
            return true;
        }else{
            showMessage("Password did not match !!");
        }
        return false;
    }

    private void register(String name, String email, String password){
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("password", password);
        String t = user.get("name").toString();
        CollectionReference userCollection = db.collection(("users"));
        Query userQuery = userCollection.whereEqualTo("email",email);
        userQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    addUser(user);
                }else{
                    showMessage("This user already exist.");
                }
            }
        });
    }

    private void addUser(Map<String, Object> user){
        db.collection("users")
            .add(user)
            .addOnSuccessListener(documentReference -> {
                Log.d("TAG-S", " ID: " + documentReference.getId());
                showMessage("Registration done successfully.");
                navigateTo(LoginActivity.class);
            })
            .addOnFailureListener(e -> Log.w("TAG-F", "Error adding document", e));

    }


    private void navigateTo(Class targetClass){
        Intent intent = new Intent(this, targetClass);
        startActivity(intent);
    }

    private void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}