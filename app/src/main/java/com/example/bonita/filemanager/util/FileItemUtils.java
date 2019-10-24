package com.example.bonita.filemanager.util;

import com.example.bonita.filemanager.R;
import com.example.bonita.filemanager.define.FileManagerDefine;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 파일에 관한 정보를 지정된 형식으로 변환하여 주는 class
 */
public class FileItemUtils {

    /**
     * 파일 이름 반환
     *
     * @param path file.getAbsolutePath()
     * @return file.getName()
     */
    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * 파일 확장자 반환
     *
     * @param name file.getName()
     * @return <ex> txt, doccx, xls...
     */
    public static String getFileExtension(String name) {
        return name.substring(name.lastIndexOf(".") + 1);

    }

    /**
     * 파일 수정 날짜를 지정된 형식으로 갖고옴
     *
     * @param modified file.lastModified()
     */
    public static String getFileDate(long modified) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return simpleDateFormat.format(modified);
    }

    /**
     * 파일 크기 + 단위 붙여서 반환
     * ////// TODO: 2019-10-10  다른 예시 찾기!
     *
     * @param size file.length()
     */
    public static String getFileSize(long size) {
        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if (size < sizeMb)
            return df.format(size / sizeKb) + " KB";
        else if (size < sizeGb)
            return df.format(size / sizeMb) + " MB";
        else if (size < sizeTerra)
            return df.format(size / sizeGb) + " GB";

        return "";
    }

    /**
     * 파일확장자에 따른 image의 resource id를 갖고옴
     *
     * @param sFileName file.getName()
     * @param bDir      true: 폴더, false: 파일
     * @return image resource id
     */
    public static int getImageResId(String sFileName, boolean bDir) {
        String sExt = getFileExtension(sFileName);
        switch (sExt) {
            case "pptx":
                return R.drawable.ico_file_pptx;
            case "ppt":
                return R.drawable.ico_file_ppt;
            case "xls":
                return R.drawable.ico_file_xls;
            case "xlsx":
                return R.drawable.ico_file_xlsx;
            case "doc":
                return R.drawable.ico_file_doc;
            case "docx":
                return R.drawable.ico_file_docx;
            default:
                if (bDir) {
                    if (sFileName.equals(FileManagerDefine.UPPER)) {
                        // 상위 폴더 이동인 경우
                        return R.drawable.ico_file_folder_upper;
                    } else {
                        // 그냥 폴더일 경우
                        return R.drawable.ico_file_folder_nosub;
                    }
                } else {
                    // 그 외 파일일 경우
                    return R.drawable.ico_file_unknown;
                }
        }
    }

    /**
     * 문서 type 별 mime type 가져오기
     */
    public static String getMimeType(String path) {
        String sExt = getFileExtension(path).toLowerCase();
        if (FileManagerDefine.EXT_TEXT.contains(sExt)) {
            return "text/*";
        } else if (FileManagerDefine.EXT_DOC.contains(sExt)) {
            return "application/msword";
        } else if (FileManagerDefine.EXT_PPT.contains(sExt)) {
            return "application/vnd.ms-powerpoint";
        } else if (FileManagerDefine.EXT_EXCEL.contains(sExt)) {
            return "application/vnd.ms-excel";
        } else if (FileManagerDefine.EXT_PDF.contains(sExt)) {
            return "application/pdf";
        } else if (FileManagerDefine.EXT_IMAGE.contains(sExt)) {
            return "image/*";
        } else if (FileManagerDefine.EXT_MUSIC.contains(sExt)) {
            return "audio/*";
        } else if (FileManagerDefine.EXT_VIDEO.contains(sExt)) {
            return "video/*";
        } else {
            return "*/*";
        }
    }
}
