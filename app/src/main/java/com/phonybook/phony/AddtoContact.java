package com.phonybook.phony;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class AddtoContact extends AppCompatActivity {
    //Refrences to edittext, buttons, switches and scrollviews
    EditText et_name, et_phonenum, et_email;
    Button btn_add, btn_cancel;
    SwitchCompat sw_family;
    int id;

    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_contact);

        et_name = findViewById(R.id.et_name);
        et_phonenum = findViewById(R.id.et_phonenum);
        et_email = findViewById(R.id.et_email);

        Intent getIntent = getIntent();
        id = getIntent.getIntExtra("id", -1);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ContactModel contactModel;

                try {
                    //contactModel = new ContactModel(-1, et_name.getText().toString(), et_phonenum.getText().toString(), et_email.getText().toString(), sw_family.isChecked());
                    dataBaseHelper = new DataBaseHelper(AddtoContact.this);
                    //dataBaseHelper.addOne(contactModel);
                }
                catch (Exception e){
                    Toast.makeText(AddtoContact.this, "Error on input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddtoContact.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }
}