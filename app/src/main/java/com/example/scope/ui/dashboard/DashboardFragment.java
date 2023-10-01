package com.example.scope.ui.dashboard;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.scope.MainActivity;
import com.example.scope.R;
import com.example.scope.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    public TextView positionX;
    public TextView positionY;
    public TextView positionZ;
    Button buttonBluetoothConnect;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        positionX = (TextView) root.findViewById(R.id.positionX);
        positionY = (TextView) root.findViewById(R.id.positionY);
        positionZ = (TextView) root.findViewById(R.id.positionZ);
        buttonBluetoothConnect = root.findViewById(R.id.buttonBluetoothConnect);

        positionY.setText("synchro en cours");
        positionX.setText("synchro en cours");
        positionZ.setText("synchro en cours");
        if(MainActivity.bleManager.getIsConnected()) {
            buttonBluetoothConnect.setText("Se connecter");
        } else {
            buttonBluetoothConnect.setText("Se déconnecter");
        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("position Update"));

        buttonBluetoothConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!MainActivity.bleManager.getIsConnected()){
                    buttonBluetoothConnect.setText("Se déconnecter");
                   MainActivity.bleManager.scanLeDevice();
                } else {
                    buttonBluetoothConnect.setText("Se connecter");
                    MainActivity.bleManager.disconnectDevice();
                }
            }
        });



        // maybe re init scope

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            positionX.setText(intent.getStringExtra("longitude"));
            positionY.setText(intent.getStringExtra("latitude"));
            positionZ.setText(intent.getStringExtra("altitude"));
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }






}