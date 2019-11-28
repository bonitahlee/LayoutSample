package com.example.bonita.filemanager;

import android.os.AsyncTask;

import com.example.bonita.filemanager.define.FileEvent;
import com.example.bonita.filemanager.define.FileManagerDefine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 파일/폴더 관련 operator를 수행하는 비동기 태스크
 */
public class FileOperatorTask extends AsyncTask<Object, Void, Boolean> {
    private final String TAG = "FileOperatorTask";

    private List<FileItem> mItemList;
    private AsyncCallback mCallBack;

    // // TODO: 2019-11-26 FileListFragment에 있는 fragment를 갖고와서 써야하나?
/*    private ProgressDialog dialog;
    private FileListFragment fragment;*/

    public FileOperatorTask(AsyncCallback callback, List<FileItem> itemList) {
        this.mCallBack = callback;
        mItemList = itemList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        Log.e(TAG, "pre");
        /*dialog = new ProgressDialog(fragment.getActivity());
        dialog.setMessage(fragment.getString(R.string.MSG_LOADING));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();*/
    }

    @Override
    protected Boolean doInBackground(Object... params) {
//        Log.e(TAG, "doin");
        switch ((int) params[0]) {
            // 폴더 열기
            case FileEvent.OPEN_FOLDER:
                return openFolder((String) params[1]);
            // 파일 삭제
            case FileEvent.DELETE_FILE:
                return deleteFile();
            default:
                return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
//        Log.e(TAG, "post");
//        dialog.dismiss();

        if (result) {
            // mItemList가 변경되었을 때에만 list update 하도록
            mCallBack.onListUpdated(mItemList);
        }
    }

    /**
     * 상위/하위 폴더로 진입
     *
     * @param path path 내로 진입
     * @return true: 이동함
     */
    private boolean openFolder(String path) {
        File file = new File(path);
        if (!isExist(file)) {
            // 이동하려는 폴더(상위 폴더로 이동일 때) 또는 현재 폴더(하위 폴더로 이동일 때)가 존재할 때에만 이동
            return false;
        }

        mItemList.clear();
        mItemList.addAll(getFileList(path));
        return true;
    }

    /**
     * itemList에서 favor check된 파일을 삭제
     */
    private boolean deleteFile() {
        // favor check된 항목을 삭제
        int iDeleteCount = 0;

        ListIterator<FileItem> iterator = mItemList.listIterator();
        while (iterator.hasNext()) {
            FileItem item = iterator.next();

            if (!item.isFavored() || item.isDir()) {
                // 일단 파일들만 삭제 가능
                continue;
            }

            File file = new File(item.getFilePath());
            if (isExist(file)) {
                iterator.remove();
                file.delete();
                iDeleteCount++;
            }
        }

        // // TODO: 2019-11-28 추후 삭제할 항목이 없을 때의 예외처리 필요
        if (iDeleteCount == 0) {
            return false;
        }
        return true;
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
}
