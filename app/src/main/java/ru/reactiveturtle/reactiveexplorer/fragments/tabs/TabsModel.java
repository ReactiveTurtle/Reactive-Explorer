package ru.reactiveturtle.reactiveexplorer.fragments.tabs;

import java.io.File;

public class TabsModel implements TabsContract.Model {
    public static final int FOLDER_CREATE = 0;
    public static final int REWRITE = 1;
    public static final int PASTE = 2;
    public static final int OBJECT_NOT_EXISTS = 3;

    private boolean mIsSelectMode = false;
    private String[] paths;
    private String[] filesBuffer = new String[0];

    private boolean isCopied = false;

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
    public void putFilesToBuffer(String[] files, boolean isCopied) {
        filesBuffer = files;
        this.isCopied = isCopied;
    }

    @Override
    public void clearFilesBuffer() {
        filesBuffer = new String[0];
    }

    @Override
    public String[] getFilesBuffer() {
        return filesBuffer;
    }

    @Override
    public boolean isFilesCopied() {
        return isCopied;
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
