package com.example.securepass.ui.scanner;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.securepass.databinding.FragmentScannerBinding;

public class ScannerFragment extends Fragment {

    private FragmentScannerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScannerViewModel scannerViewModel =
                new ViewModelProvider(this).get(ScannerViewModel.class);

        binding = FragmentScannerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textScanner;
        scannerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}