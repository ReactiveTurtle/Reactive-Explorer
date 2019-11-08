package ru.reactiveturtle.reactiveexplorer.fragments.tabs;

import java.io.File;
import java.util.ArrayList;

import ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory.DirectoryContract;

public interface TabsContract {
    interface View {
        void updateTabTitle();

        void showSelectionDialog();

        void addDirectoryFragmentBefore(String dir, int position);

        void showFragmentAt(int position);

        void removeFragmentAt(int position);

        void showBottomNavigationViewMenu(int menuId);

        void sendUpdateToolbarTitle(String path);

        boolean onBackPressed();
    }

    interface Presenter {
        void onViewCreated();

        void onViewResumed();

        void onFragmentSelected(DirectoryContract.View fragment, int position);

        void onFragmentRemoved(int position);

        void onTabReselected();

        void onAddDirectoryPath();

        void onRenameFile();

        boolean onBackPressed();
    }

    interface Model {
        void prepareModel(File file);

        void setSelectMode(boolean isSelectMode);

        boolean isSelectMode();

        void setSelectedFile(int position, String absolutePath);

        boolean isSelectedFile(int position);

        String[] getFiles();

        int getSelectedFilesCount();
    }
}
