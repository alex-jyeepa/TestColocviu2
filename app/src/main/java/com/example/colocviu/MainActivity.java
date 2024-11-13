package com.example.colocviu;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    Button press1, press2, show;
    EditText text1, text2;

    private PracticalTest01BroadcastListener broadcastReceiver = new PracticalTest01BroadcastListener();
    private IntentFilter intentFilter = new IntentFilter();

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "The activity returned OK", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "The activity returned with a result different than OK", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        show = findViewById(R.id.showbutton);
        press1 = findViewById(R.id.btn1);
        press2 = findViewById(R.id.btn2);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);

        if (savedInstanceState != null) {
            if (savedInstanceState.getString("text1") != null) {
                text1.setText(savedInstanceState.getString("text1"));
            }
            if (savedInstanceState.getString("text2") != null) {
                text2.setText(savedInstanceState.getString("text2"));
            }
        }

        press1.setOnClickListener(v -> text1.setText(String.valueOf(Integer.parseInt(text1.getText().toString()) + 1)));

        press2.setOnClickListener(v -> text2.setText(String.valueOf(Integer.parseInt(text2.getText().toString()) + 1)));

        show.setOnClickListener(v -> {
            int sum = Integer.parseInt(text1.getText().toString()) + Integer.parseInt(text2.getText().toString());
            Intent intent = new Intent(getApplicationContext(), SecondaryActivity.class);
            intent.putExtra("sum", sum);
            activityResultLauncher.launch(intent);
        });

        Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
        // start the service  and give it the arithmetical mean and geometric mean of the numbers
        intent.setAction(Constants.ACTION_TARGET);
        intent.putExtra(Constants.ARITHMETIC_MEAN, Integer.parseInt(text1.getText().toString()));
        intent.putExtra(Constants.GEOMETRIC_MEAN, Math.sqrt(Integer.parseInt(text1.getText().toString()) * Integer.parseInt(text2.getText().toString())));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else {
            startService(intent);
        }


        intentFilter.addAction(Constants.ACTION_TARGET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intentFilter.setPriority(100);
            registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("text1", text1.getText().toString());
        savedInstanceState.putString("text2", text2.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            unregisterReceiver(broadcastReceiver);
        }
    }
}