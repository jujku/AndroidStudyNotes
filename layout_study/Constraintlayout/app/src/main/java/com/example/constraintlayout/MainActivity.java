package com.example.constraintlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner layoutSpinner = findViewById(R.id.layoutSpinner);
        List<String> layoutNames = getLayoutResources();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, layoutNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutSpinner.setAdapter(adapter);

        layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String layoutName = layoutNames.get(position);
                int layoutId = getResources().getIdentifier(layoutName, "layout", getPackageName());

                if (layoutId != 0) {
                    ViewGroup rootView = findViewById(R.id.rootView); // rootView 是你的主布局的容器
                    rootView.removeAllViews();
                    View.inflate(MainActivity.this, layoutId, rootView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

    }

    private List<String> getLayoutResources() {
        Field[] fields = R.layout.class.getDeclaredFields();
        List<String> layoutNames = new ArrayList<>();
        for (Field field : fields) {
            layoutNames.add(field.getName());
        }
        return layoutNames;
    }
}