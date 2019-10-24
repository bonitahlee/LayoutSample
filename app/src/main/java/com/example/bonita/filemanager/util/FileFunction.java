package com.example.bonita.filemanager.util;

import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.widget.FileArrayAdapter;
import com.example.bonita.filemanager.widget.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * File operator 관련 class
 */
public class FileFunction {

    private String mCurrentPath;
    private FileArrayAdapter mAdapter;

    /**
     * 폴더의 하위 목록을 갖고와서 ListView에 뿌려줌
     *
     * @param path 폴더 경로
     */
    public void refreshList(String path) {
        List<FileItem> fileList = new ArrayList<>();
        File directory = new File(path);   // 폴더 경로
        if (directory.isDirectory() && !directory.exists()) {
            // 20191009. directory가 존재하지 않을 때에는 폴더를 만들어 줘야함
            directory.mkdirs();
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            // 20191009. directory 내의 파일이 존재하지 않을 때에는 빈 list를 보여줘야함
            updateList(fileList, directory.getAbsolutePath() + "/" + FileManagerDefine.UPPER);
            return;
        }

        for (File file : files) {
            fileList.add(new FileItem(
                    file.getAbsolutePath(),
                    file.lastModified(),
                    file.length(),
                    file.isDirectory()));
        }

        updateList(fileList, fileList.get(0).getFilePath());
    }

    /**
     * 상위 폴더로 이동
     *
     * @return true: 이동함
     */
    public boolean moveParent() {
        File parent = new File(mCurrentPath).getParentFile().getParentFile();
        if (isExist(parent)) {
            // 상위 폴더가 존재하면 이동
            refreshList(parent.getAbsolutePath());
            return true;
        }
        return false;
    }

    /**
     * 화면 갱신
     *
     * @param fileList 갱신할 항목 list
     * @param path     현재 폴더/파일 경로
     */
    private void updateList(List<FileItem> fileList, String path) {
        File parent = new File(path).getParentFile().getParentFile();
        if (isExist(parent)) {
            // 상위 경로가 존재할 때에는 ... 추가
            fileList.add(0, new FileItem(
                    parent.getAbsolutePath() + "/" + FileManagerDefine.UPPER,
                    0,
                    0,
                    true));
        }
        mAdapter.setItemList(fileList);
        mCurrentPath = path;
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

    /**
     * setting adapter
     */
    public void setAdapter(FileArrayAdapter adapter) {
        mAdapter = adapter;
    }

}
