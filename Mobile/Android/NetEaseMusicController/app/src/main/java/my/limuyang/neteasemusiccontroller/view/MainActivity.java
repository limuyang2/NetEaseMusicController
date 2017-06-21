package my.limuyang.neteasemusiccontroller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.limuyang.neteasemusiccontroller.R;
import my.limuyang.neteasemusiccontroller.contract.MainActivityContract;
import my.limuyang.neteasemusiccontroller.presenter.MainActivityPresenter;
import my.limuyang.neteasemusiccontroller.utils.Constants;
import my.limuyang.neteasemusiccontroller.utils.SharedPreferencesHelper;

import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.ADDVOL;
import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.DECVOL;
import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.LAST;
import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.NEXT;
import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.PAUSE_PLAY;
import static my.limuyang.neteasemusiccontroller.utils.Constants.IP_ADDRESS;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    @BindView(R.id.last)
    ImageButton last;
    @BindView(R.id.next)
    ImageButton next;
    @BindView(R.id.pause_play)
    ImageButton pausePlay;
    @BindView(R.id.addvol)
    ImageButton addvol;
    @BindView(R.id.decvol)
    ImageButton decvol;

    private MainActivityContract.Presenter presenter;

    @Override
    protected void onStart() {
        super.onStart();
        //读取配置文件中的IP
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        Constants.IpAddress = sharedPreferencesHelper.get(IP_ADDRESS, "1.1.1.1").toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainActivityPresenter(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent1 = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent1);
                break;
            default:
        }
        return true;
    }

    @OnClick({R.id.pause_play, R.id.last, R.id.next, R.id.addvol, R.id.decvol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pause_play:
                presenter.sendControlInfo(PAUSE_PLAY);
                break;
            case R.id.last:
                presenter.sendControlInfo(LAST);
                break;
            case R.id.next:
                presenter.sendControlInfo(NEXT);
                break;
            case R.id.addvol:
                presenter.sendControlInfo(ADDVOL);
                break;
            case R.id.decvol:
                presenter.sendControlInfo(DECVOL);
                break;
        }
    }

    @Override
    public void showToastMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
