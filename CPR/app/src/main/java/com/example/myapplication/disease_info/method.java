package com.example.myapplication.disease_info;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import kr.co.prnd.YouTubePlayerView;

public class method extends Fragment {
    LinearLayout layout;
    private TextView mTextview;
    private final String disease_name;
    private final String[] disease1 = {"천식", "열사병", "일사병", "협심증", "뇌졸중", "심근경색", "골절"};
    private final ArrayList<String> disease = new ArrayList<>(Arrays.asList(disease1));
    private final int[][] index_of_image = {
            {0, 0, 0, R.drawable.asthma_method_1, R.drawable.asthma_thing, 0, 0, 0, R.drawable.asthma_method2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, R.drawable.asthma_method_1, 0, 0, 0, 0, 0, 0, 0, 0},
            {R.drawable.first_aid2, 0, 0, 0, R.drawable.asthma_method_1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, R.drawable.first_aid1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, R.drawable.asthma_method_1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, R.drawable.first_aid1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public method(String name){
        disease_name = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.method, container, false);
        layout = (LinearLayout) v.findViewById(R.id.method);

        getTask task = new getTask();
        task.execute(disease_name);

        return v;
    }

    private class getTask extends AsyncTask<String, Void, String> {


        @SuppressLint({"RtlHardcoded", "ResourceAsColor", "SetJavaScriptEnabled"})
        @Override
        protected void onPostExecute(String textdata){
            super.onPostExecute(textdata);
            int count = 0;

            if(textdata == null){
                mTextview.setText("Error");
            }
            else{
                String[] method_str = textdata.split("\\n");
                int disease_num = disease.indexOf(disease_name);

                for (String s : method_str) {
                    if(count < 2){
                        mTextview = new TextView(getActivity());
                        mTextview.setText(s);
                        mTextview.setTextSize(22);
                        mTextview.setTypeface(null, Typeface.BOLD);
                        mTextview.setTextColor(R.color.red);
                        if(index_of_image[disease_num][count] != 0){
                            mTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, index_of_image[disease_num][count]);
                        }
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 0, 0, 20);
                        lp.gravity = Gravity.LEFT;
                        lp.bottomMargin = 50;

                        mTextview.setLayoutParams(lp);

                        layout.addView(mTextview);
                    }
                    else{
                        mTextview = new TextView(getActivity());
                        mTextview.setText(s);
                        mTextview.setTextSize(18);
                        mTextview.setTypeface(null, Typeface.BOLD);
                        if(index_of_image[disease_num][count] != 0){
                            mTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, index_of_image[disease_num][count]);
                        }
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 0, 0, 20);
                        lp.gravity = Gravity.LEFT;
                        lp.bottomMargin = 50;

                        mTextview.setLayoutParams(lp);

                        layout.addView(mTextview);
                    }
                    count++;
                }
                if(disease_name.equals("협심증") || disease_name.equals("심근경색")){
                    YouTubePlayerView myoutube = new YouTubePlayerView(requireActivity());
                    myoutube.setVisibility(View.VISIBLE);
                    myoutube.play("d_v8ap396gk", null);
                    layout.addView(myoutube);
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            String name = strings[0];
            String queryUrl="http://220.149.242.12:55556/disease?"+"disease_name="+ name;

            try {
                URL url= new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
                InputStream is= url.openStream();  //url위치로 입력스트림 연결

                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();
                xpp.setInput( new InputStreamReader(is, "UTF-8") );  //inputstream 으로부터 xml 입력받기

                String tag;
                xpp.next();

                int eventType= xpp.getEventType();
                while( eventType != XmlPullParser.END_DOCUMENT ){
                    switch( eventType ){
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tag= xpp.getName();    //테그 이름 얻어오기

                            if(tag.equals("item")) ;// 첫번째 검색결과
                            else if(tag.equals("disease_treat")){
                                xpp.next();
                                buffer.append(xpp.getText());
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag= xpp.getName();    //테그 이름 얻어오기
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