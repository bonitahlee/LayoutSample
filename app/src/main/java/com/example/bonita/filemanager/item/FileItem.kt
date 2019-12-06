package com.example.bonita.filemanager.item

import com.example.bonita.filemanager.util.FileUtils

/**
 * 파일에 관한 정보를 담고있는 class (경로, 이름, 수정날짜, 사이즈, image resource id, 폴더 or 파일 여부, 즐겨찾기 선택 여부)
 */
class FileItem(var filePath: String, var fileName: String, fileDate: Long, fileSize: Long, var isDir: Boolean) {

    var fileDate: String
    var fileSize: String
    var imageResId: Int = 0
    var isFavored: Boolean

    init {
        this.fileDate = FileUtils.getFileDate(fileDate)
        this.fileSize = FileUtils.getFileSize(fileSize)
        this.imageResId = FileUtils.getImageResId(this.fileName, isDir)
        this.isFavored = false;
    }
}
