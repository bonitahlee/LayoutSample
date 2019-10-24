package com.example.bonita.filemanager.define;

import android.os.Environment;

/**
 * 각종 static final 변수들을 담아두는 class
 */
public class FileManagerDefine {
    public static final String PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PATH_DOCUMENTS = PATH_ROOT + "/documents/";
    public static final String UPPER = "...";
}
