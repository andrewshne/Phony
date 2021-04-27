package com.phonybook.phony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class DialContact extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST = 1;

    YouTubePlayerView youTubePlayerView;
    View subView;
    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_call, btn_clear;
    TextView tv_dialview;

    String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_contact);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED); {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST);
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.dialer);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        tv_dialview = findViewById(R.id.tv_dialview);
        btn_0 = findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(this);
        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btn_0: {
                number = number + "0";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_1: {
                number = number + "1";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_2: {
                number = number + "2";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_3: {
                number = number + "3";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_4: {
                number = number + "4";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_5: {
                number = number + "5";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_6: {
                number = number + "6";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_7: {
                number = number + "7";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_8: {
                number = number + "8";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_9: {
                number = number + "9";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_clear: {
                number = "";
                tv_dialview.setText(number);
                break;
            }
            case R.id.btn_call: {
                if (tv_dialview.getText().equals("77744455")) {
                    videoAlertDialog();
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }
    }

    public void videoAlertDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        subView = layoutInflater.inflate(R.layout.video_layout, null);
        youTubePlayerView = subView.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.found_easteregg);
        builder.setView(subView);
        builder.create();
        builder.setNegativeButton(R.string.go_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DialContact.this, R.string.rick_roll, Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}