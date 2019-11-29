package ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory;

import java.io.File;

import ru.reactiveturtle.reactiveexplorer.common.warning.WarningDialogBuilder;

public interface DirectoryContract {
    interface View {
        void updateFilesList(File file);

        void setSelectFileItem(int position, boolean isSelect);

        void setPresenter(Presenter presenter);

        void showRenameDialogForAlone(int position, File file, int itemId);

        void showNameDialog(String text, String hintText, int inputType, String leftButton, String rightButton, int itemId);
    }

    interface Presenter {
        void onFileClicked(int position, File file);

        void onFileLongClicked(int position, File file);

        void onNameDialogResult(String result, int itemId);
    }
}
