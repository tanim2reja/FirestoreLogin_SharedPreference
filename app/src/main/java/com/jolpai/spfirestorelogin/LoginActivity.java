package com.jolpai.spfirestorelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText editTextEmail;
    EditText editTextPassword;
    Button btnLogin;
    TextView noAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().setTitle("Login");

        editTextEmail = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        noAccount = findViewById(R.id.lblNoAccount);

        btnLogin.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if(email.equalsIgnoreCase("")){
                showMessage("Please type email.");
            }else if(password.equalsIgnoreCase("")){
                showMessage("Please type password");
            }else{
                login(email, password);

            }
        });

        noAccount.setOnClickListener(view -> navigateTo(RegisterActivity.class));

        userLoggedStatus();

    }

    private void userLoggedStatus(){
        SharedPreferences shared = getSharedPreferences("SavedUser", MODE_PRIVATE);
        String user = (shared.getString("user", null));
        if(user != null){
            navigateTo(HomeActivity.class);
            finish();
        }
    }

    private void login(String email, String password){
        CollectionReference userCollection = db.collection(("users"));
        Query userQuery = userCollection.whereEqualTo("email",email).whereEqualTo("password",password);
        userQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    showMessage("Your Credential is wrong. Please try again..");
                }else{
                    // go to home
                    showMessage("Login Successful.");
                    navigateTo(HomeActivity.class);

                    saveToSharedPreference(queryDocumentSnapshots.getDocuments());
                }
            }
        });
    }

    private void saveToSharedPreference(List<DocumentSnapshot> documents) {
        for (DocumentSnapshot doc : documents ) {
            System.out.println(doc);
            SharedPreferences.Editor editor = getSharedPreferences("SavedUser", MODE_PRIVATE).edit();
            String user = doc.getData().toString();
            editor.putString("user", doc.getData().toString());
            editor.commit();
        }

    }

    private void navigateTo(Class targetClass){
        Intent intent = new Intent(this, targetClass);
        startActivity(intent);
        finish();
    }

    private void showMessage(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}