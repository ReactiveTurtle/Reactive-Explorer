package ru.reactiveturtle.reactiveexplorer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory.DirectoryContract;
import ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory.DirectoryFragment;

public class FragmentsPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();

    private OnFragmentListener mOnFragmentListener;

    public FragmentsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Bundle bundle = mFragments.get(position).getArguments();
        return bundle != null ? new File(bundle.getString("path")).getName() : "";
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void addFragmentBefore(Fragment fragment, int position) {
        mFragments.add(null);
        for (int i = mFragments.size() - 1; i > position + 1; i--) {
            mFragments.set(i, mFragments.get( i - 1));
        }
        mFragments.set(position + 1, fragment);
        notifyDataSetChanged();
    }

    public void setFragment(int position, Fragment fragment) {
        mFragments.set(position, fragment);
    }

    public void removeFragment(int position) {
        mFragments.remove(position);
        notifyDataSetChanged();
    }

    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        mOnFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener {
        void onCreate(DirectoryContract.View fragment);
    }
}
