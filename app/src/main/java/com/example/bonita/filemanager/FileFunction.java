package com.example.bonita.filemanager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.example.bonita.filemanager.event.FileEvent;
import com.example.bonita.filemanager.event.FileEventHandler;

/**
 * File operator 관련 class (파일 열기, 폴더 상/하위 이동, 삭제 등..)
 */
public class FileFunction {
    private static final String TAG = "FileFunction";

    private FileEventHandler mHandler;

    public FileFunction() {
        mHandler = new FileEventHandler();
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
        Bundle bundle = new Bundle();
        bundle.putString("FILE_PATH", filePath);
        sendMessage(FileEvent.OPEN_FOLDER, bundle);
    }

    /**
     * 파일/폴더 삭제
     */
    public void deleteFile() {
        Bundle bundle = new Bundle();
        bundle.putString("FILE_PATH", null);
        sendMessage(FileEvent.DELETE_FILE, bundle);
    }

    /**
     * handler 로 msg 보내기
     */
    private void sendMessage(int fileEvent, Bundle bundle) {
        Message message = mHandler.obtainMessage();
        message.what = fileEvent;
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
}