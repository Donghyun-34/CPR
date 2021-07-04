package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.disease_info.Angina.Angina;
import com.example.myapplication.disease_info.Asthma.Asthma;
import com.example.myapplication.disease_info.Fracture.Fracture;
import com.example.myapplication.disease_info.Heatstroke.Heatstroke;
import com.example.myapplication.disease_info.Myocardial_infarction.Myocardial_infarction;
import com.example.myapplication.disease_info.Stroke.Stroke;
import com.example.myapplication.disease_info.Sunstroke.Sunstroke;
import com.google.android.material.tabs.TabLayout;

public class Disease_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_info);

        Button stroke = (Button)findViewById(R.id.stroke_btn);
        stroke.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Disease_info.this, Stroke.class);
                finish();
                startActivity(intent);
            }
        });

        Button heatstroke = (Button)findViewById(R.id.heatstroke_btn);
        heatstroke.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Disease_info.this, Heatstroke.class);
                finish();
                startActivity(intent);
            }
        });

        Button sunstroke = (Button)findViewById(R.id.sunstroke_btn);
        sunstroke.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Disease_info.this, Sunstroke.class);
                finish();
                startActivity(intent);
            }
        });

        Button asthma = (Button)findViewById(R.id.asthma_btn);
        asthma.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Disease_info.this, Asthma.class);
                finish();
                startActivity(intent);
            }
        });

        Button fracture = (Button)findViewById(R.id.fracture_btn);
        fracture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Disease_info.this, Fracture.class);
                finish();
                startActivity(intent);
            }
        });

        Button angina = (Button)findViewById(R.id.angina_btn);
        angina.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Disease_info.this, Angina.class);
                finish();
                startActivity(intent);
            }
        });

        Button myocardial_infarction = (Button)findViewById(R.id.myocardial_infarction_btn);
        myocardial_infarction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Disease_info.this, Myocardial_infarction.class);
                finish();
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("disease_name");

        if(name != null){
            switch (name){
                case "천식": asthma.callOnClick();break;
                case "골절": fracture.callOnClick();break;
                case "협심증": angina.callOnClick();break;
                case "심근경색": myocardial_infarction.callOnClick();break;
                case "일사병": sunstroke.callOnClick();break;
                case "열사병": heatstroke.callOnClick();break;
                case "뇌졸중": stroke.callOnClick();break;
                default: break;
            }
        }
    }
}