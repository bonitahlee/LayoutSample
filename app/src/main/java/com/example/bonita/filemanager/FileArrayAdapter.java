package com.example.bonita.filemanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonita.filemanager.listener.FileManagerListener;

import java.util.List;

/**
 * FileItem data를 받아 FileListFragment로 뿌려주는 class
 */

public class FileArrayAdapter extends RecyclerView.Adapter<FileInfoViewHolder> {
    private List<FileItem> mItemList;
    private FileManagerListener.ItemClickListener mClickListener;

    public FileArrayAdapter(List<FileItem> itemList, FileManagerListener.ItemClickListener listener) {
        mItemList = itemList;
        mClickListener = listener;
    }

    /**
     * Create new views (invoked by the layout manager)
     * viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
     */
    @Override
    public FileInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_text_item, parent, false);
        return new FileInfoViewHolder(view, mClickListener);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
     */
    @Override
    public void onBindViewHolder(FileInfoViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public FileItem getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
