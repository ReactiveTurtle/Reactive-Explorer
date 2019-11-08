package ru.reactiveturtle.reactiveexplorer.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.reactiveturtle.reactiveexplorer.R;

public class ChangeDialog extends DialogFragment {
    private AppCompatEditText editText;
    private OnClickListener onClickListener;

    public static ChangeDialog newInstance(String text, String hintText, int inputType, String leftButton, String rightButton) {
        Bundle args = new Bundle();
        args.putString("text", text);
        args.putString("hint_text", hintText);
        args.putInt("input_type", inputType);
        args.putString("left_button", leftButton);
        args.putString("right_button", rightButton);

        ChangeDialog fragment = new ChangeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog().getWindow() != null && getContext() != null) {
            getDialog().getWindow().setBackgroundDrawable(Helper.getRoundDrawable(Helper.getThemeColor(getContext(), R.attr.colorPrimaryDark),
                    getResources().getDimensionPixelSize(R.dimen.big_margin)));
        }

        View view = inflater.inflate(R.layout.change_dialog_fragment, container, false);
        editText = view.findViewById(R.id.change_edit_text);
        editText.setEnabled(true);
        editText.setClickable(true);
        editText.setFocusable(true);
        AppCompatButton leftButton = view.findViewById(R.id.left_button);
        leftButton.setOnClickListener(v -> {
            if (onClickListener != null && editText.getText().toString().length() > 0) {
                onClickListener.onLeftButtonClicked(editText.getText().toString());
                dismiss();
            }
        });
        AppCompatButton rightButton = view.findViewById(R.id.right_button);
        rightButton.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onRightButtonClicked();
                dismiss();
            }
        });

        if (getArguments() != null) {
            editText.setText(getArguments().getString("text"));
            editText.setHint(getArguments().getString("hint_text"));
            editText.setInputType(getArguments().getInt("input_type"));
            editText.requestFocus();
            editText.setSelected(true);
            editText.setSelection(0, editText.getText().toString().length());
            leftButton.setText(getArguments().getString("left_button"));
            rightButton.setText(getArguments().getString("right_button"));
        }
        return view;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onLeftButtonClicked(String result);
        void onRightButtonClicked();
    }
}
