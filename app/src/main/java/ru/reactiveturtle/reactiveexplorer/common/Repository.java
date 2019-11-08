package ru.reactiveturtle.reactiveexplorer.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.util.Objects;

public class Repository {
    private SharedPreferences mPreferences;

    private static final String PREFERENCES = "preferences";

    private static final String TAB_DIRECTORIES = "tab_directories";
    private static final String TAB_DIRECTORY_POSITION = "tab_directory_position";

    public Repository(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return mPreferences.edit();
    }

    public void addDirectory(String path) {
        getEditor().putString(TAB_DIRECTORIES, getSingleLineDirectories() + ":" + path).commit();
    }

    public void setDirectory(int position, String filePath) {
        String[] directories = getDirectories();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < directories.length; i++) {
            builder.append(i == 0 ? "" : ":");
            if (i == position) builder.append(filePath);
            else builder.append(directories[i]);
        }
        if (builder.length() == 0)
            builder.append(Environment.getExternalStorageDirectory().getAbsolutePath());

        getEditor().putString(TAB_DIRECTORIES, String.valueOf(builder)).commit();
    }

    public void removeDirectory(int position) {
        String[] directories = getDirectories();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < directories.length; i++) {
            if (i != position)
                builder.append(i == 0 ? "" : ":").append(directories[i]);
        }
        if (builder.length() == 0)
            builder.append(Environment.getExternalStorageDirectory().getAbsolutePath());

        getEditor().putString(TAB_DIRECTORIES, String.valueOf(builder)).commit();
    }

    public String[] getDirectories() {
        return getSingleLineDirectories().split(":");
    }

    private String getSingleLineDirectories() {
        return mPreferences.getString(TAB_DIRECTORIES,
                Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public void setSelectedTabDirectoryPosition(int position) {
        getEditor().putInt(TAB_DIRECTORY_POSITION, position).commit();
    }

    public int getSelectedTabDirectoryPosition() {
        return mPreferences.getInt(TAB_DIRECTORY_POSITION, 0);
    }
}
