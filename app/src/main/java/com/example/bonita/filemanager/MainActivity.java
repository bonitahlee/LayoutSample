package com.example.bonita.filemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * FileListFragment를 뿌려줌
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // attach FileListFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_main, new FileListFragment()).commit();
    }
}
