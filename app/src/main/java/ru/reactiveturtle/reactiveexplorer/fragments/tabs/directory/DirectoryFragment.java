package ru.reactiveturtle.reactiveexplorer.fragments.tabs.directory;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Arrays;

import ru.reactiveturtle.reactiveexplorer.R;
import ru.reactiveturtle.reactiveexplorer.common.NameDialog;
import ru.reactiveturtle.reactiveexplorer.common.warning.WarningDialog;
import ru.reactiveturtle.reactiveexplorer.common.warning.WarningDialogBuilder;
import ru.reactiveturtle.reactiveexplorer.fragments.tabs.ModifiedLinearSmoothScroller;
import ru.reactiveturtle.reactiveexplorer.fragments.tabs.Sorter;

public class DirectoryFragment extends Fragment implements DirectoryContract.View {
    private DirectoryContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private DirectoryAdapter mAdapter;

    public static DirectoryFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString("path", path);
        DirectoryFragment fragment = new DirectoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new DirectoryAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Create: " + hashCode());
        View root = inflater.inflate(R.layout.directory_fragment_content, container, false);
        mRecyclerView = root.findViewById(R.id.directory_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            updateFilesList(new File(getArguments().getString("path")));
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.getItemAnimator().setChangeDuration(0);
            mAdapter.setOnItemClickListener((position, file) -> {
                System.out.println(DirectoryFragment.this.hashCode());
                if (mPresenter != null) {
                    mPresenter.onFileClicked(position, file);
                }
            });
            mAdapter.setOnItemLongClickListener((position, file) -> {
                if (mPresenter != null) {
                    mPresenter.onFileLongClicked(position, file);
                }
            });
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            //myView.setBackgroundResource(backgroundResource);
        }
        return root;
    }

    @Override
    public void updateFilesList(File file) {
        File[] files = file.listFiles();
        System.out.println(file.getAbsolutePath());
        Arrays.sort(files, new Sorter.NameComaparator());
        Arrays.sort(files, new Sorter.FileComaparator());
        if (mAdapter != null)
            mAdapter.setItems(files);
        if (getArguments() != null)
            getArguments().putString("path", file.getAbsolutePath());
    }

    @Override
    public void setSelectFileItem(int position, boolean isSelect) {
        mAdapter.setSelectItem(position, isSelect);
    }

    @Override
    public void setPresenter(DirectoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRenameDialogForAlone(int position, File file, int itemId) {
        if (mRecyclerView.getLayoutManager() != null) {
            ModifiedLinearSmoothScroller smoothScroller = new ModifiedLinearSmoothScroller(getContext());
            smoothScroller.setTargetPosition(position);
            smoothScroller.setOnSmoothScrollListener(new ModifiedLinearSmoothScroller.OnSmoothScrollListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onStop() {
                    smoothScroller.setOnSmoothScrollListener(null);
                    showNameDialog(file.getName(),
                            "Введите название " + (file.isDirectory() ? "папки" : "файла"), InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE,
                            "Переименовать", "Отмена", itemId);
                }
            });
            mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
        }
    }

    @Override
    public void showNameDialog(String text, String hintText, int inputType, String leftButton, String rightButton, int itemId) {
        NameDialog dialog = NameDialog.newInstance(text, hintText, inputType, leftButton, rightButton);
        dialog.setOnClickListener(new NameDialog.OnClickListener() {
            @Override
            public void onLeftButtonClicked(String result) {
                mPresenter.onNameDialogResult(result, itemId);
            }

            @Override
            public void onRightButtonClicked() {

            }
        });
        if (getFragmentManager() != null) {
            dialog.show(getFragmentManager(), "change_dialog");
        }
    }
}
