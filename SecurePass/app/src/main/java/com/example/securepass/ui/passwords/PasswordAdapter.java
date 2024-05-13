package com.example.securepass.ui.passwords;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.securepass.R;


public class PasswordAdapter extends CursorAdapter {

    private Context mContext;
    private int mResource;

    public PasswordAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mContext = context;
        mResource = flags;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(mResource, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleTextView = view.findViewById(R.id.password_item_title);
        TextView emailTextView = view.findViewById(R.id.password_item_email);
        TextView passwordTextView = view.findViewById(R.id.password_item_password);

        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email_username"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

        titleTextView.setText(title);
        emailTextView.setText(email);
    }
}