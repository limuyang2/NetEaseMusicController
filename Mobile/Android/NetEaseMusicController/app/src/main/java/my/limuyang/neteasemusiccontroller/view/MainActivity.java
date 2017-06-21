package my.limuyang.neteasemusiccontroller.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.limuyang.neteasemusiccontroller.R;
import my.limuyang.neteasemusiccontroller.contract.MainActivityContract;
import my.limuyang.neteasemusiccontroller.presenter.MainActivityPresenter;

import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.LAST;
import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.PAUSE_PLAY;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private MainActivityContract.Presenter presenter;

    @BindView(R.id.pause_play)
    Button pausePlay;
    @BindView(R.id.last)
    Button last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainActivityPresenter(this);
    }


    @OnClick({R.id.pause_play, R.id.last})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pause_play:
                presenter.sendControlInfo(PAUSE_PLAY);
                break;
            case R.id.last:
                presenter.sendControlInfo(LAST);
                break;
        }
    }

    @Override
    public void showToastMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
    }
}
