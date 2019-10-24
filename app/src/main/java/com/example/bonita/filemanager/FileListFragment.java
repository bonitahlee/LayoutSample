package com.example.bonita.filemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.util.FileFunction;
import com.example.bonita.filemanager.widget.FileArrayAdapter;
import com.example.bonita.filemanager.widget.FileItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 파일들을 보여주는 ListView
 */
public class FileListFragment extends ListFragment {
    private FileFunction mFunction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunction = new FileFunction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
    }

    /**
     * adapter 구성
     */
    private void initAdapter() {
        List<FileItem> items = new ArrayList<>();
        FileArrayAdapter adapter = new FileArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
        // noti는 누가 해줘야 하는지, adapter를 생성하고 setlistadapter하면안됨
        mFunction.setAdapter(adapter);
        mFunction.refreshList(FileManagerDefine.PATH_DOCUMENTS);
    }

    /**
     * ListView의 항목을 선택했을 경우 키 처리
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FileItem item = (FileItem) l.getItemAtPosition(position);

        if (item.isDir()) {
            // 폴더일 경우
            if (item.getFileName().equals(FileManagerDefine.UPPER)) {
                // 상위 폴더 진입
                mFunction.moveParent();
            } else {
                //폴더 진입
                mFunction.refreshList(item.getFilePath());
            }
        }

        super.onListItemClick(l, v, position, id);
    }

    /**
     * ListView 클릭 외의 키 처리
     */
    public boolean onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 상위 폴더로 이동 또는 앱 종료
            if (mFunction.moveParent()) {
                return true;
            }
        }
        return false;
    }
}
