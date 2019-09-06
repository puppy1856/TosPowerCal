package com.moonpa.tospowercal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment
{
    final String SETTING = "setting";
    private String language,enterAppName;
    View V,languageDialogV,openAppDialogV;
    ListView listV_setting;
    SharedPreferences setting;
    AccountListViewAdapter settingAdapter;
    List<AccountSettingList> settingLists;
    RadioGroup languageSettingGroup,openAppGroup;
    AlertDialog languageDialog,openAppDialog;
    AlertDialog.Builder languageDialogBuilder,openAppDialogBuilder;
    Button btn_gitgub;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_account, container, false);
        btn_gitgub = (Button) V.findViewById(R.id.fgmAcc_btn_bitgub);
        languageDialogV = LayoutInflater.from(V.getContext()).inflate(R.layout.fragment_account_lialog_language, null);
        openAppDialogV = LayoutInflater.from(V.getContext()).inflate(R.layout.fragment_account_lialog_openapp, null);
        languageSettingGroup = (RadioGroup) languageDialogV.findViewById(R.id.fgmAcc_language_radioGroup);
        openAppGroup = (RadioGroup) openAppDialogV.findViewById(R.id.fgmAcc_openApp_radioGroup);
        listV_setting = (ListView) V.findViewById(R.id.fgmAcc_listV_setting);

        setting = this.getActivity().getSharedPreferences(SETTING, Context.MODE_PRIVATE);

        languageSettingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.fgmAcc_language_radiobtnAuto:
                        setting.edit().putString("language","auto").commit();
                        break;
                    case R.id.fgmAcc_language_radiobtnEnglish:
                        setting.edit().putString("language","english").commit();
                        break;
                    case R.id.fgmAcc_language_radiobtnTraditional:
                        setting.edit().putString("language","traditionalChinese").commit();
                        break;
                    case R.id.fgmAcc_language_radiobtnSimplified:
                        setting.edit().putString("language","simplifiedChinese").commit();
                        break;
                }
            }
        });
        openAppGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fgmAcc_openAPP_radiobtnDefault:
                        setting.edit().putString("enterAPP","default").commit();
                        break;
                    case R.id.fgmAcc_openAPP_radiobtnApk:
                        setting.edit().putString("enterAPP","Apk").commit();
                        break;
                    case R.id.fgmAcc_openAPP_radiobtnGooglePlay:
                        setting.edit().putString("enterAPP","GooglePlay").commit();
                        break;
                }
            }
        });

        languageDialogBuilder = new AlertDialog.Builder(V.getContext())
                                    .setView(languageDialogV).setPositiveButton(R.string.determine, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(V.getContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(intent);
                    }
                });
        languageDialog = languageDialogBuilder.create();

        openAppDialogBuilder = new AlertDialog.Builder(V.getContext())
                                    .setView(openAppDialogV).setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PackageInfo packageInfo = null;
                        String packageName;
                        enterAppName = setting.getString("enterAPP","default");
                        if ("Apk".equals(enterAppName) || "GooglePlay".equals(enterAppName)) {
                            if ("Apk".equals(enterAppName))
                                packageName = "com.madhead.tos.zh.ex";
                            else
                                packageName = "com.madhead.tos.zh";
                            try {
                                packageInfo = V.getContext().getPackageManager().getPackageInfo(packageName, 0);
                            } catch (PackageManager.NameNotFoundException e) {
                                packageInfo = null;
                                e.printStackTrace();
                            }
                            if (packageInfo != null) {
                                Toast.makeText(V.getContext(), "OK", Toast.LENGTH_SHORT).show();
                                updateSettingList();
                            }
                            else {
                                Toast.makeText(V.getContext(), enterAppName + getString(R.string.openAppSettingWrong), Toast.LENGTH_SHORT).show();
                                setting.edit().putString("enterAPP","default").commit();
                                updateSettingList();
                            }
                        }
                        else {
                            Toast.makeText(V.getContext(), "OK", Toast.LENGTH_SHORT).show();
                            updateSettingList();
                        }
                    }
                });
        openAppDialog = openAppDialogBuilder.create();

        updateSettingList();
        listV_setting.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        languageDialog.show();
                        break;
                    case 1:
                        openAppDialog.show();
                        break;
                }
            }
        });

        btn_gitgub.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri=Uri.parse("https://github.com/puppy1856");
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });

        return V;
    }
    private void updateSettingList(){
        settingLists = new ArrayList<AccountSettingList>();
        language = setting.getString("language","auto");
        enterAppName = setting.getString("enterAPP","default");
        switch (language) {
            case "auto":
                settingLists.add(new AccountSettingList(getString(R.string.fgmAcc_settingLanguageTitle),getString(R.string.languageSetAuto)));
                break;
            case "english":
                settingLists.add(new AccountSettingList(getString(R.string.fgmAcc_settingLanguageTitle),getString(R.string.English)));
                break;
            case "traditionalChinese":
                settingLists.add(new AccountSettingList(getString(R.string.fgmAcc_settingLanguageTitle),getString(R.string.TraditionalChinese)));
                break;
            case "simplifiedChinese":
                settingLists.add(new AccountSettingList(getString(R.string.fgmAcc_settingLanguageTitle),getString(R.string.SimplifiedChinese)));
        }
        switch (enterAppName){
            case "default":
                settingLists.add(new AccountSettingList(getString(R.string.fgmAcc_appNameTitle),getString(R.string.app_name)));
                break;
            case"Apk":
                settingLists.add(new AccountSettingList(getString(R.string.fgmAcc_appNameTitle),getString(R.string.apkName)));
                break;
            case"GooglePlay":
                settingLists.add(new AccountSettingList(getString(R.string.fgmAcc_appNameTitle),getString(R.string.googlePlayName)));
                break;
        }
        settingAdapter = new AccountListViewAdapter(V.getContext(),settingLists);
        listV_setting.setAdapter(settingAdapter);
    }
}
