package com.example.androidlearn;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class camActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setListener();

    }

    private void setView(){
        setContentView(R.layout.activity_cam);
    }

    private void setListener(){
        View btn_cam = findViewById(R.id.btn_cam);
        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateCamera();
            }
        });

        View btn_video = findViewById(R.id.btn_video);
        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateRecording();
            }
        });
    }

    // using camera
    private void activateCamera(){
        // create the intent for capturing the image
        Intent aCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a file to save image
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//        Log.e("storage URI",fileUri.toString());
        aCam.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(aCam, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    // using video recording
    private void activateRecording(){
        // create the intent for capturing the video
        Intent aCam = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // create a file to save image
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
//        Log.e("storage URI",fileUri.toString());
        aCam.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // set the video quality
        aCam.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        startActivityForResult(aCam, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                Log.e("success saved","photo success");
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Log.e("cancelled event","user has cancelled this event");
            } else {
                // Image capture failed, advise user
                Log.e("failure to capture","failed");
            }
        }
//            for video recording purpose
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        //this method make the file under the top level of the phone directory，
//       some cases not seen in the photo library directly
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new Date().toString();//new DateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
//                    "IMG.jpg");
            Log.e("file path:", mediaFile.getAbsolutePath());
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
