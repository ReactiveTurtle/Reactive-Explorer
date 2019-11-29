package ru.reactiveturtle.reactiveexplorer.common.warning;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import ru.reactiveturtle.reactiveexplorer.R;

public class WarningDialog extends DialogFragment {
    private TextView textView;
    private CheckBox checkBox;
    private Button leftButton, middleButton, rightButton;

    private OnClickListener onClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.warning_fragment_content, container, false);
        textView = root.findViewById(R.id.warning_text_view);
        checkBox = root.findViewById(R.id.warning_checkbox);
        leftButton = root.findViewById(R.id.warning_left_button);
        middleButton = root.findViewById(R.id.warning_middle_button);
        rightButton = root.findViewById(R.id.warning_right_button);

        if (getArguments() != null) {
            textView.setText(getArguments().getString("message"));
            checkBox.setText(getArguments().getString("checkBoxText"));
            checkBox.setVisibility(getArguments().getBoolean("checkBoxIsVisible") ? View.VISIBLE : View.GONE);
            leftButton.setText(getArguments().getString("left"));
            middleButton.setText(getArguments().getString("middle"));
            rightButton.setText(getArguments().getString("right"));
            leftButton.setVisibility(getArguments().getBoolean("leftIsVisible") ? View.VISIBLE : View.GONE);
            middleButton.setVisibility(getArguments().getBoolean("middleIsVisible") ? View.VISIBLE : View.GONE);
            rightButton.setVisibility(getArguments().getBoolean("rightIsVisible") ? View.VISIBLE : View.GONE);
            leftButton.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onLeftButtonClicked(getArguments().getString("id"), checkBox.isChecked());
                }
            });
            middleButton.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onMiddleButtonClicked(getArguments().getString("id"), checkBox.isChecked());
                }
            });
            rightButton.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onRightButtonClicked();
                }
            });
        }

        return root;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onLeftButtonClicked(String dialogId, boolean isForAll);

        void onMiddleButtonClicked(String dialogId, boolean isForAll);

        void onRightButtonClicked();
    }
}
