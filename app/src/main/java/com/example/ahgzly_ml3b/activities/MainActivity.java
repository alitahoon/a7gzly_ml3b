package com.example.ahgzly_ml3b.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ahgzly_ml3b.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser firebaseUser;
        firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            //user is loged in

        }else{
            //user us loged out
        }
    }
}