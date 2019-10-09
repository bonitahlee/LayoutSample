package com.example.bonita.filemanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bonita.filemanager.widget.EmptyListView;
import com.example.bonita.filemanager.widget.FileArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * /documents 폴더 내의 파일들을 갖고와 화면에 뿌려줌
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListView();
    }

    /**
     * 첫 화면 ListView 구성
     */
    private void initListView() {
        ListView listView = (ListView) findViewById(R.id.lv_main);
        ////// TODO: 2019-10-08 emptyListView처리?
        EmptyListView listEmptyView = (EmptyListView) findViewById(R.id.empty_lv_main);
        //listEmptyView.setContentDescription("");

        List<FileListItem> items = getFileList();
        FileArrayAdapter adapter = new FileArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        if (adapter.getCount() == 0) {
            listView.setVisibility(View.GONE);
        }
        initListener(listView);
    }

    /**
     * ListView의 Listener를 setting
     *
     * @param listView
     */
    private void initListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    /**
     * 파일 목록을 갖고옴
     * 현재에는 /sdcard/documents 폴더 내에있는 파일목록들을 갖고옴
     *
     * @return 위 경로에 해당하는 파일 목록
     */
    private List<FileListItem> getFileList() {
        List<FileListItem> fileList = new ArrayList<>();
        ////// TODO: 2019-10-08 정의로 string등을 빼놔야 함 
        File directory = new File(Environment.getExternalStorageDirectory() + "/documents/");   // 폴더 경로
        if (!directory.exists()) {
            directory.mkdir();
        }
        //// TODO: 2019-10-08 files가 null일 수도 있음
        File[] files = directory.listFiles();
        for (File file : files) {
            fileList.add(new FileListItem(file.getAbsolutePath()));
        }
        return fileList;
    }
}
