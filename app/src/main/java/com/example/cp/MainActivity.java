package com.example.cp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_SMS = 100;
    private RecyclerView smsRecyclerView;
    private SmsAdapter smsAdapter;
    private List<SMS> smsList;
    private EditText phoneNumberEditText, messageEditText;
    private Button sendButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        smsRecyclerView = findViewById(R.id.sms_recyclerview);

        smsList = new ArrayList<>();
        smsAdapter = new SmsAdapter(smsList);
        smsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        smsRecyclerView.setAdapter(smsAdapter);

        databaseHelper = new DatabaseHelper(this);

        sendButton.setOnClickListener(v -> sendSms());

        requestSmsPermissions();
    }

    private void requestSmsPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS
            }, PERMISSIONS_REQUEST_SMS);
        } else {
            loadSmsMessages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSmsMessages();
            }
        }
    }

    private void loadSmsMessages() {
        smsList.clear();
        smsList.addAll(databaseHelper.getAllSms());
        smsAdapter.notifyDataSetChanged();
    }

    private void sendSms() {
        String phoneNumber = phoneNumberEditText.getText().toString();
        String message = messageEditText.getText().toString();

        if (!phoneNumber.isEmpty() && !message.isEmpty()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Đã gửi tin nhắn", Toast.LENGTH_SHORT).show();

            SMS sentSms = new SMS(phoneNumber, message, getCurrentFormattedDate());
            smsList.add(sentSms);
            smsAdapter.notifyItemInserted(smsList.size() - 1);
            databaseHelper.addSms(sentSms);

            phoneNumberEditText.setText("");
            messageEditText.setText("");
        } else {
            Toast.makeText(this, "Xin nhập số điện thoại và tin nhắn", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}
