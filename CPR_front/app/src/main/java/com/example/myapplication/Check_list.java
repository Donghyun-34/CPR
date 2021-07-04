package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class Check_list extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    public static String disease_name;

    CheckBox option1;
    CheckBox option2;
    CheckBox option3;
    CheckBox option4;
    CheckBox option5;
    CheckBox option6;
    CheckBox option7;
    CheckBox option8;
    CheckBox option9;
    CheckBox option10;
    CheckBox option11;
    CheckBox option12;
    CheckBox option13;
    CheckBox option14;
    CheckBox option15;
    CheckBox option16;
    CheckBox option17;

    public Check_list() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        option1 = (CheckBox)findViewById(R.id.checkBox1);
        option2 = (CheckBox) findViewById(R.id.checkBox2);
        option3 = (CheckBox)findViewById(R.id.checkBox3);
        option4 = (CheckBox) findViewById(R.id.checkBox4);
        option5 = (CheckBox)findViewById(R.id.checkBox5);
        option6 = (CheckBox) findViewById(R.id.checkBox6);
        option7 = (CheckBox)findViewById(R.id.checkBox7);
        option8 = (CheckBox) findViewById(R.id.checkBox8);
        option9 = (CheckBox)findViewById(R.id.checkBox9);
        option10 = (CheckBox) findViewById(R.id.checkBox10);
        option11 = (CheckBox)findViewById(R.id.checkBox11);
        option12 = (CheckBox) findViewById(R.id.checkBox12);
        option13 = (CheckBox)findViewById(R.id.checkBox13);
        option14 = (CheckBox) findViewById(R.id.checkBox14);
        option15 = (CheckBox)findViewById(R.id.checkBox15);
        option16 = (CheckBox)findViewById(R.id.checkBox16);
        option17 = (CheckBox)findViewById(R.id.checkBox17);

        option1.setOnCheckedChangeListener(this);
        option2.setOnCheckedChangeListener(this);
        option3.setOnCheckedChangeListener(this);
        option4.setOnCheckedChangeListener(this);
        option5.setOnCheckedChangeListener(this);
        option6.setOnCheckedChangeListener(this);
        option7.setOnCheckedChangeListener(this);
        option8.setOnCheckedChangeListener(this);
        option9.setOnCheckedChangeListener(this);
        option10.setOnCheckedChangeListener(this);
        option11.setOnCheckedChangeListener(this);
        option12.setOnCheckedChangeListener(this);
        option13.setOnCheckedChangeListener(this);
        option14.setOnCheckedChangeListener(this);
        option15.setOnCheckedChangeListener(this);
        option16.setOnCheckedChangeListener(this);
        option17.setOnCheckedChangeListener(this);

        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getTask task = new getTask();
                task.execute();

                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Check_list.this, Disease_info.class);
                intent.putExtra("disease_name", disease_name);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            buttonView.setBackgroundResource(R.color.semi_white);
        }
        else{
            buttonView.setBackgroundResource(0);
        }
    }

    private class getTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            disease_name = null;

            String queryUrl="http://220.149.242.12:55556/checklist?"
                    +"f1="+option1.isChecked()
                    +"&f2="+option2.isChecked()
                    +"&f3="+option3.isChecked()
                    +"&f4="+option4.isChecked()
                    +"&f5="+option5.isChecked()
                    +"&f6="+option6.isChecked()
                    +"&f7="+option7.isChecked()
                    +"&f8="+option8.isChecked()
                    +"&f9="+option9.isChecked()
                    +"&f10="+option10.isChecked()
                    +"&f11="+option11.isChecked()
                    +"&f12="+option12.isChecked()
                    +"&f13="+option13.isChecked()
                    +"&f14="+option14.isChecked()
                    +"&f15="+option15.isChecked()
                    +"&f16="+option16.isChecked()
                    +"&f17="+option17.isChecked();

            try {
                URL url= new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
                InputStream is= url.openStream(); // url 위치로 인풋스트림 연결

                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();
                // inputstream 으로부터 xml 입력받기
                xpp.setInput( new InputStreamReader(is, StandardCharsets.UTF_8) );
                String tag;
                xpp.next();
                int eventType= xpp.getEventType();
                while( eventType != XmlPullParser.END_DOCUMENT ){
                    switch( eventType ){
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tag= xpp.getName(); // 태그 이름 얻어오기
                            disease_name=tag;//어떤 태그 가져오는지 확인용으로 썼음
                            if(tag.equals("CPR"));
                            else if(tag.equals("disease_name")){//병명
                                xpp.next();
                                // addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                disease_name=xpp.getText();
                            }

                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tag= xpp.getName(); // 태그 이름 얻어오기
                            break;
                    }
                    eventType= xpp.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return disease_name;
        }

        @Override
        protected void onPostExecute(String textdata) {
            super.onPostExecute(textdata);
        }
    }
}