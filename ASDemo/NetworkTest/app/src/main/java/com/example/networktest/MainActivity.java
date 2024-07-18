package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequest = (Button) findViewById(R.id.send_request_button);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestWithMyHttp();

            }
        });

    }
    private void sendRequestWithMyHttp(){
        HttpUtil.sendHttpRequest("https://jsonplaceholder.typicode.com/posts/1", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                parseJSONWithGSON(response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sendRequestWithOkHttp(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://jsonplaceholder.typicode.com/posts/1")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
//                    showResponse(responseData);
                    parseJSONWithGSON(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
//        List<Item> itemList = gson.fromJson(jsonData,new TypeToken<List<Item>>(){}.getType());
        Item item = gson.fromJson(jsonData,Item.class);
//        for(Item item:itemList){
            Log.d(TAG, "id is " + item.getId());
            Log.d(TAG, "title is " + item.getTitle());
            Log.d(TAG, "body is " + item.getBody());
//        }
    }

    private void parseJSONWithJSONObject(String jsonData){
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String body = jsonObject.getString("body");
                Log.d(TAG, "id is " + id);
                Log.d(TAG, "title is " + title);
                Log.d(TAG, "body is " + body);
            }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseXMLWithPull(String xmlData){
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String to = "";
            String from = "";
            String heading = "";
            String body = "";
            while(eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch(eventType){
                    case XmlPullParser.START_TAG:{
                        if("to".equals(nodeName)){
                            to = xmlPullParser.nextText();
                        }else if("from".equals(nodeName)){
                            from = xmlPullParser.nextText();
                        }else if("heading".equals(nodeName)){
                            heading = xmlPullParser.nextText();
                        } else if ("body".equals(nodeName)) {
                            body = xmlPullParser.nextText();
                        }
                        break;
                   }
                //完成解析某个节点
                    case XmlPullParser.END_TAG:{
                    if("note".equals(nodeName)){
                        Log.d(TAG, "id is "+ to);
                        Log.d(TAG, "name is "+ from);
                        Log.d(TAG, "version is "+ heading);
                        Log.d(TAG, "body is "+ body);
                    }
                    break;
                    }
                    default:
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            }
    }
    private void sendRequestWithHttpURLConnection(){
        //开启线程来发起网络请求
        new Thread(new Runnable(){
            @Override
            public void run(){
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    // POST
//                    connection.setRequestMethod("POST");
//                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("username=admin&password=123456");


                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null ){
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch(Exception e){
                    e.printStackTrace();
                } finally {
                    if(reader != null){
                        try {
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                responseText.setText(response);
            }
        });
    }
}