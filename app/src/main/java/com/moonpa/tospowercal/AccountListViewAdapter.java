package com.moonpa.tospowercal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AccountListViewAdapter extends BaseAdapter
{
    LayoutInflater layoutInflater;
    List<AccountSettingList> list;

    public AccountListViewAdapter(Context context, List<AccountSettingList> list)
    {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return list.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        viewHolder holder = null;
        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.fragment_acc_listview,null);

            holder = new viewHolder(
                    (TextView) convertView.findViewById(R.id.fgmAcc_tv_listVTitle),
                    (TextView) convertView.findViewById(R.id.fgmAcc_tv_listVContent)
            );
            convertView.setTag(holder);
        }
        else
            holder = (viewHolder) convertView.getTag();

        AccountSettingList accountSettingList = (AccountSettingList) getItem(position);

        holder.title.setText(accountSettingList.getTitle());
        holder.content.setText(accountSettingList.getContent());

        return convertView;
    }

    private class viewHolder
    {
        TextView title,content;

        public viewHolder(TextView title,TextView content)
        {
            this.title = title;
            this.content = content;
        }
    }
}
