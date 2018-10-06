package com.moonpa.tospowercal;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.moonpa.mylibrary.PowerCal;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;


public class CalFragment extends Fragment implements View.OnClickListener
{
    final String SETTING = "setting";
    final String DATABASE_NAME = "alarm.db";
    final int DATABASE_VERISON = 5;
    int temp, nowPower, count = 0;
    int maxPower = 100;
    Button btn_powerSet, btn_reset, btn_determine;
    TextView tv_nowPower, tv_maxPower, tv_msg, tv_maxPowerSeekBar,tv_dialog,tv_listVTimeT,tv_listVPowerT;
    ListView listV;
    BubbleSeekBar seekBar, seekBarDialog;
    YoYo.YoYoString effect;
    SharedPreferences setting;
    PowerCal cal = new PowerCal();
    View dialogV,calFgm;
    AlertDialog maxPowersetDialog;
    AlertDialog.Builder maxPowetsetDialogBuilder;
    PowerCalListViewAdapter adapter;
    Context activity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_cal, container, false);
        calFgm = v;
        activity = this.getActivity();
        dialogV = LayoutInflater.from(v.getContext()).inflate(R.layout.fragment_cal_dialog, null);
        setting = this.getActivity().getSharedPreferences(SETTING, Context.MODE_PRIVATE);

        btn_powerSet = (Button) v.findViewById(R.id.fgmCal_btn_powerSet);
        btn_reset = (Button) v.findViewById(R.id.fgmCal_btn_reset);
        btn_determine = (Button) v.findViewById(R.id.fgmCal_btn_determine);
        tv_nowPower = (TextView) v.findViewById(R.id.fgmCal_tv_nowPower);
        tv_maxPower = (TextView) v.findViewById(R.id.fgmCal_tv_maxPower);
        tv_msg = (TextView) v.findViewById(R.id.fgmCal_tv_msg);
        tv_maxPowerSeekBar = (TextView) v.findViewById(R.id.fgmCal_tv_maxPowerSeekBar);
        tv_dialog = (TextView) dialogV.findViewById(R.id.fgmCal_tv_dialogmsg);
        tv_listVTimeT = (TextView) v.findViewById(R.id.fgmCal_tv_listVTimeT);
        tv_listVPowerT = (TextView) v.findViewById(R.id.fgmCal_tv_listVPowerT);
        listV = (ListView) v.findViewById(R.id.fgmCal_listV);
        seekBar = (BubbleSeekBar) v.findViewById(R.id.fgmCal_seekBar);
        seekBarDialog = (BubbleSeekBar) dialogV.findViewById(R.id.fgmCal_seekBar_dialog);

        btn_determine.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_powerSet.setOnClickListener(this);

        seekBarDialog.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener()
        {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser)
            {
                temp = progress;
                tv_dialog.setText(temp + "");
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat)
            {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser)
            {

            }
        });
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener()
        {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser)
            {
                nowPower = progress;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat)
            {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser)
            {

            }
        });

        maxPowetsetDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.fgmCal_dialogSetMaxEnergyTitle)
                .setView(dialogV)
                .setPositiveButton(R.string.determine, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        maxPower = temp;
                        tv_maxPower.setText(maxPower + "");
                        tv_maxPowerSeekBar.setText(maxPower + "");
                        seekBar.getConfigBuilder().max(maxPower).build();
                        setting.edit().putInt("maxPower", maxPower).commit();
                    }
                });
        maxPowersetDialog = maxPowetsetDialogBuilder.create();

        return v;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id)
        {
            case R.id.fgmCal_btn_determine:
                if (cal.getCalTime(maxPower, nowPower) != "wrong" && nowPower != maxPower)
                {
                    String time = cal.getTime();
                    String msg = cal.getMsgTime();
                    final List<PowerCalList> powerCalLists = new ArrayList<PowerCalList>();
                    for(int a = 1; a <= (maxPower - nowPower); a++)
                    {
                        powerCalLists.add(new PowerCalList(cal.getTime(time, a)
                                ,nowPower + a, cal.getMsg(time, a)));
                    }

                    adapter = new PowerCalListViewAdapter(calFgm.getContext(),powerCalLists);
                    listV.setAdapter(adapter);
                    listV.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            final int p = position;
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(calFgm.getContext());
                            dialogBuilder.setTitle(R.string.fgmCal_dialogTitle1).setMessage(getString(R.string.fgmCal_dialogTitle2)
                                    + powerCalLists.get(position).getMsg() + getString(R.string.fgmCal_dialogTitle3))
                                    .setPositiveButton(R.string.determine, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    AlarmListSQL alarmM = new AlarmListSQL(activity,DATABASE_NAME,null, DATABASE_VERISON);
                                    if (alarmM.getCount(powerCalLists.get(p).getTime()) == -1)
                                    {
                                        if (cal.getTime(cal.getTime()).getTimeInMillis() >=
                                                cal.getTime(powerCalLists.get(p).getTime()).getTimeInMillis())
                                        {
                                            tv_msg.setText(R.string.fgmCal_dialogWrong1);
                                            effect = YoYo.with(Techniques.Shake).duration(600).playOn(tv_msg);
                                        }
                                        else
                                        {
                                            count = setting.getInt("count", 0);
                                            alarmM.addData(++count, powerCalLists.get(p).getPower()
                                                    , powerCalLists.get(p).getTime(), powerCalLists.get(p).getMsg());
                                            setting.edit().putInt("count", count).commit();
                                            long alarmTime = cal.getTime(powerCalLists.get(p).getTime()).getTimeInMillis();

                                            Intent intent = new Intent(calFgm.getContext(),AlarmReceiver.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("time", powerCalLists.get(p).getMsg());
                                            bundle.putString("power", powerCalLists.get(p).getPower() + "");
                                            bundle.putInt("count",count);
                                            intent.putExtras(bundle);
                                            intent.addCategory(count + "");

                                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                    calFgm.getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager alarm = (AlarmManager) calFgm.getContext().getSystemService(Context.ALARM_SERVICE);
                                            alarm.set(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent);

                                            tv_msg.setText(R.string.fgmCal_dialogSetDone);
                                            effect = YoYo.with(Techniques.DropOut).duration(1000).playOn(tv_msg);
                                        }
                                    }
                                    else
                                    {
                                        tv_msg.setText(R.string.fgmCal_dialogWrong2);
                                        effect = YoYo.with(Techniques.Shake).duration(600).playOn(tv_msg);
                                    }
                                    alarmM.close();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    });
                    listV.setVisibility(View.VISIBLE);
                    tv_listVTimeT.setVisibility(View.VISIBLE);
                    tv_listVPowerT.setVisibility(View.VISIBLE);
                    tv_msg.setText(R.string.fgmCal_isDone);
                    effect = YoYo.with(Techniques.DropOut).duration(1000).playOn(tv_msg);
                    tv_nowPower.setText(nowPower + "");

                    setting.edit().putInt("maxPower", maxPower)
                            .putInt("nowPower", nowPower)
                            .putString("time", time).commit();
                }
                else
                {
                    tv_msg.setText(R.string.fgmCal_Wrong1);
                    effect = YoYo.with(Techniques.Shake).duration(600).playOn(tv_msg);
                }
                break;
            case R.id.fgmCal_btn_powerSet:
                maxPowersetDialog.show();
                break;
            case R.id.fgmCal_btn_reset:
                setting.edit().remove("maxPower").remove("nowPower")
                        .remove("time").commit();
                nowPower = 0;
                maxPower = 100;
                tv_nowPower.setText(nowPower + "");
                tv_maxPower.setText(maxPower + "");
                tv_maxPowerSeekBar.setText("100");
                listV.setVisibility(View.INVISIBLE);
                tv_listVTimeT.setVisibility(View.INVISIBLE);
                tv_listVPowerT.setVisibility(View.INVISIBLE);
                seekBar.getConfigBuilder().progress(0).max(100).build();
                tv_msg.setText(R.string.cleanDone);
                effect = YoYo.with(Techniques.DropOut).duration(1000).playOn(tv_msg);
                break;
        }
    }

    @Override
    public void onResume()
    {
        refreshCalPower();
        super.onResume();
    }

    private void refreshCalPower()
    {
        if (!setting.contains("maxPower"))
        {
            maxPowersetDialog.show();
        }
        else
        {
            maxPower = setting.getInt("maxPower", 0);
            tv_maxPower.setText(maxPower + "");
            tv_maxPowerSeekBar.setText(maxPower + "");
            seekBar.getConfigBuilder().max(maxPower).build();
        }

        if (setting.contains("time") && setting.contains("nowPower") && setting.contains("maxPower"))
        {
            String time = setting.getString("time", null);
            nowPower = setting.getInt("nowPower", 0);
            maxPower = setting.getInt("maxPower", 0);

            int temp = cal.getReNowPower(time, nowPower, maxPower);
            nowPower = temp;
            tv_nowPower.setText(nowPower + "");
            seekBar.getConfigBuilder().progress(nowPower).build();

            if(temp == maxPower)
                setting.edit().remove("time").commit();
        }
    }
}
