package com.example.bonita.filemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.util.FileFunction;
import com.example.bonita.filemanager.widget.FileArrayAdapter;
import com.example.bonita.filemanager.widget.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일들을 보여주는 ListView Fragment
 */
public class FileListFragment extends ListFragment {
    private FileFunction mFileFunction;
    private FileArrayAdapter mFileAdapter;

    private List<FileItem> mItemList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileFunction = new FileFunction();
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
        mItemList = new ArrayList<>();
        mFileAdapter = new FileArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mItemList);
        setListAdapter(mFileAdapter);
        ////// TODO: 2019-10-24 noti는 누가 해줘야 하는지, adapter를 생성하고 setlistadapter하면안됨?? ListView와 Adapter의 역할을 잘 알아야혀
        if (mFileFunction.openFolder(mItemList, FileManagerDefine.PATH_ROOT)) {
            updateList();
        }
    }

    /**
     * ListView 의 항목을 선택했을 경우 키 처리
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FileItem item = (FileItem) l.getItemAtPosition(position);
        String filePath = item.getFilePath();

        if (item.isDir()) {
            if (item.getFileName().equals(FileManagerDefine.UPPER_FOLDER)) {
                // 상위
                filePath = new File(mFileFunction.getCurrentPath()).getParentFile().getParentFile().getAbsolutePath();
            }
            // 상위/하위 폴더로 진입
            openFolder(filePath);
        } else {
            // 파일 열기
            mFileFunction.openFile(this, filePath);
        }

        super.onListItemClick(l, v, position, id);
    }

    /**
     * 상위/하위 폴더 진입
     *
     *  @param filePath
     */
    private void openFolder(String filePath) {
        if (mFileFunction.openFolder(mItemList, filePath)) {
            // mItemList가 변경되었을 때에만 list update 하도록
            updateList();
        }
    }

    /**
     * ListView 갱신
     **/
    private void updateList() {
        ////// TODO: 2019-10-24 콜백처리 해야함. UI에서 listView Item 넣는 작업을 하면 ANR이 걸릴수도 있음??
        mFileAdapter.setItemList(mItemList);
        mFileAdapter.notifyDataSetChanged();
    }
}
