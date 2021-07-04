package com.example.myapplication.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Check_list;
import com.example.myapplication.R;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("간이 문진표 작성을 위해 아래의 버튼을 눌러주세요");
    }

    public LiveData<String> getText() {
        return mText;
    }
}