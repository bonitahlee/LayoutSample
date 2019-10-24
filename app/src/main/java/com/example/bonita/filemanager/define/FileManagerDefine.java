package com.example.bonita.filemanager.define;

import android.os.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * 각종 static final 변수들을 담아두는 class
 */
public class FileManagerDefine {
    public static final String PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String UPPER = "..";

    public static final List<String> EXT_TEXT = Arrays.asList("txt");
    public static final List<String> EXT_DOC = Arrays.asList("doc", "docx");
    public static final List<String> EXT_PPT = Arrays.asList("ppt", "ppts");
    public static final List<String> EXT_EXCEL = Arrays.asList("xls", "xlsx");
    public static final List<String> EXT_PDF = Arrays.asList("pdf");

    public static final List<String> EXT_IMAGE = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");

    public static final List<String> EXT_MUSIC = Arrays.asList("mp3");
    public static final List<String> EXT_VIDEO = Arrays.asList("mp4");

}
