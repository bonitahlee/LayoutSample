package com.example.bonita.filemanager.widget

import com.example.bonita.filemanager.util.FileItemUtils

/**
 *
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
