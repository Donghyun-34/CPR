package com.example.myapplication.disease_info.Myocardial_infarction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.disease_info.feature;
import com.example.myapplication.disease_info.method;
import com.example.myapplication.disease_info.symptom;

import java.util.ArrayList;

public class Viewpagerad extends FragmentPagerAdapter {
    private final ArrayList<Fragment> items;

    public Viewpagerad(@NonNull FragmentManager fm) {
        super(fm);
        items = new ArrayList<>();
        String name = "심근경색";
        items.add(new method(name));
        items.add(new symptom(name));
        items.add(new feature(name));
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}