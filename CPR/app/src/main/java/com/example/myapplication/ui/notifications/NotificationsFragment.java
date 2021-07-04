package com.example.myapplication.ui.notifications;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Emergency_info;
import com.example.myapplication.R;

import java.util.Objects;

public class NotificationsFragment extends Fragment {
    public Button e_Call;
    private static final int PERMISSIONS_REQUEST_CODE = 1000;
    String REQUIRED_PERMISSIONS  = Manifest.permission.CALL_PHONE;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        e_Call = (Button)root.findViewById(R.id.emergency_call_btn);
        e_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:01048744647"));

                startActivity(intent);
            }
        });

        int permssionCheck = ContextCompat.checkSelfPermission(requireActivity(),REQUIRED_PERMISSIONS);

        if (permssionCheck!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_PERMISSIONS))
            {
                Toast.makeText(requireActivity(),"전화 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(requireActivity(), new String[]{REQUIRED_PERMISSIONS}, PERMISSIONS_REQUEST_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{REQUIRED_PERMISSIONS}, PERMISSIONS_REQUEST_CODE);
                Toast.makeText(requireActivity(),"전화 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            }
        }

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grandResults.length > 0 && grandResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}