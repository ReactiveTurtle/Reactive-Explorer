package ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ru.reactiveturtle.reactiveexplorer.common.Helper;
import ru.reactiveturtle.reactiveexplorer.R;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {
    private File[] mFiles = new File[]{};
    private boolean[] mSelections = new boolean[]{};

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    @NonNull
    @Override
    public DirectoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DirectoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.directory_adapter_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DirectoryViewHolder holder, int position) {
        if (mSelections[position]) {
            int color = Helper.getThemeColor(holder.itemView.getContext(), R.attr.colorAccent);
            holder.itemView.setBackground(Helper.getRoundDrawable(Color.rgb(
                    (int) (Color.red(color) * 0.5f), (int) (Color.green(color) * 0.5f), (int) (Color.blue(color) * 0.5f)), holder.itemView.getResources().getDimensionPixelSize(R.dimen.big_margin)));
        } else {
            int color = Helper.getThemeColor(holder.itemView.getContext(), R.attr.directoryAdapterItemBackground);
            holder.itemView.setBackground(Helper.getRoundDrawable(color, holder.itemView.getResources().getDimensionPixelSize(R.dimen.big_margin)));
        }
        holder.name.setText(mFiles[position].getName());
        holder.fileIcon.setBackgroundResource(mFiles[position].isDirectory() ? R.drawable.ic_folder_open_black_48dp : R.drawable.ic_file_black_48dp);
        Helper.editDrawableToWhite(holder.fileIcon.getBackground());
    }

    @Override
    public int getItemCount() {
        return mFiles.length;
    }

    public void setItems(File[] files) {
        mFiles = files;
        mSelections = new boolean[files.length];
        notifyDataSetChanged();
    }

    public void setSelectItem(int position, boolean isSelect) {
        mSelections[position] = isSelect;
        notifyItemChanged(position);
    }

    public void clear() {
        mFiles = new File[0];
        notifyDataSetChanged();
    }

    class DirectoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private View fileIcon;
        private TextView name;
        private View mask;

        DirectoryViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.directory_adapter_name);
            name.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
            fileIcon = itemView.findViewById(R.id.directory_adapter_file_icon);
            mask = itemView.findViewById(R.id.directory_adapter_mask);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mask.setOnClickListener(this);
            mask.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getLayoutPosition(), mFiles[getLayoutPosition()]);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener.onItemLongClick(getLayoutPosition(), mFiles[getLayoutPosition()]);
            }
            return false;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, File file);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, File file);
    }
}
