package com.example.bonita.filemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonita.filemanager.adapter.FileArrayAdapter;
import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.function.FileFunction;
import com.example.bonita.filemanager.item.FileItem;
import com.example.bonita.filemanager.listener.FileManagerListener;

/**
 * 파일목록을 보여주는 Fragment
 */
public class FileListFragment extends Fragment {
    private final String TAG = "FilListFragment";

    private LinearLayoutManager mLayoutManager;
    private FileArrayAdapter mFileAdapter;
    private FileFunction mFileFunction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFileFunction = new FileFunction(new FileManagerListener.TaskListener() {
            @Override
            public void onTaskCompleted() {
                // FileOperatorTask에서 콜백받는 부분
                updateList();
            }
        });
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
        mFileAdapter = new FileArrayAdapter(mFileFunction.getItemList(), new FileManagerListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Adapter 의 항목을 선택했을 경우 키 처리
                FileItem item = mFileAdapter.getItem(position);
                String filePath = item.getFilePath();

                if (item.isDir()) {
                    // 상위/하위 폴더로 진입
                    mFileFunction.openFolder(filePath);
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
        mFileFunction.openFolder(FileManagerDefine.PATH_ROOT);
    }

    /**
     * 키 이벤트 처리
     */
    public boolean onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mFileFunction.deleteFile();
            return true;
        }
        return false;
    }

    /**
     * Adapter 항목 갱신
     **/
    private void updateList() {
        mFileAdapter.notifyDataSetChanged();
        mLayoutManager.scrollToPosition(0);
    }
}
