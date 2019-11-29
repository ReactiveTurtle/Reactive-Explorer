package ru.reactiveturtle.reactiveexplorer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.reactiveturtle.reactiveexplorer.common.progress.ProgressDialog;
import ru.reactiveturtle.reactiveexplorer.common.warning.WarningDialog;
import ru.reactiveturtle.reactiveexplorer.common.warning.WarningDialogBuilder;

public class FileCopyDialog extends ProgressDialog {
    private File[][] files = new File[2][0];
    private int lastFile = 0;
    private boolean isFolderCreateSkip = false, isFolderCreateSkipForAll = false;
    private boolean isRewrite = false, isRewriteForAll = false, isRewriteSkip = false, isRewriteSkipForAll = false;
    private boolean isFileCreateSkip = false, isFileCreateSkipForAll = false;
    private boolean isObjectNotExistsSkip = false, isObjectNotExistsSkipForAll = false;

    private OnEndListener onEndListener;

    public static FileCopyDialog newInstance(File[] src, File[] dst, boolean isCopy) {
        Bundle args = new Bundle();
        args.putInt("max", src.length);
        args.putBoolean("isCopy", isCopy);

        FileCopyDialog fragment = new FileCopyDialog();
        fragment.setFilesToCopy(src, dst);
        fragment.setArguments(args);
        return fragment;
    }

    private void setFilesToCopy(File[] src, File[] dst) {
        files[0] = src;
        files[1] = dst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        pasteFiles();
        return root;
    }

    private void pasteFiles() {
        for (int i = lastFile; i < files[0].length; i++) {
            lastFile = i;
            if (getArguments() != null) {
                WarningDialogBuilder builder = null;
                File src = files[0][i];
                File dst = files[1][i];
                if (src.exists()) {
                    if (getArguments().getBoolean("isCopy")) {
                        if (src.isDirectory()) {
                            if (!dst.exists() && !dst.mkdirs()) {
                                builder = new WarningDialogBuilder()
                                        .setId("warning_fail_folder_create")
                                        .setWarningMessage("Не удалось создать папку. Попробовать снова?")
                                        .setButtonsText("Да", "Пропустить", "Отмена");
                            }
                        } else {
                            try {
                                if (dst.exists()) {
                                    builder = new WarningDialogBuilder()
                                            .setId("warning_fail_file_rewrite")
                                            .setWarningMessage("Файл уже существует. Перезаписать?")
                                            .setButtonsText("Да", "Пропустить", "Отмена");
                                } else if (dst.getParentFile().exists() || dst.getParentFile().mkdirs()) {
                                    if (dst.createNewFile()) {
                                        FileInputStream inputStream = new FileInputStream(src);
                                        FileOutputStream outputStream = new FileOutputStream(dst);
                                        byte[] buffer = new byte[1024];
                                        while (inputStream.read(buffer) > 0) {
                                            outputStream.write(buffer);
                                        }
                                        inputStream.close();
                                        outputStream.close();
                                    } else {
                                        builder = new WarningDialogBuilder()
                                                .setId("warning_fail_file_create")
                                                .setWarningMessage("Не удалось создать файл. Попробовать снова?")
                                                .setButtonsText("Да", "Пропустить", "Отмена");
                                    }
                                } else {
                                    builder = new WarningDialogBuilder()
                                            .setId("warning_fail_folder_create")
                                            .setWarningMessage("Не удалось создать папку. Попробовать снова?")
                                            .setButtonsText("Да", "Пропустить", "Отмена");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (dst.exists()) {
                            builder = new WarningDialogBuilder()
                                    .setId("warning_fail_file_rewrite")
                                    .setWarningMessage("Файл уже существует. Перезаписать?")
                                    .setButtonsText("Да", "Пропустить", "Отмена");
                        } else if (!src.renameTo(dst)) {
                            builder = new WarningDialogBuilder()
                                    .setId("warning_fail_object_paste")
                                    .setWarningMessage("Не удалось переместить. Попробовать снова?")
                                    .setButtonsText("Да", "Пропустить", "Отмена");
                        }
                    }
                } else {
                    builder = new WarningDialogBuilder()
                            .setId("warning_object_not_exists")
                            .setWarningMessage((getArguments().getBoolean("isCopy") ? "Скопированный" : "Вырезанный") + " объект не обнаружен. Попробовать снова?")
                            .setButtonsText("Да", "Пропустить", "Отмена");
                }
                if (builder != null && getFragmentManager() != null) {
                    builder.setCheckBoxText("Применить для всех");

                    WarningDialog dialog = builder.build();
                    dialog.setOnClickListener(new WarningDialog.OnClickListener() {
                        @Override
                        public void onLeftButtonClicked(String dialogId, boolean isForAll) {

                        }

                        @Override
                        public void onMiddleButtonClicked(String dialogId, boolean isForAll) {

                        }

                        @Override
                        public void onRightButtonClicked() {
                            dismiss();
                        }
                    });
                    dialog.show(getFragmentManager(), "warning_dialog");
                    break;
                }
            }
            setProgress(i);
            setProgressName("Из: " + files[0][i].getAbsolutePath() + "\n" +
                    "В: " + files[1][i]);
            setProgressText((i + 1) + "/" + files[0].length);
            if (i == files[0].length - 1) {
                dismiss();
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onEndListener != null) {
            onEndListener.onEnd();
        }
    }

    public void setOnEndListener(OnEndListener onEndListener) {
        this.onEndListener = onEndListener;
    }

    public interface OnEndListener {
        void onEnd();
    }
}
