package ru.reactiveturtle.reactiveexplorer.common.progress;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.reactiveturtle.reactiveexplorer.R;

public class ProgressDialog extends DialogFragment {
    private TextView progressName, progressText;
    private ProgressBar progressBar;

    public static ProgressDialog newInstance(int maxProgress) {
        Bundle args = new Bundle();
        args.putInt("max", maxProgress);

        ProgressDialog fragment = new ProgressDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.progress_dialog_fragment, container, false);
        progressName = root.findViewById(R.id.progress_name_text_view);
        progressText = root.findViewById(R.id.progress_text_view);
        progressBar = root.findViewById(R.id.progress_bar);
        if (getArguments() != null) {
            progressBar.setMax(getArguments().getInt("max"));
        }
        return root;
    }

    public void setProgressName(String text) {
        progressName.setText(text);
    }

    public void setProgressText(String text) {
        progressText.setText(text);
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }
}
