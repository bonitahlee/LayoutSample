package com.example.bonita.filemanager.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bonita.filemanager.R;
import com.example.bonita.filemanager.define.FileManagerDefine;

import java.util.List;

/**
 * Created by bonita.h.lee on 2019-08-29.
 * FileArrayAdapter
 */

public class FileArrayAdapter extends ArrayAdapter<FileItem> {
    private List<FileItem> mItemList;

    public FileArrayAdapter(Context context, int textViewResourceId, List<FileItem> objects) {
        super(context, textViewResourceId, objects);
        mItemList = objects;
        setItemList(objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.image_text_item, null);
            viewHolder = new ViewHolder();
            viewHolder.fileNameTv = (TextView) convertView.findViewById(R.id.tv_file_name);
            viewHolder.fileDateTv = (TextView) convertView.findViewById(R.id.tv_file_date);
            viewHolder.fileSizeTv = (TextView) convertView.findViewById(R.id.tv_file_size);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_file);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // set [file date, file size] visibility
        setVisibilityFileDateSize(convertView, position);

        // image view selector 구현. 여기다 하는게 맞나용?
        ImageView image_favorite = (ImageView) convertView.findViewById(R.id.image_favor);
        image_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
            }
        });

        // textView imageView 구성
        setTextImage(viewHolder, position);
        return convertView;
    }

    /**
     * 경우에 따라 파일 날짜, 파일 사이즈를 보이거나 숨기도록 설정
     */
    private void setVisibilityFileDateSize(View convertView, int position) {
        FileItem item = getItem(position);
        if (item == null) {
            return;
        }

        if (item.getFileName().equals(FileManagerDefine.UPPER_FOLDER)) {
            // 상위 폴더 이동 (..)에는 file date, file size를 안보이도록
            convertView.findViewById(R.id.layout_file_date_size).setVisibility(View.GONE);
            // height를 -1(match parent)로 임의 지정....?
            //convertView.findViewById(R.id.tv_file_name).getLayoutParams().height = -1;
        } else if (item.isDir()) {
            // 폴더일 경우에는 file size 만 안보이도록
            convertView.findViewById(R.id.layout_file_date_size).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.tv_file_size).setVisibility(View.GONE);
        } else {
            // 그 외의 경우에는 다보이도록
            convertView.findViewById(R.id.layout_file_date_size).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.tv_file_size).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 파일이름, 파일날짜, 파일크기, 파일 image 의 text와 image resource를 설정
     */
    private void setTextImage(ViewHolder viewHolder, int position) {
        viewHolder.fileNameTv.setText(mItemList.get(position).getFileName());
        viewHolder.fileDateTv.setText(mItemList.get(position).getFileDate());
        viewHolder.fileSizeTv.setText(mItemList.get(position).getFileSize());
        viewHolder.imageView.setImageResource(mItemList.get(position).getImageResId());
    }

    @Nullable
    @Override
    public FileItem getItem(int position) {
        return mItemList.get(position);
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
     * holder 패턴 적용을 위해 추가
     */
    class ViewHolder {
        private TextView fileNameTv;
        private TextView fileDateTv;
        private TextView fileSizeTv;
        private ImageView imageView;
    }
}
