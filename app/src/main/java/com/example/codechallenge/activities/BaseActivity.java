package com.example.codechallenge.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

/*
     * Base Activity make sure that every activity in the project have same basic behaviour.
     * Like: On Internet Unavailability show alert Or Load local DB data
*/

public abstract class BaseActivity extends AppCompatActivity {

    /* Base Activity is abstract because we're binding child classes to update its views on Network state change by
    * overriding onNetworkStartChange method*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();//Registering Network State Change Broadcast Receiver
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();//Unregistering Network State Change Broadcast Receiver
    }

    private void registerReceiver(){
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, intentFilter);
    }

    private void unregisterReceiver(){
        unregisterReceiver(networkReceiver);
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    /*In ON state passing 0 argument to onNetworkStartChange method*/
                    onNetworkStartChange(0);//ON
                } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                    /*In OFF state passing 1 argument to onNetworkStartChange method*/
                    onNetworkStartChange(1);//OFF
                }
            }
        }
    };

    //Child classes will implement this method to see the change in Internet state
    protected abstract void onNetworkStartChange(int state);
}
