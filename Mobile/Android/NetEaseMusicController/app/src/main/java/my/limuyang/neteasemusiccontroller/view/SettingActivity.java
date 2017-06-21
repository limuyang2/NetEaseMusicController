package my.limuyang.neteasemusiccontroller.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import my.limuyang.neteasemusiccontroller.R;

/**
 * Created by limuyang on 2017/6/21.
 */

public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getFragmentManager().beginTransaction()
                .replace(R.id.setting_frameLayout, new SettingPreferenceFragment())
                .commit();
    }

}
