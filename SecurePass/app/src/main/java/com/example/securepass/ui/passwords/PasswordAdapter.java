package com.example.securepass.ui.passwords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.securepass.R;

import java.util.List;

public class PasswordAdapter extends ArrayAdapter<String[]> {

    private Context mContext;
    private int mResource;

    public PasswordAdapter(Context context, int resource, List<String[]> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        String[] password = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.password_item_title);
        TextView emailTextView = convertView.findViewById(R.id.password_item_email);

        if (password != null) {
            titleTextView.setText(password[0]);
            emailTextView.setText(password[1]);
        }

        return convertView;
    }
}
