package com.example.myapplication.disease_info;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

public class feature extends Fragment {
    LinearLayout layout;
    private TextView mTextview;
    private final String disease_name;

    public feature(String name){
        disease_name = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.feature, container, false);
        layout = (LinearLayout)  v.findViewById(R.id.feature);

        getTask task = new getTask();
        task.execute(disease_name);

        return v;
    }

    private class getTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String textdata){
            super.onPostExecute(textdata);

            if(textdata == null){
                mTextview.setText("Error");
            }
            else{
                String[] method_str = textdata.split("\\n");

                for (String s : method_str) {
                    mTextview = new TextView(getActivity());
                    mTextview.setText(s);
                    mTextview.setTextSize(15);
                    mTextview.setTypeface(null, Typeface.BOLD);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, 20);
                    lp.gravity = Gravity.LEFT;
                    lp.bottomMargin = 50;

                    mTextview.setLayoutParams(lp);

                    layout.addView(mTextview);
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            String name = strings[0];

            String queryUrl="http://220.149.242.12:55556/disease?"+"disease_name="+ name;

            try {
                URL url= new URL(queryUrl); //???????????? ??? ?????? url??? URL ????????? ??????.
                InputStream is= url.openStream();  //url????????? ??????????????? ??????

                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();
                xpp.setInput( new InputStreamReader(is, "UTF-8") );  //inputstream ???????????? xml ????????????

                String tag;
                xpp.next();

                int eventType= xpp.getEventType();
                while( eventType != XmlPullParser.END_DOCUMENT ){
                    switch( eventType ){
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tag= xpp.getName();    //?????? ?????? ????????????

                            if(tag.equals("item")) ;// ????????? ????????????
                            else if(tag.equals("disease_feat")){
                                xpp.next();
                                buffer.append(xpp.getText());
                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tag= xpp.getName();    //?????? ?????? ????????????
                            if(tag.equals("item"))
                                break;
                    }
                    eventType= xpp.next();
                }

                return buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }
    }
}