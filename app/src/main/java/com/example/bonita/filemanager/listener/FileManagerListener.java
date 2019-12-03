package com.example.bonita.filemanager.listener;

import android.view.View;

/**
 * 이 project에서 쓰이는 listener들을 묶어놓은 클래스
 */
public class FileManagerListener {

    /**
     * FileArrayAdapter(RecyclerView.Adapter) 의 항목을 선택하였을 때의 처리를 위한 interface
     */
    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    /**
     * Task가 끝났을 때의 처리를 위한 interface
     */
    public interface TaskListener {
        void onTaskCompleted();
    }
}
