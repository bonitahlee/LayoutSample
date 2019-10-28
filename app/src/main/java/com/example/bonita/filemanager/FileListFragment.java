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
import com.example.bonita.filemanager.util.FileFunction;
import com.example.bonita.filemanager.util.FileAdapterClickListener;
import com.example.bonita.filemanager.widget.FileArrayAdapter;
import com.example.bonita.filemanager.widget.FileItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 파일들의 ListView을 보여주는 Fragment
 */
public class FileListFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();   // "FilListFragment"

    private RecyclerView mRecyclerView;
    private FileArrayAdapter mFileAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FileFunction mFileFunction;
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
        initListView(view);
    }

    /**
     * ListView 구성
     */
    private void initListView(View view) {
        mItemList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.file_list);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mFileAdapter = new FileArrayAdapter(mItemList, mItemClickListener);
        mRecyclerView.setAdapter(mFileAdapter);

        openFolder(FileManagerDefine.PATH_ROOT);
    }

    /**
     * ListView 의 항목을 선택했을 경우 키 처리
     */
    FileAdapterClickListener mItemClickListener = new FileAdapterClickListener() {
        @Override
        public void onClick(View view, int position) {
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
    };

    /**
     * 상위/하위 폴더 진입
     *
     * @param filePath
     */
    private void openFolder(String filePath) {
        Object[] objects = new Object[]{FileEvent.OPEN_FOLDER, filePath};
        new FolderTask().execute(objects);
    }

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
         * ListView 갱신
         **/
        private void updateList() {
            mFileAdapter.setItemList(mItemList);
            mFileAdapter.notifyDataSetChanged();
        }
    }
}
