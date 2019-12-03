package com.example.bonita.filemanager.listener;

import android.view.View;

/**
 * FileArrayAdapter(RecyclerView.Adapter) 의 항목을 선택하였을 때의 처리를 위한 interface
 */
public interface ItemClickListener {
    void onClick(View view, int position);

    /**
     * Task가 끝났을 때의 처리를 위한 interface
     */
    interface TaskListener {
        void onTaskCompleted();
    }
}
