package com.example.bonita.filemanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.event.FileEvent;
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
    private final String TAG = this.getClass().getSimpleName();   // "FilListFragment"

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
        openFolder(FileManagerDefine.PATH_ROOT);
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
     * @param filePath
     */
    private void openFolder(String filePath) {
        Object[] objects = new Object[]{FileEvent.OPEN_FOLDER, filePath};
        new FolderTask().execute(objects);
    }

    /**
     * ListView 갱신
     **/
    private void updateList() {
        mFileAdapter.setItemList(mItemList);
        mFileAdapter.notifyDataSetChanged();
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
    }
}
