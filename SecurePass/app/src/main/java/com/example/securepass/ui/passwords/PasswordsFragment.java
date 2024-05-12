package com.example.securepass.ui.passwords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.securepass.R;
import com.example.securepass.databinding.FragmentPasswordsBinding;
import com.example.securepass.ui.singlePassword.SinglePasswordFragment;

import java.util.Arrays;

public class PasswordsFragment extends Fragment {

    private FragmentPasswordsBinding binding;

    private String[][] passwords = new String[][] {{"Title1", "your@email.com"}, {"Title2", "your2@email.com"}, {"Title3", "your3@email.com"}};
    private ListView listView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPasswordsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.passwords_list);

        PasswordAdapter adapter = new PasswordAdapter(root.getContext(), R.layout.password_item, Arrays.asList(passwords));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            String title = passwords[position][0];
            String email = passwords[position][1];

            NavController navController = Navigation.findNavController(view);

            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("email", email);

            navController.popBackStack(R.id.navigation_passwords, true);
            navController.navigate(R.id.navigation_single_password, bundle);
        });

        ImageButton addButton = root.findViewById(R.id.add_password_button);
        addButton.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);

            navController.popBackStack();
            navController.navigate(R.id.navigation_add_password);
        });



        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}