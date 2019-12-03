package com.example.bonita.filemanager.listener;

import android.view.View;

/**
 * 각종 이벤트의 콜백 인터페이스
 */
public interface NotifyListener {

    void onClick(View view, int position);

    void onTaskCompleted();
}
