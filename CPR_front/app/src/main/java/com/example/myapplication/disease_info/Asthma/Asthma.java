package com.example.myapplication.disease_info.Asthma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.myapplication.Emergency_info;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.disease_info.Angina.Angina;
import com.example.myapplication.disease_info.Myocardial_infarction.Myocardial_infarction;
import com.example.myapplication.ui.notifications.NotificationsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Asthma extends AppCompatActivity {
    Animation fab_open, fab_close;
    FloatingActionButton fab1, fab2, fab3, fab4, fab5;
    Boolean openFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disease_info_fragment);

        ViewPager vp = findViewById(R.id.method_view);
        Viewpagerad ad = new Viewpagerad(getSupportFragmentManager());
        vp.setAdapter(ad);

        TabLayout tab = findViewById(R.id.method_tabLayout);
        tab.setupWithViewPager(vp);

        fab_init();

        Objects.requireNonNull(tab.getTabAt(0)).setText("대처법") ;
        Objects.requireNonNull(tab.getTabAt(1)).setText("증상") ;
        Objects.requireNonNull(tab.getTabAt(2)).setText("특징") ;

        Button home = (Button)findViewById(R.id.disease_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Asthma.this, MainActivity.class);
                finish();
                startActivity(home);
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.emergency_function1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.emergency_function2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:01048744647"));

                startActivity(intent);
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton)findViewById(R.id.emergency_function3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Intent emergeny_room = new Intent(Asthma.this, Emergency_info.class);
                emergeny_room.putExtra("msg", 1);
                startActivity(emergeny_room);
            }
        });

        FloatingActionButton fab4 = (FloatingActionButton)findViewById(R.id.emergency_function4);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Intent pharmacy = new Intent(Asthma.this, Emergency_info.class);
                pharmacy.putExtra("msg", 2);
                startActivity(pharmacy);
            }
        });

        FloatingActionButton fab5 = (FloatingActionButton)findViewById(R.id.emergency_function5);
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Intent aed = new Intent(Asthma.this, Emergency_info.class);
                aed.putExtra("msg", 3);
                startActivity(aed);
            }
        });
    }

    //fab 버튼을 초기화 하는 함수
    public void fab_init(){
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab1 = findViewById(R.id.emergency_function1);
        fab2 = findViewById(R.id.emergency_function2);
        fab3 = findViewById(R.id.emergency_function3);
        fab4 = findViewById(R.id.emergency_function4);
        fab5 = findViewById(R.id.emergency_function5);

        fab2.startAnimation(fab_close);
        fab3.startAnimation(fab_close);
        fab4.startAnimation(fab_close);
        fab5.startAnimation(fab_close);
        fab2.setClickable(false);
        fab3.setClickable(false);
        fab4.setClickable(false);
        fab5.setClickable(false);
    }

    //fab 버튼의 animation을 조정하는 함수
    public void anim() {

        if (openFlag) {
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab5.startAnimation(fab_close);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            fab5.setClickable(false);
            openFlag = false;
        } else {
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab5.startAnimation(fab_open);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            fab5.setClickable(true);
            openFlag = true;
        }
    }
}