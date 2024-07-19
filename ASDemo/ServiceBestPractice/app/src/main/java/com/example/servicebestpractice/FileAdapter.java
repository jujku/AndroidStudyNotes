package com.example.servicebestpractice;// FileAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebestpractice.R;
import com.example.servicebestpractice.enitiy.FileItem;

import java.util.List;

// 自定义适配器类，用于在RecyclerView中显示文件信息
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private Context mContext; // 上下文对象
    private List<FileItem> fileList; // 文件列表
    private TextView mSelectedFile;
    // 构造函数
    public FileAdapter(Context context, List<FileItem> files,TextView selectedFile) {
        mContext = context;
        fileList = files;
        mSelectedFile = selectedFile;
    }

    // 创建视图持有者
    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    // 绑定数据到视图持有者
    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileItem fileItem = fileList.get(position);
        holder.fileNameTextView.setText(fileItem.getName());
        holder.fileSizeTextView.setText(String.format("%.2f MB", fileItem.getSize()));

        // 设置点击事件，显示Toast消息
        holder.itemView.setOnClickListener(v -> {
            mSelectedFile.setText("你选中了" + fileItem.getName());
        });
    }

    // 获取数据项总数
    @Override
    public int getItemCount() {
        return fileList.size();
    }

    // 视图持有者类
    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView;
        TextView fileSizeTextView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileName);
            fileSizeTextView = itemView.findViewById(R.id.fileSize);
        }
    }
}
