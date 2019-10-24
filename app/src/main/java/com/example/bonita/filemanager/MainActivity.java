package com.example.bonita.filemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/**
 * FileListFragment를 뿌려줌
 */
public class MainActivity extends AppCompatActivity {
    private FileListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // attach FileListFragment
        mListFragment = new FileListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_main, mListFragment).commit();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // fragment로 key를 보냄
        if (mListFragment.onKeyUp(keyCode)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
