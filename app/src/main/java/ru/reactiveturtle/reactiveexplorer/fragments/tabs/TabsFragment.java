package ru.reactiveturtle.reactiveexplorer.fragments.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.reactiveturtle.reactiveexplorer.FragmentsPagerAdapter;
import ru.reactiveturtle.reactiveexplorer.R;
import ru.reactiveturtle.reactiveexplorer.common.Repository;
import ru.reactiveturtle.reactiveexplorer.common.selection.SelectionDialog;
import ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory.DirectoryContract;
import ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory.DirectoryFragment;

public class TabsFragment extends Fragment implements TabsContract.View {
    private TabsContract.Presenter mPresenter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentsPagerAdapter mFragmentsPagerAdapter;
    private BottomNavigationView mBottomNavigationView;

    private OnImportantChangeListener mOnImportantChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onViewResumed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tabs_fragment_content, container, false);
        mTabLayout = root.findViewById(R.id.tabs_fragment_tab_layout);
        mViewPager = root.findViewById(R.id.tabs_fragment_view_pager);
        System.out.println(mViewPager + "hash: " + hashCode());
        mFragmentsPagerAdapter = new FragmentsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mFragmentsPagerAdapter);

        mBottomNavigationView = root.findViewById(R.id.tab_bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_create_new_folder:
                    break;
                case R.id.action_create_new_file:
                    break;
                case R.id.action_rename:
                    mPresenter.onRenameFile();
                    break;
                case R.id.action_tab_add:
                    mPresenter.onAddDirectoryPath();
                    break;
            }
            return true;
        });

        if (getContext() != null) {
            Repository repository = new Repository(getContext());
            mPresenter = new TabsPresenter(this, repository, new TabsModel());
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    mPresenter.onFragmentSelected((DirectoryContract.View) mFragmentsPagerAdapter.getItem(i), i);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    mPresenter.onTabReselected();
                }
            });
            mPresenter.onViewCreated();
            if (mViewPager.getCurrentItem() == 0)
                mPresenter.onFragmentSelected((DirectoryContract.View) mFragmentsPagerAdapter.getItem(0), 0);
        }
        return root;
    }

    @Override
    public void updateTabTitle() {
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void showSelectionDialog() {
        if (getFragmentManager() != null) {
            SelectionDialog dialog = SelectionDialog.newInstance("Выберите действие:",
                    new String[]{"Создать файл", "Создать папку", "Удалить текущую вкладку"});
            dialog.setOnItemClickListener(result -> {
                switch (result) {
                    case "Удалить текущую вкладку":
                        mPresenter.onFragmentRemoved(mViewPager.getCurrentItem());
                        break;
                }
                dialog.dismiss();
            });
            dialog.show(getFragmentManager(), "tab_action_selection_dialog");
        }
    }

    @Override
    public void addDirectoryFragmentBefore(String dir, int position) {
        System.out.println("Tab: " + hashCode());
        mFragmentsPagerAdapter.addFragmentBefore(DirectoryFragment.newInstance(dir), position);
    }

    @Override
    public void showFragmentAt(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void removeFragmentAt(int position) {
        mFragmentsPagerAdapter.removeFragment(position);
        mPresenter.onFragmentSelected((DirectoryContract.View) mFragmentsPagerAdapter.getItem(mViewPager.getCurrentItem()), mViewPager.getCurrentItem());
    }

    @Override
    public void showBottomNavigationViewMenu(int menuId) {
        if (getActivity() != null) {
            mBottomNavigationView.getMenu().clear();
            getActivity().getMenuInflater().inflate(menuId, mBottomNavigationView.getMenu());
        }
    }

    @Override
    public void setBottomNavigationViewItemVisible(int itemId, boolean isVisible) {
        System.out.println(mBottomNavigationView.getMenu().size());
        mBottomNavigationView.getMenu().findItem(itemId).setVisible(isVisible);
    }

    @Override
    public void sendUpdateToolbarTitle(String path) {
        if (mOnImportantChangeListener != null) {
            mOnImportantChangeListener.onChangeTitle(path);
        }
    }

    @Override
    public boolean onBackPressed() {
        System.out.println(mPresenter);
        return mPresenter.onBackPressed();
    }

    public void setOnImportantChangeListener(OnImportantChangeListener onImportantChangeListener) {
        mOnImportantChangeListener = onImportantChangeListener;
    }

    public interface OnImportantChangeListener {
        void onChangeTitle(String title);
    }
}
