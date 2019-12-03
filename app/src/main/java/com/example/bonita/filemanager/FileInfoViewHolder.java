package com.example.bonita.filemanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bonita.filemanager.define.FileManagerDefine;
import com.example.bonita.filemanager.listener.ItemClickListener;

/**
 * ViewHolder
 */
public class FileInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener mClickListener;

    private TextView fileNameTv;
    private TextView fileDateTv;
    private TextView fileSizeTv;
    private ImageView fileImage;
    private ImageView favorImage;

    public FileInfoViewHolder(View view, ItemClickListener listener) {
        super(view);
        fileNameTv = view.findViewById(R.id.tv_file_name);
        fileDateTv = view.findViewById(R.id.tv_file_date);
        fileSizeTv = view.findViewById(R.id.tv_file_size);
        fileImage = view.findViewById(R.id.image_file);
        favorImage = view.findViewById(R.id.image_favor);

        // set item click listener
        mClickListener = listener;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickListener.onClick(v, getAdapterPosition());
    }

    /**
     * item을 view에 binding
     */
    public void bind(FileItem item) {
        // set [file date, file size, image favor] visibility
        setViewVisibility(item);

        // set textView & imageView
        setTextAndImage(item);

        // set selection of image_favor
        setFavorImage(item);
    }

    /**
     * 경우에 따라 [파일 날짜, 파일 사이즈, 즐겨찾기 이미지]를 보이거나 숨기도록 설정
     */
    private void setViewVisibility(FileItem item) {
        if (item.getFileName().equals(FileManagerDefine.UPPER_FOLDER)) {
            // 상위 폴더 이동 (..)에는 file date, file size를 안보이도록
            fileDateTv.setVisibility(View.GONE);
            fileSizeTv.setVisibility(View.GONE);
            favorImage.setVisibility(View.GONE);
        } else if (item.isDir()) {
            // 폴더일 경우에는 file date 만 안보이도록
            fileDateTv.setVisibility(View.VISIBLE);
            fileSizeTv.setVisibility(View.GONE);
            favorImage.setVisibility(View.VISIBLE);
        } else {
            // 그 외의 경우에는 다보이도록
            fileDateTv.setVisibility(View.VISIBLE);
            fileSizeTv.setVisibility(View.VISIBLE);
            favorImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 파일이름, 파일날짜, 파일크기, 파일아이콘 의 text와 image resource를 설정
     */
    private void setTextAndImage(FileItem item) {
        fileNameTv.setText(item.getFileName());
        fileDateTv.setText(item.getFileDate());
        fileSizeTv.setText(item.getFileSize());
        fileImage.setImageResource(item.getImageResId());
    }

    /**
     * R.id.image_favor이 selector [image는 selected되었는지 기억 못하므로 code로 추가]
     */
    private void setFavorImage(final FileItem item) {
        favorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean selected = view.isSelected();
                view.setSelected(!selected);
                item.setFavored(!selected);
            }
        });

        // 즐겨찾기 선택된 item만 select된 상태로 표시
        if (item.isFavored()) {
            favorImage.setSelected(true);
        } else {
            favorImage.setSelected(false);
        }
    }
}
