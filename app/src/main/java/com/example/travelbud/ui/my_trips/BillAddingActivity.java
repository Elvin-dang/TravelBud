package com.example.travelbud.ui.my_trips;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelbud.BackgroundServiceProvider;
import com.example.travelbud.PermissionSet;
import com.example.travelbud.R;

import com.example.travelbud.model.BillModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

public class BillAddingActivity extends AppCompatActivity {

    TextView description, amount;
    Button saveBtn,selectBtn,captureBtn;
    ImageView billImage;
    PermissionSet permissionSet;
    Uri imageUri;
    String photoFileName = "photo.jpg";
    public static final String APP_TAG = "TravelBug";
    private static final String JOB_TAG = "media_sync";
    private File file;

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    //request codes
    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 101;
    private static final int MY_PERMISSIONS_ACCESS_MEDIA = 102;

    public static final int JOB_INITIATE_DURATION = 30;
    public static final int JOB_REPEAT_INTERVAL = 12;

    ArrayList<String> items;
    String tripKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_adding);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Bill");

        permissionSet = new PermissionSet(this);

        description = findViewById(R.id.bill_description_txt);
        amount = findViewById(R.id.bill_amount_txt);
        billImage  = findViewById(R.id.bill_image);
        //selectBtn = findViewById(R.id.bill_select);
        saveBtn = findViewById(R.id.bill_add_btn);
        captureBtn = findViewById(R.id.bill_capture_btn);


        Intent intent = getIntent();
        tripKey = intent.getExtras().getString("trip_key");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBill(tripKey);
                finish();
            }
        });

//        selectBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTakePhotoClick(v);
            }
        });

        callBackgroundService();

    }

    private void callBackgroundService() {
        Data data = new Data.Builder()
                .putString("tag",APP_TAG)
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(BackgroundServiceProvider.class, JOB_REPEAT_INTERVAL, TimeUnit.HOURS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag(JOB_TAG)
                .setInitialDelay(JOB_INITIATE_DURATION,TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(periodicWorkRequest);

    }


    private void readItemsFromFile(){
        //retrieve the app's private folder.
        //this folder cannot be accessed by other apps
        File filesDir = getFilesDir();
        //prepare a file to read the data
        File todoFile = new File(filesDir,"toUpload.txt");
        //if file does not exist, create an empty list
        if(!todoFile.exists()){
            items = new ArrayList<String>();
        }else{
            try{
                //read data and put it into the ArrayList
                items = new ArrayList<String>(FileUtils.readLines(todoFile));
            }
            catch(IOException ex){
                items = new ArrayList<String>();
            }
        }
    }

    private void saveItemsToFile(){
        File filesDir = getFilesDir();
        //using the same file for reading. Should use define a global string instead.
        File todoFile = new File(filesDir,"toUpload.txt");
        try{
            //write list to file
            FileUtils.writeLines(todoFile,items);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public Uri getFileUri(String fileName, int type) {
        Uri fileUri = null;

        try {
            String typestr = "Images"; //default to images type
            if (type == 1) {
                typestr = "Videos";
            }

            // Get safe media storage directory depending on type
            File mediaStorageDir = new
                    File(getExternalFilesDir(Environment.getExternalStorageDirectory().toString()), APP_TAG+typestr);


            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            // Create the file target for the media based on filename
            file = new File(mediaStorageDir, fileName);
            // Wrap File object into a content provider, required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-withIntents#sharing-files-with-api-24-or-higher
            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(
                        this.getApplicationContext(),
                        "com.example.travelbud.fileprovider", file);
            } else {
                fileUri = Uri.fromFile(mediaStorageDir);
            }
        } catch (Exception ex) {
            Log.d("getFileUri", ex.getStackTrace().toString());
        }
        return fileUri;
    }

    public void onTakePhotoClick(View v) {
        // Check permissions
        if (!permissionSet.checkPermissionForCamera()
                || !permissionSet.checkPermissionForExternalStorage()) {
            permissionSet.requestPermissionForCamera();

        } else {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // set file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            photoFileName = "IMG_" + timeStamp + ".jpg";
            // Create a photo file reference
            Uri file_uri = getFileUri(photoFileName, 0);

            // Add extended data to the intent
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
            //intent.putExtra("file_location",file_uri);
            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==MY_PERMISSIONS_ACCESS_MEDIA && data != null && data.getData() != null){
           imageUri = data.getData();
           billImage.setImageURI(imageUri);
            String[] filename = file.getAbsolutePath().split("/");

        }else if (requestCode == MY_PERMISSIONS_REQUEST_OPEN_CAMERA) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                // Load the taken image into a preview
                billImage.setImageBitmap(takenImage);


            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken AAA!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addBill(String tripKey) {

        reference = FirebaseDatabase.getInstance().getReference("bills").child(tripKey).push();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        BillModel bill = new BillModel(description.getText().toString(),Double.parseDouble(amount.getText().toString()),
                firebaseUser.getUid());
        reference.setValue(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Upload to local storage
                    String[] filename = file.getAbsolutePath().split("/");
                    readItemsFromFile();
                    items.add(new String (filename[filename.length - 1])+" "+tripKey+" "+reference.getKey());
                    saveItemsToFile();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}