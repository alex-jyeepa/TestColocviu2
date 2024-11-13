package com.example.colocviu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class PracticalTest01BroadcastListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int sum = intent.getIntExtra(Constants.ARITHMETIC_MEAN, -1);
        int geom = intent.getIntExtra(Constants.GEOMETRIC_MEAN, -1);
        if (Objects.equals(intent.getAction(), Constants.ACTION_TARGET))
            Log.d(Constants.TAG, "Broadcast received with sum: " + sum + " and geom: " + geom);
    }
}
