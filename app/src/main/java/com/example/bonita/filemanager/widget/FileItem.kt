package com.example.bonita.filemanager.widget

import com.example.bonita.filemanager.util.FileItemUtils

/**
 * 파일에 관한 정보를 담고있는 class (경로, 이름, 수정날짜, 사이즈, image resource id, 폴더 or 파일 여부)
 */
class FileItem(var filePath: String?, fileDate: Long, fileSize: Long, var isDir: Boolean) {
    var fileName: String? = null
    var fileDate: String? = null
    var fileSize: String? = null
    var imageResId: Int = 0

    init {
        this.fileName = FileItemUtils.getFileName(filePath)
        this.fileDate = FileItemUtils.getFileDate(fileDate)
        this.fileSize = FileItemUtils.getFileSize(fileSize)
        this.imageResId = FileItemUtils.getImageResId(this.fileName, isDir)
    }
}
