package com.example.telephonemanagerbroadcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView callerText;
    LocalBroadcastManager manager;
    String caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callerText = findViewById(R.id.callerText);
        manager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("my.result.receiver");
        manager.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String incomingNumber = intent.getStringExtra("inComingNumber");
            System.out.println("incomingNumber: " + incomingNumber);
            matchContact(incomingNumber);
        }
    };

    private void matchContact(String number) {
        ArrayList<String> nameFromDb = new ArrayList<>();
        ArrayList<String> numberFromDb = new ArrayList<>();
        HashMap<String, String> contactInfo = new HashMap<>();

        SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("com.example.telephonemanagerbroadcast", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS phoneBook (name VARCHAR, number VARCHAR)");
        sqLiteDatabase.execSQL("INSERT INTO phoneBook(name,number) VALUES ('Jason','055555555555')");
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM phoneBook", null);
        int nameIx = cursor.getColumnIndex("name");
        int numberIx = cursor.getColumnIndex("number");
        while (cursor.moveToNext()) {
            String nameDb = cursor.getString(nameIx);
            String numberDb = cursor.getString(numberIx);
            nameFromDb.add(nameDb);
            numberFromDb.add(numberDb);
            contactInfo.put(numberDb, nameDb);
        }
        cursor.close();
        for (String s : numberFromDb) {
            if (s.matches(number)) {
                caller = (contactInfo.get(number));
                callerText.setText("Caller" + caller);
            } else {
                caller = "";
            }
        }
    }
}