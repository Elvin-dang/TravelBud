package com.example.travelbud;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class BackgroundServiceProvider extends Worker {
    ArrayList<String> syncItems;
    StorageReference storageReference;
    Context context;
    String APP_TAG;

    ArrayList<String> items;

    public BackgroundServiceProvider(@NonNull Context context,
                                     @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        APP_TAG = data.getString("tag");
        readItemsFromFile();

        return Result.success();

    }

    private void readItemsFromFile() {

        try {
            // Run a task specified by a Runnable Object asynchronously.
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    //read items from database
                    File filesDir = context.getFilesDir();
                    //prepare a file to read the data
                    File todoFile = new File(filesDir, "toUpload.txt");
                    //if file does not exist, create an empty list
                    if (!todoFile.exists()) {
                        items = new ArrayList<String>();
                    } else {
                        try {
                            //read data and put it into the ArrayList
                            items = new ArrayList<String>(FileUtils.readLines(todoFile));
                            syncItems = items;
                            for (String item : items) {
                                String[] x = item.split(" ");
                                firbaseOnlineStorage(x[0], x[1], x[2], item);
                            }
                        } catch (IOException ex) {
                            items = new ArrayList<String>();
                        }
                    }
                    System.out.println("I'll run in a separate thread than the main thread.");
                }
            });
            // Block and wait for the future to complete
            future.get();
        } catch (Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }


    }


    private void firbaseOnlineStorage(String name, String tripKey, String billKey, String item) {
        Uri fileUri = getFileUri(name, 0);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference =
                FirebaseStorage.getInstance().getReference("bills/" + tripKey + "/" + name);

        storageReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        syncItems.remove(item);
                        Toast.makeText(context, "Media Backup Success", Toast.LENGTH_SHORT).show();
                        addIntoFirebaseStore(name, tripKey, billKey, uri.toString());
                        saveItemsToFile();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "URL adding fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Firebase Upload fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveItemsToFile() {
        File filesDir = context.getFilesDir();
        File todoFile = new File(filesDir, "toUpload.txt");
        try {
            FileUtils.writeLines(todoFile, syncItems);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void addIntoFirebaseStore(String name, String tripKey, String billKey, String url) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bills");
        HashMap<String, Object> map = new HashMap<>();
        map.put("url", url);
        reference.child(tripKey + "/" + billKey).updateChildren(map);
    }

    public Uri getFileUri(String fileName, int type) {
        Uri fileUri = null;

        try {
            String typestr = "Images";
            if (type == 1) {
                typestr = "Videos";
            }

            File mediaStorageDir = new
                    File(context.getExternalFilesDir(Environment.getExternalStorageDirectory().toString()), APP_TAG + typestr);

            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            File file = new File(mediaStorageDir, fileName);
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
}

