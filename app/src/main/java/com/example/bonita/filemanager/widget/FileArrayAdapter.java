package com.example.bonita.filemanager.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bonita.filemanager.FileInfoViewHolder;
import com.example.bonita.filemanager.R;
import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.FileAdapterClickListener;
import com.example.bonita.filemanager.FileItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bonita.h.lee on 2019-08-29.
 * FileArrayAdapter
 */

public class FileArrayAdapter extends RecyclerView.Adapter<FileInfoViewHolder> {
    private List<FileItem> mItemList;
    private List<String> mCheckedList;
    private FileAdapterClickListener mClickListener;

    public FileArrayAdapter(FileAdapterClickListener listener) {
        mItemList = new ArrayList<>();
        mCheckedList = new ArrayList<>();
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
        // set [file date, file size, image favor] visibility
        setViewVisibility(holder, position);

        // textView imageView 구성
        setTextAndImage(holder, position);

        // image_favor view selector 구현. 여기다 하는게 맞나용?
        addFavorSelector(holder.getFavorImage(), position);
    }

    /**
     * 경우에 따라 [파일 날짜, 파일 사이즈, 즐겨찾기 이미지]를 보이거나 숨기도록 설정
     */
    private void setViewVisibility(FileInfoViewHolder viewHolder, int position) {
        FileItem item = getItem(position);

        if (item.getFileName().equals(FileManagerDefine.UPPER_FOLDER)) {
            // 상위 폴더 이동 (..)에는 file date, file size를 안보이도록
            viewHolder.getFileDateTv().setVisibility(View.GONE);
            viewHolder.getFileSizeTv().setVisibility(View.GONE);
            viewHolder.getFavorImage().setVisibility(View.GONE);
        } else if (item.isDir()) {
            // 폴더일 경우에는 file date 만 안보이도록
            viewHolder.getFileDateTv().setVisibility(View.VISIBLE);
            viewHolder.getFileSizeTv().setVisibility(View.GONE);
            viewHolder.getFavorImage().setVisibility(View.VISIBLE);
        } else {
            // 그 외의 경우에는 다보이도록
            viewHolder.getFileDateTv().setVisibility(View.VISIBLE);
            viewHolder.getFileSizeTv().setVisibility(View.VISIBLE);
            viewHolder.getFavorImage().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 파일이름, 파일날짜, 파일크기, 파일아이콘 의 text와 image resource를 설정
     */
    private void setTextAndImage(FileInfoViewHolder viewHolder, int position) {
        viewHolder.getFileNameTv().setText(mItemList.get(position).getFileName());
        viewHolder.getFileDateTv().setText(mItemList.get(position).getFileDate());
        viewHolder.getFileSizeTv().setText(mItemList.get(position).getFileSize());
        viewHolder.getFileImage().setImageResource(mItemList.get(position).getImageResId());
    }

    /**
     * R.id.image_favor이 selector [image는 selected되었는지 기억 못하므로 code로 추가]
     */
    private void addFavorSelector(ImageView favorImage, final int position) {
        favorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelected = view.isSelected();
                view.setSelected(!isSelected);
                setSelection(isSelected, String.valueOf(position));
            }
        });
    }

    private void setSelection(boolean isSelected, String position) {
        if (isSelected) {
            mCheckedList.add(position);
        } else {
            mCheckedList.remove(position);
        }
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
