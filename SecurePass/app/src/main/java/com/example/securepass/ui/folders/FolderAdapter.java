package com.example.securepass.ui.folders;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.securepass.R;

import java.util.List;

public class FolderAdapter extends CursorAdapter {

    private Context mContext;
    private int mResource;

    public FolderAdapter(Context context, Cursor cursor, int flags) {
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
        TextView titleTextView = view.findViewById(R.id.folder_item_title);

        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));

        titleTextView.setText(title);
    }
}