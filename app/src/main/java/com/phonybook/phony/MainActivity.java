package com.phonybook.phony;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    public Uri imageUri;

    RecyclerView contactView;
    View subView;
    private String[] cameraPermissions;
    private String[] storagePermissions;


    FloatingActionButton fab_add, fab_dial;
    Button btn_changetheme;
    RecyclerViewAdapter mAdapter;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_main);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        contactView = findViewById(R.id.rc_contactview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        contactView.setLayoutManager(linearLayoutManager);
        contactView.setHasFixedSize(true);
        refresh(true);
        fab_add = findViewById(R.id.fab_add);
        fab_dial = findViewById(R.id.fab_dial);
        btn_changetheme = findViewById(R.id.btn_changetheme);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDialog();
            }
        });
        fab_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DialContact.class);
                startActivity(intent);
            }
        });
        btn_changetheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }

                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.this.getClass()));
            }
        });
    }

    private void refresh(boolean order) {
        List<ContactModel> allContacts = dataBaseHelper.selectAll();
        allContacts.sort(new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel o1, ContactModel o2) {
                return (order ? 1 : -1) * o1.getName().compareTo(o2.getName());
            }
        });
            contactView.setVisibility(View.VISIBLE);
            mAdapter = new RecyclerViewAdapter(this, allContacts);
            contactView.setAdapter(mAdapter);
    }

    private void addTaskDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        subView = layoutInflater.inflate(R.layout.activity_addto_contact, null);
        final EditText nameField = subView.findViewById(R.id.et_name);
        final EditText phonenumField = subView.findViewById(R.id.et_phonenum);
        final EditText emailField = subView.findViewById(R.id.et_email);
        final CheckBox workcontactField = subView.findViewById(R.id.cb_workcon);
        final ImageView imageField = subView.findViewById(R.id.iv_photo);
        imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_new_contact);
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imageField.getTag().equals("")) {
                    imageUri = Uri.parse("android.resource://com.phonybook.phony/" +R.drawable.ic_action_person);
                    imageField.setImageURI(imageUri);
                    imageField.setTag(imageUri);
                }
                final String name = nameField.getText().toString();
                final String phonenum = phonenumField.getText().toString();
                final String email = emailField.getText().toString();
                final boolean workcon = workcontactField.isChecked();
                final String photo = imageField.getTag().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phonenum)) {
                    Toast.makeText(MainActivity.this, R.string.invalid_add, Toast.LENGTH_SHORT).show();
                } else {
                    ContactModel newContact = new ContactModel(name, phonenum, email, photo, workcon);
                    dataBaseHelper.addOne(newContact);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, R.string.task_cancelled, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void editTaskDialog(final ContactModel contactModel) {
        LayoutInflater inflater = LayoutInflater.from(this);
        subView = inflater.inflate(R.layout.activity_addto_contact, null);
        final EditText nameField = subView.findViewById(R.id.et_name);
        final EditText phonenumField = subView.findViewById(R.id.et_phonenum);
        final EditText emailField = subView.findViewById(R.id.et_email);
        final ImageView imageField = subView.findViewById(R.id.iv_photo);
        final CheckBox workcontactField = subView.findViewById(R.id.cb_workcon);
        if (contactModel == null) {
            return;
        }
        nameField.setText(contactModel.getName());
        phonenumField.setText(contactModel.getPhonenum());
        emailField.setText(contactModel.getEmail());
        imageField.setImageURI(imageUri);
        workcontactField.setChecked(contactModel.isWork());

        imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_contact);
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imageField.getTag().equals("")) {
                    imageUri = Uri.parse("android.resource://com.phonybook.phony/" +R.drawable.ic_action_person);
                    imageField.setImageURI(imageUri);
                    imageField.setTag(imageUri);
                }
                final String name = nameField.getText().toString();
                final String phonenum = phonenumField.getText().toString();
                final String email = emailField.getText().toString();
                final String photo = imageField.getTag().toString();
                final boolean workcon = workcontactField.isChecked();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phonenum)) {
                    Toast.makeText(MainActivity.this, R.string.invalid_edit, Toast.LENGTH_SHORT).show();
                } else {
                    dataBaseHelper.updateOne(new ContactModel(contactModel.getId(), name, phonenum, email, photo, workcon));
                    //Refresh the activity
                    MainActivity.this.finish();
                    MainActivity.this.startActivity(MainActivity.this.getIntent());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, R.string.task_cancelled, Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    public boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                storagePermissions, STORAGE_REQUEST_CODE);
    }

    public boolean checkCameraPermissions() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    public void imagePickDialog() {
        String[] options = {getString(R.string.camera), getString(R.string.gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_image);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Camera selected
                if (which == 0) {
                    if (!checkCameraPermissions()) {
                        requestCameraPermission();
                    } else {
                        // Permission granted
                        pickFromCamera();
                    }
                } else if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickFromGallery();
                }
            }
        });

        builder.create().show();
    }

    public void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    public void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Image_Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Image_Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }


    public void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        if (id == R.id.action_sort) {
            sortOptionsDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void sortOptionsDialog() {
        String[] options = {"Ascending", "Descending"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    refresh(which == 0);
            }
        }).create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, R.string.camera_storage_require_permission, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, R.string.storage_permission_require, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                assert result != null;
                imageUri = result.getUri();
                ImageView photoSet = subView.findViewById(R.id.iv_photo);
                photoSet.setImageURI(imageUri);
                photoSet.setTag(imageUri);
            }
        } else {
            Toast.makeText(this, getString(R.string.error_crop) + requestCode + " " + resultCode, Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataBaseHelper != null) {
            dataBaseHelper.close();
        }
    }

}