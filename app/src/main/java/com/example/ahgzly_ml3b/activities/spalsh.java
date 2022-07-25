package com.example.ahgzly_ml3b.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ahgzly_ml3b.R;
import com.example.ahgzly_ml3b.fragments.code_verification;
import com.example.ahgzly_ml3b.fragments.date_picker;
import com.example.ahgzly_ml3b.fragments.sign_up;

import java.util.Locale;

public class spalsh extends AppCompatActivity implements date_picker.on_btn_ok_clicked, com.example.ahgzly_ml3b.fragments.sign_up.onBtnNextClicked, code_verification.btnPressed , code_verification.userAuthnticationDone {
    LottieAnimationView sp_lot_animation;
    private static final String Mypref= "MyPREFERENCES";
    //    ImageView splash_imageView;
    Button sign_up;
    LottieAnimationView splash_lot_sign_up;
    Animation anim;
    ConstraintLayout splash;
    LinearLayout splash_para;
    LinearLayout sign_part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //set local arabic for date picker
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config =
                getBaseContext().getResources().getConfiguration();
        config.setLocale(locale);
        createConfigurationContext(config);

        //change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.secondColor));
        }
        getSupportActionBar().hide();
        //inflate elements
        splash_lot_sign_up=findViewById(R.id.splash_lot_sign_up);
        splash_para=findViewById(R.id.splash_para);
        sign_part=findViewById(R.id.sign_part);
        sign_up=findViewById(R.id.btn_sign_up);
//        splash_imageView=findViewById(R.id.splash_background);
        splash=findViewById(R.id.splash);
        anim= AnimationUtils.loadAnimation(this,R.anim.fade);
        sp_lot_animation=findViewById(R.id.splash_lot__animation);
        sp_lot_animation.playAnimation();
        //get layout params
        LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,400);
        //check if lotti animation ended
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                splash_para.setVisibility(View.GONE);
                sign_part.setVisibility(View.GONE);
                splash_lot_sign_up.startAnimation(anim);
                splash_lot_sign_up.setVisibility(View.VISIBLE);
//                splash_imageView.animate().translationY(-1200).setDuration(800).start();
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
                ft.replace(R.id.splash_frgament_container,new sign_up());
                ft.addToBackStack("sign_up");
                ft.commit();
            }
        });


            }
    //clear SharedPreferences


    @Override
    protected void onStop() {
        super.onStop();
        final SharedPreferences pref = getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().commit();
    }

    @Override
    public void btn_ok_clicked(String date) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        com.example.ahgzly_ml3b.fragments.sign_up s=new sign_up().newInstance(date);
        ft.replace(R.id.splash_frgament_container,s);
        ft.addToBackStack(null);
        ft.commit();

        Toast.makeText(this, "date "+ date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void btn_next_clicked(String first_name, String last_name, String postion, String date_of_birth) {
        splash_lot_sign_up.setAnimation(R.raw.vf_message);
        splash_lot_sign_up.playAnimation();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        Toast.makeText(this, "name "+first_name, Toast.LENGTH_SHORT).show();
        com.example.ahgzly_ml3b.fragments.code_verification s=new code_verification().newInstance(first_name,last_name,postion,date_of_birth);
        ft.replace(R.id.splash_frgament_container,s);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void btn_Clicked(String clicked) {
        switch (clicked) {
            case "back":
                splash_lot_sign_up.setAnimation(R.raw.sign_up);
                splash_lot_sign_up.playAnimation();
                break;
        }

    }

    @Override
    public void authState(String state) {
        switch (state){
            case "done":
                Intent i=new Intent(this,MainActivity.class);
                // Check if we're running on Android 5.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Apply activity animation
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                } else {
                    // Swap without animation
                }
                startActivity(i);
        }
    }
}
