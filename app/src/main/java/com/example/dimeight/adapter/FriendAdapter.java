package com.example.dimeight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dimeight.model.Friend;

import java.util.List;

public class FriendAdapter extends BaseAdapter {
    private final List<Friend> mList;
    private final Context mContext;

    public FriendAdapter(List<Friend> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView = LayoutInflater.from(mContext)
                    .inflate(android.R.layout.simple_list_item_2, parent, false);

        TextView tvName = convertView.findViewById(android.R.id.text1);
        TextView tvAddress = convertView.findViewById(android.R.id.text2);

        tvName.setText(mList.get(position).getName());
        tvAddress.setText(mList.get(position).getAddress());

        return convertView;
    }
}
