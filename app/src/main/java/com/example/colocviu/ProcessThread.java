package com.example.colocviu;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ProcessThread extends Thread {

        private Context context;
        int sum, geom;

        public ProcessThread(Context context, int sum, int geom) {
            this.context = context;
            this.sum = sum;
            this.geom = geom;
        }

        @Override
        public void run() {
            Log.d(Constants.TAG, "Thread.run() was invoked, PID: " + android.os.Process.myPid() + " TID: " + android.os.Process.myTid());
            while(true){
                sendMessage();
                sleep();
            }
        }

        private void sleep() {
            try {
                Thread.sleep(Constants.SLEEP_TIME);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        private void sendMessage() {
            Intent intent = new Intent();
            intent.setAction(Constants.ACTION_TARGET);
            intent.putExtra(Constants.ARITHMETIC_MEAN, sum);
            intent.putExtra(Constants.GEOMETRIC_MEAN, geom);
            Log.d(Constants.TAG, "Sending broadcast intent with sum: " + sum + " and geom: " + geom);
            context.sendBroadcast(intent);
        }
}
