package com.example.bonita.filemanager;

import java.util.List;

/**
 * 파일목록이 갱신됐을 경우 콜백을 주기 위한 class
 */
public interface AsyncCallback {
    void onListUpdated(List<FileItem> itemList);
}
