package ru.reactiveturtle.reactiveexplorer.common.selection;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.reactiveturtle.reactiveexplorer.R;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.ViewSelectionHolder> {
    private ArrayList<String> viewNames = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewSelectionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewSelectionHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selection_adapter_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSelectionHolder holder, int i) {
        holder.textView.setText(viewNames.get(i));
        System.out.println(Integer.toHexString(holder.textView.getTextColors().getDefaultColor()));
    }

    @Override
    public int getItemCount() {
        return viewNames.size();
    }

    public void setItems(String[] viewName) {
        viewNames.addAll(Arrays.asList(viewName));
        notifyDataSetChanged();
    }

    public class ViewSelectionHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private LinearLayout root;
        private TextView textView;

        @SuppressLint("ClickableViewAccessibility")
        ViewSelectionHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.selection_item_root);
            textView = itemView.findViewById(R.id.selection_item_text_view);
            itemView.setOnTouchListener(this);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                AlphaAnimation animation = new AlphaAnimation(1f, 0.25f);
                animation.setDuration(128);
                v.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setAlpha(0.25f);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (onItemClickListener != null) {
                    v.setAlpha(1f);
                    onItemClickListener.onItemClick(viewNames.get(getLayoutPosition()));
                }
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setAlpha(1f);
                AlphaAnimation animation = new AlphaAnimation(0.25f, 1f);
                animation.setDuration(256);
                v.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            return true;
        }
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String result);
    }
}
