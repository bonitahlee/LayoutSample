package com.example.bonita.filemanager.util;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.example.bonita.filemanager.FileListFragment;
import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.widget.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * File operator 관련 class (파일 열기, 폴더 상/하위 이동 등..)
 */
public class FileFunction {

    /**
     * 상위/하위 폴더로 진입
     *
     * @param itemList
     * @param path
     * @return true: 이동함
     */
    public boolean openFolder(List<FileItem> itemList, String path) {
        if (!isExist(new File(path))) {
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
                Uri.fromFile(new File(path)),
                FileItemUtils.getMimeType(path));

        try {
            fragment.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            // emulator에 실행할 app이 설치되어 있지 않다면 모든타입("*/*")으로 바꿔서 보내도록 exception 처리
            e.printStackTrace();
            sendIntent.setDataAndType(Uri.fromFile(new File(path)), "*/*");
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
        List<FileItem> fileList = new ArrayList<>();
        for (File file : files) {
            fileList.add(new FileItem(
                    file.getAbsolutePath(),
                    file.getName(),
                    file.lastModified(),
                    file.length(),
                    file.isDirectory()));
        }

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
     * 이동하려는 경로가 존재하는지 확인(최상위폴더는 제외)
     *
     * @param file 이동하려는 파일
     * @return true: 이동 가능, false: 이동 불가(최상위 폴더일 경우) -> 앱 종료
     */
    private boolean isExist(File file) {
        return file != null && file.exists() && !file.getAbsolutePath().equals("/");
    }
}
