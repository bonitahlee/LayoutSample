package com.example.bonita.filemanager;

import java.io.File;

////// TODO: 2019-10-08 각각 member 변수로 갖고 있어야 함
public class FileListItem {

    private File mFile;
    private String mPath;

    public FileListItem(String path) {
        mPath = path;
    }

    public File getFile() {
        if (mFile == null) {
            mFile = new File(mPath);
        }
        return mFile;
    }

    public void setFile(File mFile) {
        this.mFile = mFile;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }
}
