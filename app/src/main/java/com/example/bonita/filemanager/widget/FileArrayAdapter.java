package com.example.bonita.filemanager.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonita.filemanager.FileAdapterClickListener;
import com.example.bonita.filemanager.FileInfoViewHolder;
import com.example.bonita.filemanager.FileItem;
import com.example.bonita.filemanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bonita.h.lee on 2019-08-29.
 * FileArrayAdapter
 */

public class FileArrayAdapter extends RecyclerView.Adapter<FileInfoViewHolder> {
    private List<FileItem> mItemList;
    private FileAdapterClickListener mClickListener;

    public FileArrayAdapter(FileAdapterClickListener listener) {
        mItemList = new ArrayList<>();
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

    /**
     * adapter의 itemList를 변경
     *
     * @param list 변경될 list
     */
    public void setItemList(List<FileItem> list) {
        mItemList = list;
    }
}
