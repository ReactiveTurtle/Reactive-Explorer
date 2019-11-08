package ru.reactiveturtle.reactiveexplorer.fragments.tabs;

import android.os.Environment;

import java.io.File;
import java.util.Arrays;

import ru.reactiveturtle.reactiveexplorer.R;
import ru.reactiveturtle.reactiveexplorer.common.Repository;
import ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory.DirectoryContract;

public class TabsPresenter implements TabsContract.Presenter, DirectoryContract.Presenter {
    private TabsContract.View mView;
    private Repository mRepository;
    private TabsContract.Model mModel;

    private DirectoryContract.View mFragment;

    TabsPresenter(TabsContract.View view, Repository repository, TabsContract.Model model) {
        mView = view;
        mRepository = repository;
        mModel = model;
    }

    /* Tabs section */
    @Override
    public void onViewCreated() {
        /* Здесь добавляются все сохранённые вкладки из Repository в Adapter ViewPager */
        if (mView != null) {
            String[] dirs = mRepository.getDirectories();
            for (int i = 0; i < dirs.length; i++) {
                mView.addDirectoryFragmentBefore(dirs[i], i - 1);
            }
            mView.showFragmentAt(mRepository.getSelectedTabDirectoryPosition());
            mView.updateTabTitle();
        }
    }

    @Override
    public void onViewResumed() {
        File file = new File(mRepository.getDirectories()[mRepository.getSelectedTabDirectoryPosition()]);
        while (!file.exists()) {
            file = file.getParentFile();
        }
        mRepository.setDirectory(mRepository.getSelectedTabDirectoryPosition(), file.getAbsolutePath());
        mFragment.updateFilesList(file);

        if (mModel.isSelectMode()) {
            String[] paths = mModel.getFiles();
            mModel.prepareModel(file);
            File[] files = file.listFiles();
            Arrays.sort(files, new Sorter.NameComaparator());
            Arrays.sort(files, new Sorter.FileComaparator());
            for (int i = 0; i < files.length; i++) {
                for (String path : paths) {
                    if (files[i].getAbsolutePath().equals(path)) {
                        mModel.setSelectedFile(i, files[i].getAbsolutePath());
                        mFragment.setSelectFileItem(i, true);
                    }
                }
            }
        }
    }

    @Override
    public void onFragmentSelected(DirectoryContract.View fragment, int position) {
        if (mFragment != null) {
            if (mModel.isSelectMode()) {
                quitSelectMode();
            }
            mFragment.setPresenter(null);
        }
        mFragment = fragment;
        mFragment.setPresenter(this);
        mFragment.updateFilesList(new File(mRepository.getDirectories()[position]));

        mRepository.setSelectedTabDirectoryPosition(position);
        mView.sendUpdateToolbarTitle(mRepository.getDirectories()[mRepository.getSelectedTabDirectoryPosition()]);
    }

    @Override
    public void onFragmentRemoved(int position) {
        if (mFragment != null) {
            mFragment.setPresenter(null);
        }
        mFragment = null;

        int count = mRepository.getDirectories().length;
        mRepository.removeDirectory(position);
        if (position == 0 && count == 1) {
            mView.addDirectoryFragmentBefore(mRepository.getDirectories()[0], -1);
        }
        mView.removeFragmentAt(position);
    }

    @Override
    public void onTabReselected() {
        mView.showSelectionDialog();
    }

    @Override
    public void onAddDirectoryPath() {
        if (mView != null) {
            mRepository.addDirectory(Environment.getExternalStorageDirectory().getAbsolutePath());
            mView.addDirectoryFragmentBefore(Environment.getExternalStorageDirectory().getAbsolutePath(), mRepository.getSelectedTabDirectoryPosition());
            mRepository.setSelectedTabDirectoryPosition(mRepository.getSelectedTabDirectoryPosition() + 1);
            mView.showFragmentAt(mRepository.getSelectedTabDirectoryPosition());
        }
    }

    @Override
    public void onRenameFile() {
        String[] paths = mModel.getFiles();
        if (mModel.getSelectedFilesCount() == 1)
            for (int i = 0; i < paths.length; i++)
                if (paths[i] != null) {
                    mFragment.showRenameDialogForAlone(i, new File(mModel.getFiles()[i]));
                    break;
                }
    }

    @Override
    public boolean onBackPressed() {
        if (!mModel.isSelectMode()) {
            File file = new File(mRepository.getDirectories()[mRepository.getSelectedTabDirectoryPosition()]);
            if (file.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath()))
                return true;
            updateDirectory(file.getParentFile());
        } else {
            quitSelectMode();
        }
        return false;
    }

    /* Fragment section */
    @Override
    public void onFileClicked(int position, File file) {
        if (!mModel.isSelectMode()) {
            if (file.isDirectory()) {
                updateDirectory(file);
            } else {
                //checkUtilsForFile(file);
            }
        } else {
            if (mModel.isSelectedFile(position)) {
                mModel.setSelectedFile(position, null);
                mFragment.setSelectFileItem(position, false);
            } else {
                mModel.setSelectedFile(position, file.getAbsolutePath());
                mFragment.setSelectFileItem(position, true);
            }
            if (mModel.getSelectedFilesCount() == 0)
                quitSelectMode();
        }
    }

    @Override
    public void onFileLongClicked(int position, File file) {
        if (!mModel.isSelectMode()) {
            enterSelectMode();
        }
        onFileClicked(position, file);
    }

    @Override
    public void onFileRenamed(String result, String rule) {
        if (mModel.isSelectMode()) {

            quitSelectMode();
            mFragment.updateFilesList(new File(mRepository.getDirectories()[mRepository.getSelectedTabDirectoryPosition()]));
        }
    }

    private void updateDirectory(File file) {
        mRepository.setDirectory(mRepository.getSelectedTabDirectoryPosition(), file.getAbsolutePath());
        mFragment.updateFilesList(file);
        if (mView != null) {
            mView.updateTabTitle();
            mView.sendUpdateToolbarTitle(mRepository.getDirectories()[mRepository.getSelectedTabDirectoryPosition()]);
        }
    }

    private void enterSelectMode() {
        mModel.prepareModel(new File(mRepository.getDirectories()[mRepository.getSelectedTabDirectoryPosition()]));
        mModel.setSelectMode(true);
        mView.showBottomNavigationViewMenu(R.menu.tabs_bottom_navigation_view_edit_menu);
    }

    private void quitSelectMode() {
        mModel.setSelectMode(false);
        String[] paths = mModel.getFiles();
        for (int i = 0; i < paths.length; i++) {
            if (paths[i] != null) {
                mFragment.setSelectFileItem(i, false);
            }
        }
        mView.showBottomNavigationViewMenu(R.menu.tabs_bottom_navigation_view_add_menu);
    }
}
