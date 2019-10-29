package com.example.bonita.filemanager.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bonita.filemanager.R;
import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.util.FileAdapterClickListener;
import com.example.bonita.filemanager.util.FileItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bonita.h.lee on 2019-08-29.
 * FileArrayAdapter
 */

public class FileArrayAdapter extends RecyclerView.Adapter<FileArrayAdapter.FileInfoViewHolder> {
    private List<FileItem> mItemList;
    private FileAdapterClickListener mClickListener;

    public FileArrayAdapter(List<FileItem> objects, FileAdapterClickListener listener) {
        mItemList = new ArrayList<>();
        mClickListener = listener;
        setItemList(objects);
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public FileInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_text_item, parent, false);
        return new FileInfoViewHolder(view, mClickListener);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(FileInfoViewHolder holder, int position) {
        // set [file date, file size, image favor] visibility
        setViewVisibility(holder, position);

        // textView imageView 구성
        setTextImage(holder, position);
    }

    /**
     * 경우에 따라 [파일 날짜, 파일 사이즈, 즐겨찾기 이미지]를 보이거나 숨기도록 설정
     */
    private void setViewVisibility(FileInfoViewHolder viewHolder, int position) {
        FileItem item = getItem(position);
        if (item == null) {
            return;
        }

        if (item.getFileName().equals(FileManagerDefine.UPPER_FOLDER)) {
            // 상위 폴더 이동 (..)에는 file date, file size를 안보이도록
            viewHolder.fileDateTv.setVisibility(View.GONE);
            viewHolder.fileSizeTv.setVisibility(View.GONE);
            viewHolder.favorImage.setVisibility(View.GONE);
            // height를 -1(match parent)로 임의 지정....?
            // viewHolder.fileNameTv.getLayoutParams().height = -1;
        } else if (item.isDir()) {
            // 폴더일 경우에는 file date 만 안보이도록
            viewHolder.fileDateTv.setVisibility(View.VISIBLE);
            viewHolder.fileSizeTv.setVisibility(View.GONE);
            viewHolder.favorImage.setVisibility(View.VISIBLE);
        } else {
            // 그 외의 경우에는 다보이도록
            viewHolder.fileDateTv.setVisibility(View.VISIBLE);
            viewHolder.fileSizeTv.setVisibility(View.VISIBLE);
            viewHolder.favorImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 파일이름, 파일날짜, 파일크기, 파일아이콘 의 text와 image resource를 설정
     */
    private void setTextImage(FileInfoViewHolder viewHolder, int position) {
        viewHolder.fileNameTv.setText(mItemList.get(position).getFileName());
        viewHolder.fileDateTv.setText(mItemList.get(position).getFileDate());
        viewHolder.fileSizeTv.setText(mItemList.get(position).getFileSize());
        viewHolder.fileImage.setImageResource(mItemList.get(position).getImageResId());
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

    /**
     * item에 관한 정보를 담고있는 holder. (재사용 하기 위해 추가)
     */
    class FileInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fileNameTv;
        private TextView fileDateTv;
        private TextView fileSizeTv;
        private ImageView fileImage;
        private ImageView favorImage;

        FileInfoViewHolder(View view, FileAdapterClickListener listener) {
            super(view);
            fileNameTv = view.findViewById(R.id.tv_file_name);
            fileDateTv = view.findViewById(R.id.tv_file_date);
            fileSizeTv = view.findViewById(R.id.tv_file_size);
            fileImage = view.findViewById(R.id.image_file);
            favorImage = view.findViewById(R.id.image_favor);

            // image_favor view selector 구현. 여기다 하는게 맞나용?
            addFavorSelector();

            // set item click listener
            mClickListener = listener;
            view.setOnClickListener(this);
        }

        /**
         * R.id.image_favor이 selector [image는 selected되었는지 기억 못하므로 code로 추가]
         */
        private void addFavorSelector() {
            favorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setSelected(!view.isSelected());
                }
            });
        }

        @Override
        public void onClick(View v) {
            mClickListener.onClick(v, getAdapterPosition());
        }
    }
}
