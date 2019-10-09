package com.example.bonita.filemanager.widget;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bonita.filemanager.FileListItem;
import com.example.bonita.filemanager.R;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by bonita.h.lee on 2019-08-29.
 */

public class FileArrayAdapter extends ArrayAdapter<FileListItem> {
    private ViewHolder viewHolder;
    private List<FileListItem> mList;

    public FileArrayAdapter(Context context, int textViewResourceId, List<FileListItem> objects) {
        super(context, textViewResourceId, objects);
        mList = objects;
    }

    ////// TODO: 2019-10-08 seminar 과제!금요일까지 mainactivity->fragment , fileListItem->kotlin, imageview->scaletype center, fileListItem 멤버변수 추가
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.image_text_item_linear, null);
            viewHolder = new ViewHolder();
            viewHolder.setFileNameTv((TextView) convertView.findViewById(R.id.tv_file_name));
            viewHolder.setFileDateTv((TextView) convertView.findViewById(R.id.tv_file_date));
            viewHolder.setFileSizeTv((TextView) convertView.findViewById(R.id.tv_file_size));
            viewHolder.setImageView((ImageView) convertView.findViewById(R.id.image_file));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        File file = getItem(position).getFile();

        // textView imageView 구성
        setTextViewText(viewHolder, file);
        setImageViewResource(viewHolder.getImageView(), file.getName());

        return convertView;
    }

    class ViewHolder {
        private TextView fileNameTv;
        private TextView fileDateTv;
        private TextView fileSizeTv;
        private ImageView imageView;


        public TextView getFileNameTv() {
            return fileNameTv;
        }

        public void setFileNameTv(TextView fileNameTv) {
            this.fileNameTv = fileNameTv;
        }

        public TextView getFileDateTv() {
            return fileDateTv;
        }

        public void setFileDateTv(TextView fileDateTv) {
            this.fileDateTv = fileDateTv;
        }

        public TextView getFileSizeTv() {
            return fileSizeTv;
        }

        public void setFileSizeTv(TextView fileSizeTv) {
            this.fileSizeTv = fileSizeTv;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }

    /**
     * TextView에 파일이름, 파일날짜, 파일크기를 setting한다
     *
     * @param viewHolder
     * @param file
     */
    private void setTextViewText(ViewHolder viewHolder, File file) {
        viewHolder.getFileNameTv().setText(file.getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        viewHolder.getFileDateTv().setText(simpleDateFormat.format(file.lastModified()));
        long lFileSize = file.length();
        viewHolder.getFileSizeTv().setText(getFileSize(lFileSize));
    }

    /**
     * 파일 크기 + 단위 붙여서 반환
     *
     * @param lFileSize
     * @return
     */
    private String getFileSize(long lFileSize) {
        if (lFileSize > 1000000) {
            return lFileSize + "MB";
        } else {
            return lFileSize + "KB";
        }
    }

    /**
     * 리스트뷰의 아이템에 이미지를 변경한다.
     *
     * @param imageView
     * @param sFileName
     */
    private void setImageViewResource(ImageView imageView, String sFileName) {
        String sExt = FilenameUtils.getExtension(sFileName);
        switch (sExt) {
            case "pptx":
                imageView.setImageResource(R.drawable.home_ico_file_pptx);
                break;
            case "ppt":
                imageView.setImageResource(R.drawable.home_ico_file_ppt);
                break;
            case "xls":
                imageView.setImageResource(R.drawable.home_ico_file_xls);
                break;
            case "xlsx":
                imageView.setImageResource(R.drawable.home_ico_file_xlsx);
                break;
            case "doc":
                imageView.setImageResource(R.drawable.home_ico_file_doc);
                break;
            case "docx":
                imageView.setImageResource(R.drawable.home_ico_file_docx);
                break;
            default:
                //imageView.setImageResource(R.drawable.home_ico_file_pptx);
                break;
        }
    }

    @Nullable
    @Override
    public FileListItem getItem(int position) {
        return mList.get(position);
    }
}
