package ru.reactiveturtle.reactiveexplorer.fragments.tabs;

import java.io.File;

public class TabsModel implements TabsContract.Model {
    private boolean mIsSelectMode = false;
    private String[] paths;
    private int selectedCount = 0;

    @Override
    public void prepareModel(File file) {
        paths = new String[file.listFiles().length];
    }

    @Override
    public void setSelectMode(boolean isSelectMode) {
        mIsSelectMode = isSelectMode;
        if (!isSelectMode)
            selectedCount = 0;
    }

    @Override
    public boolean isSelectMode() {
        return mIsSelectMode;
    }

    @Override
    public void setSelectedFile(int position, String absolutePath) {
        if (paths[position] == null && absolutePath != null) {
            selectedCount++;
        } else if (paths[position] != null && absolutePath == null) {
            selectedCount--;
        }
        paths[position] = absolutePath;
    }

    @Override
    public boolean isSelectedFile(int position) {
        return paths[position] != null;
    }

    @Override
    public String[] getFiles() {
        return paths;
    }

    @Override
    public int getSelectedFilesCount() {
        return selectedCount;
    }
}
