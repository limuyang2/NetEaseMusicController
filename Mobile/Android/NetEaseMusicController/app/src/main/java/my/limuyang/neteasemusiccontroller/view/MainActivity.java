package my.limuyang.neteasemusiccontroller.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.limuyang.neteasemusiccontroller.R;
import my.limuyang.neteasemusiccontroller.contract.MainActivityContract;
import my.limuyang.neteasemusiccontroller.presenter.MainActivityPresenter;
import my.limuyang.neteasemusiccontroller.utils.Constants;

import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.LAST;
import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.NEXT;
import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.PAUSE_PLAY;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    @BindView(R.id.last)
    ImageView last;
    @BindView(R.id.next)
    ImageView next;
    @BindView(R.id.pause_play)
    ImageView pausePlay;
    @BindView(R.id.addvol)
    ImageView addvol;
    @BindView(R.id.decvol)
    ImageView decvol;
    @BindView(R.id.ipaddressr)
    EditText ipaddressr;
    @BindView(R.id.getipbutton)
    Button getipbutton;
    private MainActivityContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainActivityPresenter(this);
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
                presenter.sendControlInfo(Constants.ControlName.ADDVOL);
                break;
            case R.id.decvol:
                presenter.sendControlInfo(Constants.ControlName.DECVOL);
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

    @OnClick(R.id.getipbutton)
    public void onClick() {
        String Ipaddress = ipaddressr.getText().toString();
    }
}
