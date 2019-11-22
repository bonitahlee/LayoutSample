package com.example.bonita.filemanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * item에 관한 정보를 담고있는 holder. (재사용 하기 위해 추가)
 */
public class FileInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private FileAdapterClickListener mClickListener;

    private TextView fileNameTv;
    private TextView fileDateTv;
    private TextView fileSizeTv;
    private ImageView fileImage;
    private ImageView favorImage;

    public FileInfoViewHolder(View view, FileAdapterClickListener listener) {
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

    public TextView getFileNameTv() {
        return fileNameTv;
    }

    public TextView getFileDateTv() {
        return fileDateTv;
    }

    public TextView getFileSizeTv() {
        return fileSizeTv;
    }

    public ImageView getFileImage() {
        return fileImage;
    }

    public ImageView getFavorImage() {
        return favorImage;
    }
}
