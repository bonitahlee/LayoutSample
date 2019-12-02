package com.example.bonita.filemanager;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.example.bonita.filemanager.define.FileEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * File operator 관련 class (파일 열기, 폴더 상/하위 이동, 삭제 등..)
 */
public class FileFunction {

    private NotifyListener mCallBack;
    private List<FileItem> mItemList;

    public FileFunction(NotifyListener callback) {
        mCallBack = callback;
        mItemList = new ArrayList<>();
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
     * 상위/하위 폴더 진입
     *
     * @param filePath filePath내로 진입
     */
    public void openFolder(String filePath) {
        Object[] objects = new Object[]{FileEvent.OPEN_FOLDER, filePath};
        new FileOperatorTask(mItemList, mCallBack).execute(objects);
    }

    /**
     * 파일/폴더 삭제
     */
    public void deleteFile() {
        Object[] objects = new Object[]{FileEvent.DELETE_FILE, null};
        new FileOperatorTask(mItemList, mCallBack).execute(objects);
    }

    /**
     * FileItem의 List를 반환
     */
    public List<FileItem> getItemList() {
        return mItemList;
    }
}