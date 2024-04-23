package com.example.dimeight;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dimeight.adapter.FriendAdapter;
import com.example.dimeight.helper.FriendDB;
import com.example.dimeight.model.Friend;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Friend> mList = new ArrayList<>();

    private FriendAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        mAdapter = new FriendAdapter(mList, this);
        ListView listView = findViewById(R.id.listview);

        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(this::itemLongClick);

        findViewById(R.id.fab).setOnClickListener(v -> createDialog(null).show());
    }

    private BottomSheetDialog createDialog(Bundle bundle) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this)
                .inflate(R.layout.new_friend, null, false);

        TextInputEditText tieName = view.findViewById(R.id.tie_name);
        TextInputEditText tieAddress = view.findViewById(R.id.tie_address);
        TextInputEditText tiePhone = view.findViewById(R.id.tie_phone);
        Button btAdd = view.findViewById(R.id.bt_add);

        boolean isUpdate = false;
        if (bundle != null) {
            tieName.setText(bundle.getString("name"));
            tieAddress.setText(bundle.getString("address"));
            tiePhone.setText(bundle.getString("phone"));
            isUpdate = true;
        }

        final boolean finalIsUpdate = isUpdate;

        btAdd.setOnClickListener(v -> {
            if (tieName.getText().toString().isEmpty()) {
                Snackbar.make(v, "Name cannot empty", Snackbar.LENGTH_SHORT).show();
                return;
            }

            try (FriendDB db = new FriendDB(this)) {

                if (finalIsUpdate) {
                    //TODO Update
                    Friend friend = new Friend(
                            bundle.getInt("id"),
                            tieName.getText().toString(),
                            tieAddress.getText().toString(),
                            tiePhone.getText().toString());
                    db.update(friend);
                    mList.set(bundle.getInt("pos"), friend);
                } else {
                    //TODO Insert
                    Friend friend = new Friend(
                            tieName.getText().toString(),
                            tieAddress.getText().toString(),
                            tiePhone.getText().toString());
                    int id = (int) db.insert(friend);
                    friend.setId(id);
                    mList.add(friend);
                }
            }

            mAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.setContentView(view);
        return dialog;
    }

    private boolean itemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CharSequence[] items = {"Edit", "Delete"};
        int[] checked = {-1};
        new AlertDialog.Builder(this)
                .setTitle("Your Action:")
                .setSingleChoiceItems(items, checked[0], (dialog, which) -> checked[0] = which)
                .setPositiveButton("Push", (dialog, which) -> {
                    switch (checked[0]) {
                        case 0: //TODO Edit
                            Bundle bundle = new Bundle();
                            bundle.putInt("pos", i);
                            bundle.putInt("id", mList.get(i).getId());
                            bundle.putString("name", mList.get(i).getName());
                            bundle.putString("address", mList.get(i).getAddress());
                            bundle.putString("phone", mList.get(i).getPhone());
                            createDialog(bundle).show();
                            break;
                        case 1: //TODO Delete
                            new AlertDialog.Builder(this)
                                    .setTitle("Confirm")
                                    .setMessage(String.format("Delete %s ?", mList.get(i).toString()))
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("Yes", (dialog1, which1) -> {
                                        try (FriendDB db = new FriendDB(this)) {
                                            db.delete(mList.get(i).getId());
                                            mList.remove(mList.get(i));
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }).show();
                            break;
                    }
                }).show();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try (FriendDB db = new FriendDB(this)) {
            mList.clear();
            mList.addAll(db.getAllFriends());
            mAdapter.notifyDataSetChanged();
        }
    }
}