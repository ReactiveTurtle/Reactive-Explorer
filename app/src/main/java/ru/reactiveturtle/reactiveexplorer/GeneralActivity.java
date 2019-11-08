package ru.reactiveturtle.reactiveexplorer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ru.reactiveturtle.reactiveexplorer.common.Permissions;
import ru.reactiveturtle.reactiveexplorer.fragments.permission.PermissionFragment;
import ru.reactiveturtle.reactiveexplorer.fragments.settings.SettingsFragment;
import ru.reactiveturtle.reactiveexplorer.fragments.start.StartFragment;
import ru.reactiveturtle.reactiveexplorer.fragments.tabs.TabsContract;
import ru.reactiveturtle.reactiveexplorer.fragments.tabs.TabsFragment;

public class GeneralActivity extends AppCompatActivity {

    private TextView mSelectedPathTextView;

    private ViewPager mViewPager;
    private FragmentsPagerAdapter mFragmentsPagerAdapter;

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity);
        if (getSupportFragmentManager().getFragments().size() > 0) {
            Intent intent = new Intent(this, GeneralActivity.class);
            intent.putExtra("selected_page", savedInstanceState.getInt("selected_page", 0));
            startActivity(intent);
        }

        Toolbar toolbar = findViewById(R.id.general_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mSelectedPathTextView = findViewById(R.id.general_toolbar_selected_path);
        mSelectedPathTextView.setVisibility(View.GONE);

        initBottomNavigationView();
        initFragmentsViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("selected_page", mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permissions.WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFragmentsPagerAdapter.setFragment(0, new StartFragment());
                    mFragmentsPagerAdapter.setFragment(0, new TabsFragment());
                } else {
                    for (int i = 0; i < 2; i++) {
                        mFragmentsPagerAdapter.setFragment(2, PermissionFragment.newInstance(
                                "Для полного функционирования приложению требуется разрешение" +
                                        " на запись в память устройства"));
                    }
                }
                mFragmentsPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentsPagerAdapter.getItem(mViewPager.getCurrentItem()).getClass().getName().equals(TabsFragment.class.getName())) {
            TabsContract.View fragment = (TabsContract.View) mFragmentsPagerAdapter.getItem(mViewPager.getCurrentItem());
            if (fragment.onBackPressed())
                super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void initFragmentsViewPager() {
        mViewPager = findViewById(R.id.general_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        mSelectedPathTextView.setVisibility(View.GONE);
                        mBottomNavigationView.setSelectedItemId(R.id.action_general_page);
                        break;
                    case 1:
                        mSelectedPathTextView.setVisibility(View.VISIBLE);
                        mBottomNavigationView.setSelectedItemId(R.id.action_pages);
                        break;
                    case 2:
                        mSelectedPathTextView.setVisibility(View.GONE);
                        mBottomNavigationView.setSelectedItemId(R.id.action_settings);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mFragmentsPagerAdapter = new FragmentsPagerAdapter(getSupportFragmentManager());
        if (Permissions.hasStoragePermission(this)) {
            TabsFragment tabsFragment = new TabsFragment();
            tabsFragment.setOnImportantChangeListener(new TabsFragment.OnImportantChangeListener() {
                @Override
                public void onChangeTitle(String title) {
                    mSelectedPathTextView.setText(title);
                }
            });
            mFragmentsPagerAdapter.addFragment(new StartFragment());
            mFragmentsPagerAdapter.addFragment(tabsFragment);
        } else {
            mFragmentsPagerAdapter.addFragment(new Fragment());
            mFragmentsPagerAdapter.addFragment(new Fragment());
            Permissions.requestStoragePermission(this);
        }
        mFragmentsPagerAdapter.addFragment(new SettingsFragment());
        mViewPager.setAdapter(mFragmentsPagerAdapter);
        mFragmentsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(getIntent().getIntExtra("selected_page", 0));
    }

    private void initBottomNavigationView() {
        mBottomNavigationView = findViewById(R.id.general_bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener((menuItem) -> {
            switch (menuItem.getItemId()) {
                case R.id.action_general_page:
                    mViewPager.setCurrentItem(0, true);
                    break;
                case R.id.action_pages:
                    mViewPager.setCurrentItem(1, true);
                    break;
                case R.id.action_settings:
                    mViewPager.setCurrentItem(2, true);
                    break;
            }
            return true;
        });
    }
}
