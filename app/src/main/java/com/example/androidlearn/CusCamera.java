package com.example.androidlearn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.*;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.example.androidlearn.utils.getOutputMediaFile;

public class CusCamera extends AppCompatActivity {

    private Camera mCamera;
    private Camera.Parameters parameters;
    private camPreview mPreview;
    private String TAG = "output:";
    private boolean touched = false;
    private byte[] tmpdata;
    private AlertDialog confirm;
    private AlertDialog.Builder builder;
    // normal status
    static final private int BACK_CAMERA = 0;
    // self photo
    static final private int FRONT_CAMERA = 1;
    static private int CAMERA_STATUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check the existence of the hardware
        if (!checkCamHardware(CusCamera.this)){
            // navigate back to menu automatically
            finish();
        }

        setView();
        setListener();

        builder = new AlertDialog.Builder(CusCamera.this);
        Log.e(TAG,"builder built");
        builder.setMessage(R.string.cam_confirm)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id){
                    savePhoto();
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mCamera.startPreview();
                }
            });
    }

    private boolean checkCamHardware(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera.CameraInfo getCamInfo(int cameraId){
        Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,cameraInfo);
        return cameraInfo;
    }

    public static Camera getCameraInstance(int cam_status){
        Camera c = null;
        try{
            c = Camera.open(cam_status);
            CAMERA_STATUS = cam_status;
        }catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }

    private void setView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide the action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cus_camera);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // get a camera instance
        mCamera = getCameraInstance(BACK_CAMERA);
        setCameraProperties();
        // Create our Preview and set the content to the activity
        mPreview = new camPreview(CusCamera.this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        setCameraDisplayOrientation(CusCamera.this,0,mCamera);
        preview.addView(mPreview);

        // find and add the switch
//        Switch cam = findViewById(R.id.switch_cam);
        // start the preview
        mCamera.startPreview();
    }

    private void setListener(){
        FrameLayout preview = findViewById(R.id.camera_preview);

        preview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e(TAG,"touched!");
                mCamera.takePicture(null,null, mPicture);
                touched = true;
                return false;
            }
        });

        Switch cam_switch = findViewById(R.id.switch_cam);

        cam_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "swiched!");
                mCamera.stopPreview();
                mCamera.release();
                mCamera = getCameraInstance(CAMERA_STATUS==FRONT_CAMERA?BACK_CAMERA:FRONT_CAMERA);
                mPreview = new camPreview(CusCamera.this, mCamera);
                FrameLayout preview = findViewById(R.id.camera_preview);
                preview.clearChildFocus(mPreview);
                setCameraDisplayOrientation(CusCamera.this,CAMERA_STATUS,mCamera);
                preview.addView(mPreview);
                mCamera.startPreview();
            }
        });

    }

    /*
    * set the cam's parameters
    * */
    private void setCameraProperties(){
        parameters = mCamera.getParameters();
        parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
        parameters.setPictureFormat(PixelFormat.JPEG);
        mCamera.setParameters(parameters);

    }

    /*
    * save the photo after confirmed
    * */
    private void savePhoto(){
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions: " );
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(tmpdata);
            fos.close();
            Log.e(TAG,"write photo success!");
            Toast.makeText(CusCamera.this, "photo saved!",Toast.LENGTH_SHORT)
                    .show();
            mCamera.startPreview();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"released");
        mCamera.stopPreview();
        mCamera.release();

    }

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            tmpdata = data;
            // stop the preview
            mCamera.stopPreview();
//            Toast.makeText(CusCamera.this,"Save this photo XD?",Toast.LENGTH_LONG)
//                    .show();
            // create and show the dialog
            confirm = builder.create();
            confirm.show();

            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
//            Toast.makeText(CusCamera.this, "builder created", Toast.LENGTH_SHORT)
//                .show();
        }
    };

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

}
