package com.moonpa.tospowercal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PowerCalListViewAdapter extends BaseAdapter
{
    LayoutInflater layoutInflater;
    List<PowerCalList> PowerCalList;

    public PowerCalListViewAdapter(Context context, List<PowerCalList> PowerCalList)
    {
        layoutInflater = LayoutInflater.from(context);
        this.PowerCalList = PowerCalList;
    }

    @Override
    public int getCount()
    {
        return PowerCalList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return PowerCalList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return PowerCalList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        viewHolder holder = null;
        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.fragment_cal_listview,null);

            holder = new viewHolder(
                    (TextView) convertView.findViewById(R.id.fgmCal_tv_listVTime),
                    (TextView) convertView.findViewById(R.id.fgmCal_tv_listVPower)
            );
            convertView.setTag(holder);
        }
        else
            holder = (viewHolder) convertView.getTag();

        PowerCalList powerCalList = (PowerCalList)getItem(position);

        holder.time.setText(powerCalList.getMsg());
        holder.power.setText(powerCalList.getPower() + "");

        return convertView;
    }

    private class viewHolder
    {
        TextView time,power;

        public viewHolder(TextView time,TextView power)
        {
            this.time = time;
            this.power = power;
        }
    }
}
