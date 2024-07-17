package com.example.cameraalbumtest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.Manifest;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher,openAlbumLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    public static final int CHOOSE_PHOTO = 2;

    public static final int TAKE_PHOTO = 1;
    private ImageView picture;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    takePhoto();
                }


            }
        });

        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);

        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                // 创建一个 ActivityResultContract 实例，用于启动活动并返回结果。
                new ActivityResultCallback<ActivityResult>() {
                    // 实现 ActivityResultCallback 接口，用于处理返回的结果。
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            try{
                                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                                picture.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        openAlbumLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                // 创建一个 ActivityResultContract 实例，用于启动活动并返回结果。
                new ActivityResultCallback<ActivityResult>() {
                    // 实现 ActivityResultCallback 接口，用于处理返回的结果。
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                                handleImageOnkitkat(result.getData());
                        }
                    }
                }
        );
    }
    private void handleImageOnkitkat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        //是不是document类型的uri
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的id 则通过document id来处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

            } else if("com.android.prociders.downloads.document".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
             //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }
    private void takePhoto(){
        File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(MainActivity.this,"com.example.cameraalbumtest.fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        activityResultLauncher.launch(intent);
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        openAlbumLauncher.launch(intent);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过uri和selection来获取真是的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }
}