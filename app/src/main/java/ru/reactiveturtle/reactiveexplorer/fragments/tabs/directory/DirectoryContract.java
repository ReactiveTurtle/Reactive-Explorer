package ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory;

import java.io.File;

public interface DirectoryContract {
    interface View {

        void updateFilesList(File file);

        void setSelectFileItem(int position, boolean isSelect);

        void setPresenter(Presenter presenter);

        void showRenameDialogForAlone(int position, File file);
    }

    interface Presenter {

        void onFileClicked(int position, File file);

        void onFileLongClicked(int position, File file);

        void onFileRenamed(String result, String rule);
    }
}
