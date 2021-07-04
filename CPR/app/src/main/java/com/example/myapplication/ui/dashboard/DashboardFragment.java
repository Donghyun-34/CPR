package com.example.myapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Disease_info;
import com.example.myapplication.R;
import com.example.myapplication.Emergency_info;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Button emergeny_room = (Button) root.findViewById(R.id.emergency_room_btn);
        emergeny_room.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent emergeny_room = new Intent(getActivity(), Emergency_info.class);
                emergeny_room.putExtra("msg", 1);
                startActivity(emergeny_room);
            }
        });

        Button pharmacy = (Button) root.findViewById(R.id.pharmacy_btn);
        pharmacy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent pharmacy = new Intent(getActivity(), Emergency_info.class);
                pharmacy.putExtra("msg", 2);
                startActivity(pharmacy);
            }
        });

        Button aed = (Button) root.findViewById(R.id.aed_btn);
        aed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent aed = new Intent(getActivity(), Emergency_info.class);
                aed.putExtra("msg", 3);
                startActivity(aed);
            }
        });

        Button disease_info = (Button) root.findViewById(R.id.disease_info_btn);
        disease_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent disease = new Intent(getActivity(), Disease_info.class);
                startActivity(disease);
            }
        });

        return root;
    }

    private void removeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager mFragmentManager = requireActivity().getSupportFragmentManager();
            final FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.remove(fragment);
            mFragmentTransaction.commit();
            fragment.onDestroy();
            fragment.onDetach();
            fragment = null;
        }
    }

}