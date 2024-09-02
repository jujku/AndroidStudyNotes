package com.example.a2demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button undo,redo;
    MyView myView;
    private String TAG ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        undo = (Button) findViewById(R.id.undo);
        redo = (Button) findViewById(R.id.redo);
        myView = (MyView) findViewById(R.id.myView);

        undo.setOnClickListener(this);
        redo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.undo:
                myView.undo();
                break;
            case R.id.redo:
                myView.redo();
                break;
        }
    }
}