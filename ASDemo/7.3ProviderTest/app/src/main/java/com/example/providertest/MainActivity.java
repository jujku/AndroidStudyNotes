package com.example.providertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String newId;
    private static String TAG = "onCreate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button query = (Button) findViewById(R.id.query_button);
        Button delete = (Button) findViewById(R.id.delete_button);
        Button insert = (Button) findViewById(R.id.insert_button);
        Button update = (Button) findViewById(R.id.update_button);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                ContentValues values = new ContentValues();
                values.put("name","a clash of kings");
                values.put("author","george martin");
                values.put("pages",1040);
                values.put("price",22.85);
                Uri newUri = getContentResolver().insert(uri,values);
                newId = newUri.getPathSegments().get(1);
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                if(cursor != null){
                    while (cursor.moveToFirst()){
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, "onClick: name "+ name );
                        Log.d(TAG, "onClick: author " + author);
                        Log.d(TAG, "onClick: pages " + pages);
                        Log.d(TAG, "onClick: price " + price);
                    }
                    cursor.close();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name","a storm of swords");
                values.put("pages",1216);
                values.put("prive",24.05);
                getContentResolver().update(uri,values,null,null);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book/" + newId);
                getContentResolver().delete(uri,null,null);
            }
        });

    }
}