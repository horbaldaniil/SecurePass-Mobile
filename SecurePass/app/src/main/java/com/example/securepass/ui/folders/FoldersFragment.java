package com.example.securepass.ui.folders;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;
import com.example.securepass.databinding.FragmentFoldersBinding;


import java.util.ArrayList;
import java.util.List;

public class FoldersFragment extends Fragment {

    private FragmentFoldersBinding binding;
    private ListView listView;
    private DBHelper dbHelper;
    private int userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFoldersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.folders_list);
        dbHelper = new DBHelper(requireContext());

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        refreshFolderList();

        ImageButton addButton = root.findViewById(R.id.add_folder_button);
        addButton.setOnClickListener(v -> showAddFolderDialog());

        return root;
    }

    private void refreshFolderList() {
        List<String> folders = new ArrayList<>();
        Cursor cursor = dbHelper.getFoldersForUser(userId);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String folderTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_FOLDER_TITLE));
                folders.add(folderTitle);
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.folder_item, R.id.folder_item_title, folders);
        listView.setAdapter(adapter);
    }

    private void showAddFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Folder");

        // Set up the input
        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String folderTitle = input.getText().toString();
            dbHelper.addFolder(folderTitle, userId); // Add folder to the database
            refreshFolderList();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}