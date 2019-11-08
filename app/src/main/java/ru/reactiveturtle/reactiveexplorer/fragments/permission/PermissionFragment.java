package ru.reactiveturtle.reactiveexplorer.fragments.permission;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.reactiveturtle.reactiveexplorer.common.Helper;
import ru.reactiveturtle.reactiveexplorer.common.Permissions;
import ru.reactiveturtle.reactiveexplorer.R;

public class PermissionFragment extends Fragment {
    public static PermissionFragment newInstance(String warning) {
        Bundle args = new Bundle();
        args.putString("warning", warning);
        PermissionFragment fragment = new PermissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.permission_fragment_content, container, false);
        TextView warningTextView = root.findViewById(R.id.permission_fragment_warning);
        if (getArguments() != null)
            warningTextView.setText(getArguments().getString("warning"));

        Button goButton = root.findViewById(R.id.permission_fragment_go_button);
        goButton.setBackground(Helper.getRoundDrawable(((ColorDrawable) goButton.getBackground()).getColor(),
                getResources().getDimensionPixelSize(R.dimen.big_margin)));
        goButton.setOnClickListener(v -> {
            Permissions.requestStoragePermission(getActivity());
        });
        return root;
    }
}
