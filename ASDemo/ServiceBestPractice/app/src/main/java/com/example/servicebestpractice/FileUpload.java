package com.example.servicebestpractice;

import android.util.Log;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;

public class FileUpload {

    private final OkHttpClient client = new OkHttpClient();

    public void uploadFile(String filePath, String url) throws IOException {
        File file = new File(filePath);

        // 构建请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        // 构建请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 执行请求
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 上传成功
                System.out.println("File uploaded successfully: " + response.body().string());
            } else {
                // 上传失败
                System.out.println("File upload failed: " + response.message());
            }
        }
    }
}
