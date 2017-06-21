package my.limuyang.neteasemusiccontroller.view;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import my.limuyang.neteasemusiccontroller.R;
import my.limuyang.neteasemusiccontroller.utils.Constants;
import my.limuyang.neteasemusiccontroller.utils.SharedPreferencesHelper;

import static my.limuyang.neteasemusiccontroller.utils.Constants.IP_ADDRESS;
import static my.limuyang.neteasemusiccontroller.utils.Constants.MANUAL_IP;
import static my.limuyang.neteasemusiccontroller.utils.Tools.getServerIP;
import static my.limuyang.neteasemusiccontroller.utils.Tools.matcherIP;

/**
 * 设置项
 * Created by limuyang on 2017/6/21.
 */

public class SettingPreferenceFragment extends PreferenceFragment {
    private AlertDialog progressDialog;
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
        initAutoSetIp();
    }

    private void initAutoSetIp() {
        if (autoSetIp.isChecked()) {
            manualIp.setEnabled(false);
            autoSetIp.setSummary(sharedPreferencesHelper.get(IP_ADDRESS, "").toString());
        } else {
            manualIp.setEnabled(true);
            manualIp.setSummary(sharedPreferencesHelper.get(IP_ADDRESS, "").toString());
        }
    }

    private class PreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case Constants.AUTO_SET_IP:
                    boolean value = (boolean) newValue;
                    if (value) {
                        showProgressDialog(null);
                        manualIp.setEnabled(false);
                        manualIp.setSummary(null);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final String a = getServerIP();
                                //在UI线程中执行以下代码
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (a != null && !a.equals("")) {
                                            sharedPreferencesHelper.put(IP_ADDRESS, a);
                                            Toast.makeText(getActivity(), getString(R.string.scan_ok), Toast.LENGTH_LONG).show();
                                        } else {
                                            autoSetIp.setChecked(false);
                                            initAutoSetIp();
                                            Toast.makeText(getActivity(), getString(R.string.scan_erro), Toast.LENGTH_LONG).show();
                                        }
                                        autoSetIp.setSummary(a);
                                        closeProgressDialog();
                                    }
                                });
                            }
                        }).start();
                    } else {
                        autoSetIp.setSummary(null);
                        manualIp.setEnabled(true);
                        manualIp.setSummary(sharedPreferencesHelper.get(IP_ADDRESS, "").toString());
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
                    Object oj = sharedPreferencesHelper.get(IP_ADDRESS, null);
                    ipEditText.setText(oj == null ? null : oj.toString());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(view);
                    builder.setCancelable(false); //不会点击外面和按返回键消失
                    final Dialog dialog = builder.show();

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String getText = ipEditText.getText().toString();

                            if (matcherIP(getText)) {
                                dialog.dismiss();
                                manualIp.setSummary(getText);
                                sharedPreferencesHelper.put(IP_ADDRESS, ipEditText.getText().toString());
                                Toast.makeText(getActivity(), getString(R.string.setIp_success), Toast.LENGTH_LONG).show();
                            } else
                                ipEditText.setError(getString(R.string.IP_format_incorrect));
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


    public void showProgressDialog(String msg) {
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            return;
        }
        if (msg == null) {
            msg = getString(R.string.scanning);
        }

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.progress_dialog, new RelativeLayout(getActivity()), false);
        TextView msgTextView = view.findViewById(R.id.dialog_msg_text);
        msgTextView.setText(msg);
        progressDialog = new AlertDialog.Builder(getActivity()).create();
        progressDialog.setView(view);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();
        lp.width = this.getResources().getDimensionPixelSize(R.dimen.dialog_width);
        lp.height = this.getResources().getDimensionPixelSize(R.dimen.dialog_height);
        progressDialog.getWindow().setAttributes(lp);

    }


    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }
}
