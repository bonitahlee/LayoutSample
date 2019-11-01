package com.example.bonita.filemanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.event.FileEvent;
import com.example.bonita.filemanager.util.FileAdapterClickListener;
import com.example.bonita.filemanager.util.FileFunction;
import com.example.bonita.filemanager.util.FileItem;
import com.example.bonita.filemanager.widget.FileArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 파일들의 ListView을 보여주는 Fragment
 */
public class FileListFragment extends Fragment {
    private final String TAG = "FilListFragment";

    private FileArrayAdapter mFileAdapter;

    private FileFunction mFileFunction;
    private List<FileItem> mItemList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileFunction = new FileFunction();
        mItemList = new ArrayList<>();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize adapter
        mFileAdapter = new FileArrayAdapter(mItemList, new FileAdapterClickListener() {
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
        new FolderTask().execute(objects);
    }

    /**
     * 폴더 관련 event 처리
     */
    class FolderTask extends AsyncTask<Object, Void, Boolean> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "pre");
            dialog = new ProgressDialog(FileListFragment.this.getActivity());
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            Log.e(TAG, "doin");
            switch ((int) params[0]) {
                // 폴더 열기
                case FileEvent.OPEN_FOLDER:
                    return mFileFunction.openFolder(mItemList, (String) params[1]);
                default:
                    return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.e(TAG, "post");
            dialog.dismiss();
            if (result) {
                // mItemList가 변경되었을 때에만 list update 하도록
                updateList();
            }
        }

        /**
         * Adapter 항목 갱신
         **/
        private void updateList() {
            mFileAdapter.setItemList(mItemList);
            mFileAdapter.notifyDataSetChanged();
        }
    }
}
