package com.example.bonita.filemanager;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.event.FileEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * File operator 관련 class (파일 열기, 폴더 상/하위 이동 등..)
 */
public class FileFunction {
    private static final String TAG = "FileFunction";

    private List<FileItem> mItemList;

    public FileFunction() {
        mItemList = new ArrayList<>();
    }

    /**
     * 상위/하위 폴더로 진입
     *
     * @param itemList
     * @param path
     * @return true: 이동함
     */
    public boolean openFolder(List<FileItem> itemList, String path) {
        File file = new File(path);
        if (!isExist(file)) {
            // 이동하려는 폴더(상위 폴더로 이동일 때) 또는 현재 폴더(하위 폴더로 이동일 때)가 존재할 때에만 이동
            return false;
        }

        itemList.clear();
        itemList.addAll(getFileList(path));
        return true;
    }

    /**
     * 파일 열기
     */
    public void openFile(FileListFragment fragment, String path) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setDataAndType(
                FileUtils.getUriFromFile(fragment.getContext(), path),
                FileUtils.getMimeType(path));
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            fragment.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            // emulator에 실행할 app이 설치되어 있지 않다면 모든타입("*/*")으로 바꿔서 보내도록 exception 처리
            sendIntent.setDataAndType(
                    FileUtils.getUriFromFile(fragment.getContext(), path),
                    "*/*");
            fragment.startActivity(sendIntent);
        }
    }

    /**
     * 매개변수로 전달된 폴더경로의 하위 목록을 갖고옴
     *
     * @param path 폴더 경로
     */
    private List<FileItem> getFileList(String path) {
        File directory = new File(path);   // 폴더 경로

        if (!directory.exists() && directory.isDirectory()) {
            // 20191009. directory가 존재하지 않을 때에는 폴더를 만들어 줘야함
            directory.mkdirs();
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            // 20191009. directory 내의 파일이 존재하지 않을 때에는 빈 list를 보여줘야함
            files = new File[]{};
        }

        return addFileItemList(path, files);
    }

    /**
     * FileItem List에 항목을 추가하여 반환
     *
     * @param path  하위 항목을 가져올 경로
     * @param files path 폴더의 하위항목들
     * @return path폴더의 하위 항목(폴더/파일들)
     */
    private List<FileItem> addFileItemList(String path, File[] files) {
        List<FileItem> fileList = new ArrayList<>();     // 폴더 + 파일을 담는 list
        List<FileItem> onlyFileList = new ArrayList<>();  // file을 폴더 밑에 정렬하기 위해 파일만 담는 list 생성

        ////// TODO: 2019-11-07 추후에 sorting관련해서 변경 예정 
        // 폴더를 먼저 정렬하기 위해, 파일만 따로 구분한 뒤 나중에 fileList로 합쳐줌
        for (File file : files) {
            if (file.isDirectory()) {
                addFileItem(fileList, file);
            } else {
                addFileItem(onlyFileList, file);
            }
        }
        fileList.addAll(onlyFileList);

        File parent = new File(path).getParentFile();
        if (isExist(parent)) {
            // 상위 경로가 존재할 때 추가
            fileList.add(0, new FileItem(
                    parent.getAbsolutePath(),
                    FileManagerDefine.UPPER_FOLDER,
                    0,
                    0,
                    true));
        }
        return fileList;
    }

    /**
     * 매개변수로 넘어온 list에 FileItem을 생성하여 넣어줌
     */
    private void addFileItem(List<FileItem> list, File file) {
        list.add(new FileItem(
                file.getAbsolutePath(),
                file.getName(),
                file.lastModified(),
                file.length(),
                file.isDirectory()));
    }

    /**
     * 이동하려는 경로가 존재하는지 확인(최상위폴더는 제외)
     *
     * @param file 이동하려는 파일
     * @return true: 이동 가능, false: 이동 불가(최상위 폴더일 경우)
     */
    private boolean isExist(File file) {
        String topPath = new File(FileManagerDefine.PATH_ROOT).getParent();
        return file != null && file.exists() && !file.getAbsolutePath().equals(topPath);
    }

    ////// TODO: 2019-11-07 feedback: fileEventHandler로 대체

    /**
     * 폴더 관련 event 처리
     */
    public class FolderTask extends AsyncTask<Object, Void, Boolean> {
        private ProgressDialog dialog;
        private FileListFragment fragment;

        FolderTask(FileListFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "pre");
            dialog = new ProgressDialog(fragment.getActivity());
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
                    return openFolder(mItemList, (String) params[1]);
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
            fragment.getAdapter().setItemList(mItemList);
            fragment.getAdapter().notifyDataSetChanged();
//            mLayoutManager.scrollToPosition(0);
        }
    }

    public FolderTask getAsyncTask(FileListFragment fragment) {
        return new FolderTask(fragment);
    }
}