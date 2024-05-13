package com.example.securepass.ui.passwords;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;
import com.example.securepass.databinding.FragmentFolderPasswordsBinding;
import com.example.securepass.databinding.FragmentPasswordsBinding;
import com.example.securepass.ui.passwords.PasswordAdapter;

public class FolderPasswordsFragment extends Fragment {
    private FragmentFolderPasswordsBinding binding;
    private ListView listView;
    private DBHelper dbHelper;
    private int userId;
    private int folderId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFolderPasswordsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView title = root.findViewById(R.id.text_folder_name);

        listView = root.findViewById(R.id.passwords_list);

        dbHelper = new DBHelper(requireContext());

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        Bundle bundle = getArguments();

        if (userId != -1 && bundle != null) {
            folderId = bundle.getInt("folderId");

            String folderTitle = dbHelper.getFolderTitleById(folderId);

            title.setText(folderTitle);

            Cursor cursor = dbHelper.getPasswordsForUserAndFolder(userId, folderId);
            if (cursor != null) {
                PasswordAdapter adapter = new PasswordAdapter(requireContext(), cursor, R.layout.password_item);
                listView.setAdapter(adapter);
            } else {
                Log.e("FolderPasswordsFragment", "Cursor is null");
            }
        } else {
            Log.e("FolderPasswordsFragment", "Invalid user ID");
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Cursor cursor = (Cursor) parent.getItemAtPosition(position);

            int passwordId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_ID));

            NavController navController = Navigation.findNavController(view);

            Bundle bundle1 = new Bundle();
            bundle1.putInt("passwordId", passwordId);

            navController.popBackStack(R.id.navigation_passwords, true);
            navController.navigate(R.id.navigation_single_password, bundle1);
        });

        TextView deleteButton = root.findViewById(R.id.delete_folder_button);

        deleteButton.setOnClickListener(v -> {
            dbHelper.deleteFolder(folderId);

            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
            navController.navigate(R.id.navigation_folders);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}