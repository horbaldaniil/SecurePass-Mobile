package com.example.securepass.ui.folders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepass.databinding.FragmentFoldersBinding;

public class FoldersFragment extends Fragment {

    private FragmentFoldersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FoldersViewModel foldersViewModel =
                new ViewModelProvider(this).get(FoldersViewModel.class);

        binding = FragmentFoldersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textFolders;
        foldersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}