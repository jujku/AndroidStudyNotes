package com.example.servicetest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class DownloadTask extends AsyncTask<Void,Integer,Boolean> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();//显示进度条对话框
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            while(true){
                int downloadPercent = doDownload(); //一个虚构的方法
                publishProgress(downloadPercent);
                if(downloadPercent >= 100){
                    break;
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //在这里更新下载进度
        progressDialog.setMessage("Downloaded" + values[0] + "%");

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss(); //关闭进度对话框

        //提示下载结果
        if(result){
            Toast.makeText(context,"下载成功",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(context,"下载失败",Toast.LENGTH_SHORT).show();
        }
    }
}
