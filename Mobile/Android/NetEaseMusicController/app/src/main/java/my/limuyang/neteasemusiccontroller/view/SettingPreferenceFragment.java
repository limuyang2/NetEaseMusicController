package my.limuyang.neteasemusiccontroller.view;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

import my.limuyang.neteasemusiccontroller.R;
import my.limuyang.neteasemusiccontroller.utils.Constants;
import my.limuyang.neteasemusiccontroller.utils.SharedPreferencesHelper;

import static my.limuyang.neteasemusiccontroller.utils.Constants.MANUAL_IP;

/**
 * Created by limuyang on 2017/6/21.
 */

public class SettingPreferenceFragment extends PreferenceFragment {
    private SwitchPreference autoSetIp;
    private Preference manualIp;

    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        initViews();
    }

    private void initViews() {
        autoSetIp = (SwitchPreference) findPreference(Constants.AUTO_SET_IP);
        manualIp = findPreference(MANUAL_IP);
        autoSetIp.setOnPreferenceChangeListener(new PreferenceChangeListener());
        manualIp.setOnPreferenceClickListener(new PreferenceClickListener());


        if (autoSetIp.isChecked()) {
            manualIp.setEnabled(false);
        } else {
            manualIp.setEnabled(true);
            manualIp.setSummary(sharedPreferencesHelper.get(MANUAL_IP, "").toString());
        }
    }

    private class PreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case Constants.AUTO_SET_IP:
                    boolean value = (boolean) newValue;
                    if (value) {
                        manualIp.setEnabled(false);
                        manualIp.setSummary(null);
                        sharedPreferencesHelper.remove(MANUAL_IP);
                    } else {
                        manualIp.setEnabled(true);
                        manualIp.setSummary(sharedPreferencesHelper.get(MANUAL_IP, "").toString());
                    }
                    break;

            }
            return true;
        }
    }

    private class PreferenceClickListener implements Preference.OnPreferenceClickListener {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case Constants.MANUAL_IP:
                    //弹出自定义dialog
                    View view = View.inflate(getActivity(), R.layout.dialog_set_ip, null);
                    Button okButton = view.findViewById(R.id.okButton);
                    Button backButton = view.findViewById(R.id.backButton);
                    final MaterialEditText ipEditText = view.findViewById(R.id.ip_editText);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(view);
                    builder.setCancelable(false); //不会点击外面和按返回键消失
                    final Dialog dialog = builder.show();

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            sharedPreferencesHelper.put(MANUAL_IP, ipEditText.getText().toString());
                        }
                    });

                    backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    break;
            }
            return false;
        }
    }
}
