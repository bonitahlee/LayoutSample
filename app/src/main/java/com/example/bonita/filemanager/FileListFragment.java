package com.example.bonita.filemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.event.FileEvent;
import com.example.bonita.filemanager.widget.FileArrayAdapter;

////// TODO: 2019-11-07 FileAdapter를 들고있어야 할까? itemList도? 전체적인 구조변경 필요(adapter에 관해 많이 찾아보기) 

/**
 * 파일들의 ListView을 보여주는 Fragment
 */
public class FileListFragment extends Fragment {
    private final String TAG = "FilListFragment";

    private FileArrayAdapter mFileAdapter;
    private LinearLayoutManager mLayoutManager;

    private FileFunction mFileFunction;

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

        // RecyclerView 구성
        RecyclerView recyclerView = view.findViewById(R.id.file_list);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        // initialize adapter
        mFileAdapter = new FileArrayAdapter(new FileAdapterClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Adapter 의 항목을 선택했을 경우 키 처리
                FileItem item = mFileAdapter.getItem(position);
                String filePath = item.getFilePath();

                if (item.isDir()) {
                    // 상위/하위 폴더로 진입
                    openFolder(filePath);
                } else {
                    // 파일 열기
                    mFileFunction.openFile(FileListFragment.this, filePath);
                }
            }
        });
        recyclerView.setAdapter(mFileAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // show file list
        openFolder(FileManagerDefine.PATH_ROOT);
    }

    /**
     * 상위/하위 폴더 진입
     *
     * @param filePath
     */
    private void openFolder(String filePath) {
        Object[] objects = new Object[]{FileEvent.OPEN_FOLDER, filePath};
        mFileFunction.getAsyncTask(this).execute(objects);
    }

    public FileArrayAdapter getAdapter() {
        return mFileAdapter;
    }
}
