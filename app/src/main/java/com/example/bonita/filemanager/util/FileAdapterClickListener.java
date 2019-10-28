package com.example.bonita.filemanager.util;

import android.view.View;

/**
 * FileArrayAdapter(RecyclerView.Adapter) 의 항목을 선택하였을 때의 처리를 위한 interface
 */
public interface FileAdapterClickListener {
    void onClick(View view, int position);
}
