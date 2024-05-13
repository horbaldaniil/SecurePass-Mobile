package com.example.securepass.ui.folders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepass.R;
import com.example.securepass.databinding.FragmentFoldersBinding;

public class FoldersFragment extends Fragment {

    private FragmentFoldersBinding binding;

    private String[] folders = new String[] {"Folder1","Folder2","Folder3"};
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FoldersViewModel foldersViewModel =
                new ViewModelProvider(this).get(FoldersViewModel.class);

        binding = FragmentFoldersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.folders_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), R.layout.folder_item, R.id.folder_item_title, folders);

        listView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}