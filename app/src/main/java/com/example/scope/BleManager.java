package com.example.scope;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BleManager {
    private boolean isConnected = false;
    //public BluetoothAdapter bluetoothAdapter;
    public BluetoothLeScanner bluetoothLeScanner;
    Context context;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 20000;
    private boolean scanning =false;
    private Handler handler = new Handler();
    public BluetoothGatt bluetoothGatt;

    private ScanCallback leScanCallback;

    private ScanSettings scanSettings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
            .setNumOfMatches(1)
            .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
            .build();

    private ScanFilter scanFilter = new ScanFilter.Builder()
            .setServiceUuid(ParcelUuid.fromString("19b10000-e8f2-537e-4f6c-d104768a1214"))
            .build();

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d("ChangementD'état", String.valueOf(newState));
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.d("DécouverteService", String.valueOf(status));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d("CaractRead", characteristic.toString());
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d("CaractWrite", characteristic.toString());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d("CaractChange", characteristic.toString());
        }
    };

    private List<ScanFilter> scanFilterList = new ArrayList<>();

    public BleManager(Context context, BluetoothAdapter bluetoothAdapter){
        this.context = context;
        this.scanFilterList.add(scanFilter);
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        leScanCallback =
                new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        scanning = false;
                        bluetoothLeScanner.stopScan(leScanCallback);
                        try {
                            bluetoothGatt = result.getDevice().connectGatt(context,true, bluetoothGattCallback);
                            Toast.makeText(context,"Connecté",Toast.LENGTH_SHORT).show();
                            isConnected = true;
                        } catch (Exception e) {
                            Toast.makeText(context,"Unable to connect",Toast.LENGTH_SHORT).show();
                        }


                        /*String device = result.getDevice().getName();
                        ArrayList<String> leDeviceListAdapter = new ArrayList<String>();
                        if (device != null) {
                            leDeviceListAdapter.add(result.getDevice().getName());
                        }else {
                            Log.d("device sans nom", result.getDevice().getUuids().toString());
                        }
                        for (String name: leDeviceListAdapter
                        ) {
                            Log.d("résulat scan", name);
                        }*/
                    }
                };
    }

    public void scanLeDevice() {
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);
            scanning = true;
            bluetoothLeScanner.startScan(scanFilterList,scanSettings,leScanCallback); // inclure les options
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    public boolean getIsConnected(){
        return isConnected;
    }

    public void disconnectDevice(){
        isConnected = false;
        bluetoothGatt.disconnect();
        Toast.makeText(context,"Déconnecté",Toast.LENGTH_SHORT).show();
        Log.d("Status device","On deconnect le device");
    }
}
