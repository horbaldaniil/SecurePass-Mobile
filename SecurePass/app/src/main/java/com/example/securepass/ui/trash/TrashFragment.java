package com.example.securepass.ui.trash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepass.R;
import com.example.securepass.databinding.FragmentTrashBinding;
import com.example.securepass.ui.passwords.PasswordAdapter;

import java.util.Arrays;

public class TrashFragment extends Fragment {

    private FragmentTrashBinding binding;

    private String[][] passwords = new String[][] {{"Title1", "your@email.com"}, {"Title2", "your2@email.com"}, {"Title3", "your3@email.com"}};
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrashViewModel trashViewModel =
                new ViewModelProvider(this).get(TrashViewModel.class);

        binding = FragmentTrashBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.trash_list);

        PasswordAdapter adapter = new PasswordAdapter(root.getContext(), R.layout.password_item, Arrays.asList(passwords));
        listView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}