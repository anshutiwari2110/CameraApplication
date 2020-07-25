package com.example.cameraapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FrameLayout cameraFrame;
    private Camera camera;
    private boolean flipCamera;
    private ArrayList<Bitmap> imageArrayList;
    private RecyclerView mRcPreview;
    private GalleryAdapter adapter;
    Image image;
    private ImageView mIvPreviewImage;
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mIvPreviewImage.setImageBitmap(bitmap);
            imageArrayList.add(bitmap);
            camera.startPreview();
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraFrame = findViewById(R.id.fl_capture_screen);
        ImageView mIvCapture = findViewById(R.id.iv_capture);
        ImageView mIvFlipCamera = findViewById(R.id.iv_flip_camera);
        mIvPreviewImage = findViewById(R.id.iv_preview_image);
        mRcPreview = findViewById(R.id.rc_preview);
        mRcPreview.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false));
        adapter = new GalleryAdapter(MainActivity.this);
        mRcPreview.setAdapter(adapter);

        mIvCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

        mIvFlipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.stopPreview();
                boolean currentMode = flipCamera == true ? false : true;
                initiateCamera(currentMode);
            }
        });

        mIvPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ImageGallery.class));
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1001);
            } else {
                initiateCamera(true);
            }
        } else {
            initiateCamera(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateCamera(true);
            } else {
                Toast.makeText(MainActivity.this, "User Denied Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initiateCamera(boolean flip) {
        camera = null;
        try {
            flipCamera = flip;
            int cameraID;
//           String flash;
            if (flip) {
                cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
//               flash = Camera.Parameters.FLASH_MODE_ON;
            } else {
                cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
            camera = Camera.open(cameraID);
            CameraSurfaceView surfaceView = new CameraSurfaceView(MainActivity.this, camera);
            cameraFrame.addView(surfaceView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}