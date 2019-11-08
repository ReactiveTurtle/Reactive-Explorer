package ru.reactiveturtle.reactiveexplorer.common.selection;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.reactiveturtle.reactiveexplorer.R;
import ru.reactiveturtle.reactiveexplorer.common.Helper;

public class SelectionDialog extends DialogFragment {
    private SelectionAdapter adapter;
    public static SelectionDialog newInstance(String[] strings) {

        Bundle args = new Bundle();
        args.putStringArray("strings", strings);
        args.putInt("recycler_view_position", 0);

        SelectionDialog fragment = new SelectionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static SelectionDialog newInstance(String title, String[] strings) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArray("strings", strings);
        args.putInt("recycler_view_position", 0);

        SelectionDialog fragment = new SelectionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static SelectionDialog newInstance(String title, String[] strings, int position) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArray("strings", strings);
        args.putInt("recycler_view_position", position);

        SelectionDialog fragment = new SelectionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.selection_dialog_fragment, container, false);

        if (getDialog().getWindow() != null) {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(Helper.unitToPixels(32, TypedValue.COMPLEX_UNIT_DIP, getResources()));
            getDialog().getWindow().setBackgroundDrawable(gd);
        }

        RecyclerView recyclerView = view.findViewById(R.id.selection_fragment_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
        dividerItemDecoration.setDrawable(new ColorDrawable(Color.GRAY));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        if (getArguments() != null) {
            if (getArguments().getString("title") == null)
                view.findViewById(R.id.select_dialog_title).setVisibility(View.GONE);
            else {
                TextView textView = view.findViewById(R.id.select_dialog_title);
                textView.setText(getArguments().getString("title"));
            }
            adapter.setItems(getArguments().getStringArray("strings"));
            recyclerView.scrollToPosition(getArguments().getInt("recycler_view_position"));
        }
        return view;
    }

    public void setOnItemClickListener(SelectionAdapter.OnItemClickListener onItemClickListener) {
        adapter = new SelectionAdapter();
        adapter.setOnItemClickListener(onItemClickListener);
    }
}
