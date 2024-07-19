package com.example.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.servicebestpractice.imp.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;
    private static String TAG = "DownloadTask";

    public DownloadTask(DownloadListener listener){
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;

        try{
           long downloadedLength = 0; //记录已下载的文件长度
           String downloadUrl = params[0];
           String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            Log.d(TAG, "doInBackground: " + fileName);
           String directory = Environment.getExternalStoragePublicDirectory
                   (Environment.DIRECTORY_DOWNLOADS).getPath();
           file = new File(directory + fileName);
           long contentLength = getContentLength(downloadUrl);
           if(file.exists()){
               downloadedLength = file.length();
               if(downloadedLength == contentLength){
                   int fileSuffix = 1;
                   String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                   String extension = fileName.substring(fileName.lastIndexOf('.'));
                   while (file.exists() && downloadedLength == contentLength) {
                       Log.d(TAG, "doInBackground: " + "改名");
                       fileSuffix++;
                       fileName = baseName + "(" + fileSuffix + ")" + extension;
                       file = new File(directory, fileName);
                       downloadedLength = file.length();
                   }
               }
           }

           if(contentLength == 0){
               Log.d(TAG, "doInBackground: "+ contentLength);
               return TYPE_FAILED;
           } else if (contentLength == downloadedLength){
//               已下载字节和文件粽子节相等，说明已经下载完成
               return TYPE_SUCCESS;
           }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //断点下载，指定从哪个字节开始下载
                    .addHeader("RANGE","bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            Log.d(TAG, "doInBackground: " + response);
            if(response != null){
                Log.d(TAG, "doInBackground: " + downloadedLength);
                is = response.body().byteStream();

                Log.d(TAG, "doInBackground: " + file);
                savedFile = new RandomAccessFile(file,"rw");
                Log.d(TAG, "doInBackground: " + "download");
                savedFile.seek(downloadedLength); //跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1){
                    if(isCanceled){
                        return TYPE_CANCELED;
                    }else if(isPaused){
                        return TYPE_PAUSED;
                    }else{
                        total += len;
                        savedFile.write(b,0,len);
                        //计算已下载的百分比downloadedLength
//                        Log.d(TAG, "doInBackground: " + total + " " +  + " " + contentLength + " " + (int) ((total + downloadedLength)*100 / contentLength ));
                        int progress = (int) ((total + downloadedLength)*100 / contentLength );
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(is != null){
                    is.close();
                }
                if(savedFile != null){
                    savedFile.close();
                }
                if(isCanceled && file != null){
                    file.delete();
                }
            }catch (Exception e){
                Log.d(TAG, "doInBackground: " + "catch");
                e.printStackTrace();
            }
        }
        Log.d(TAG, "doInBackground: " + "fail");
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        super.onPostExecute(status);
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }
    public void pauseDownload(){
        isPaused = true;
    }
    public void cancelDownload(){
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Log.d(TAG, "getContentLength: " + downloadUrl);
        Response response = client.newCall(request).execute();
        if(response != null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }
}
