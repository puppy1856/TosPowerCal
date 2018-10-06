package com.moonpa.tospowercal;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class AlarmFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener
{
    final String SETTING = "setting";
    final String DATABASE_NAME = "alarm.db";
    final String CHANNEL_ID = "TosPowerCal";
    final int DATABASE_VERISON = 5;
    boolean vibrate,voice;
    private NotificationManager noMgr;
    SharedPreferences setting;
    ListView listView;
    Button btn_claen,btn_refresh;
    ToggleButton togBtn_vibrate,togBtn_voice;
    PowerCalListViewAdapter adapter;
    AlarmListSQL alarmM;
    SwipeRefreshLayout refreshLayout;
    Context activity;
    View V;
    List<PowerCalList> powerCalLists;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_alarm, container, false);
        activity = this.getActivity();
        noMgr = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        listView = (ListView) V.findViewById(R.id.fgmAla_listV);
        togBtn_vibrate = (ToggleButton) V.findViewById(R.id.fgmAla_togBtn_vibrate);
        togBtn_voice = (ToggleButton) V.findViewById(R.id.fgmAla_togBtn_voice);
        btn_claen = (Button) V.findViewById(R.id.fgmAla_btn_claen);
        btn_refresh = (Button) V.findViewById(R.id.fgmAla_btn_refresh);
        refreshLayout = (SwipeRefreshLayout) V.findViewById(R.id.fgmAla_refreshLayout);

        setting = this.getActivity().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        vibrate = setting.getBoolean("vibrate",true);
        voice = setting.getBoolean("voice",true);

        togBtn_vibrate.setChecked(vibrate ? false : true);
        togBtn_voice.setChecked(voice ? false : true);

        btn_claen.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        togBtn_vibrate.setOnCheckedChangeListener(this);
        togBtn_voice.setOnCheckedChangeListener(this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshListView();
                refreshLayout.setRefreshing(false);
            }
        });

        return V;
    }

    @Override
    public void onResume()
    {
        refreshListView();
        super.onResume();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        int id = buttonView.getId();

        switch (id)
        {
            case R.id.fgmAla_togBtn_vibrate:
                vibrate = isChecked ? false : true;
                setting.edit().putBoolean("vibrate",vibrate).commit();
                if (vibrate)
                    Toast.makeText(V.getContext(),R.string.fgmAla_vibrateOn,Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(V.getContext(),R.string.fgmAla_vibrateOff,Toast.LENGTH_SHORT).show();
                break;
            case R.id.fgmAla_togBtn_voice:
                voice = isChecked ? false : true;
                setting.edit().putBoolean("voice",voice).commit();
                if (voice)
                    Toast.makeText(V.getContext(),R.string.fgmAla_voiceOn,Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(V.getContext(),R.string.fgmAla_voiceOff,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id)
        {
            case R.id.fgmAla_btn_claen:
                final AlertDialog.Builder cleanDialogBuilder = new AlertDialog.Builder(V.getContext());

                        cleanDialogBuilder.setTitle(R.string.fgmAla_dialogTitle1).setMessage(R.string.fgmAla_dialogMsg1)
                        .setPositiveButton(R.string.determine, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                refreshListView();
                                delAllAlarm();
                                alarmM.cleanTable();
                                setting.edit().putInt("count",0).commit();
                                listView.setVisibility(View.INVISIBLE);
                                Toast.makeText(V.getContext(),R.string.cleanDone,Toast.LENGTH_SHORT).show();
                                cleanDialogBuilder.create().dismiss();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                cleanDialogBuilder.create().dismiss();
                            }
                        }).show();
                break;

            case R.id.fgmAla_btn_refresh:
                refreshListView();
                break;
        }
    }

    private void refreshListView()
    {
        alarmM = new AlarmListSQL(this.getActivity(),DATABASE_NAME,null, DATABASE_VERISON);


        if (alarmM.isTableExist())
        {
            powerCalLists = alarmM.getDataList();

            if (alarmM.getDataCount() == 0)
            {
                alarmM.cleanTable();
                setting.edit().putInt("count",0).commit();
            }

            adapter = new PowerCalListViewAdapter(V.getContext(),powerCalLists);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    final int p = position;
                    /*
                    Log.e("location " + p
                            , powerCalLists.get(position).getTime() + "," + powerCalLists.get(position).getMsg());
                    Log.e("listSize",powerCalLists.size() + "");
                    */
                    final AlertDialog.Builder listVDialogBuilder = new AlertDialog.Builder(V.getContext());
                    listVDialogBuilder.setTitle(R.string.fgmAla_dialogTitle2)
                            .setMessage(getString(R.string.fgmAla_dialogMsg2) + powerCalLists.get(position).getMsg() + getString(R.string.fgmAla_dialogMsg3))
                            .setPositiveButton(R.string.determine, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    delAlarm(powerCalLists.get(p).getCount());
                                    alarmM.delData(powerCalLists.get(p).getTime());
                                    refreshListView();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    listVDialogBuilder.create().dismiss();
                                }
                            }).show();
                }
            });
        }
        alarmM.close();
        listView.setVisibility(View.VISIBLE);
    }

    private void delAlarm(int count)
    {
        AlarmManager alarm = (AlarmManager) V.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(V.getContext(),AlarmReceiver.class);
        intent.addCategory(count + "");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                V.getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarm.cancel(pendingIntent);
    }

    private void delAllAlarm()
    {
        if (alarmM.isTableExist())
        {
            AlarmManager alarm = (AlarmManager) V.getContext().getSystemService(Context.ALARM_SERVICE);

            if (alarmM.getDataCount() != 0)
            {
                for (int a = 0; a < powerCalLists.size(); a++)
                {
                    Intent intent = new Intent(V.getContext(),AlarmReceiver.class);
                    intent.addCategory(powerCalLists.get(a).getCount() + "");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            V.getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarm.cancel(pendingIntent);
                }
            }
        }
    }
}
