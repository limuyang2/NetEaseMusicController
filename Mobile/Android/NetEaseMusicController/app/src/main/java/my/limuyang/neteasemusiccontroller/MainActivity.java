package my.limuyang.neteasemusiccontroller;

import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Documented;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static my.limuyang.neteasemusiccontroller.MainActivity.ControlName.LAST;
import static my.limuyang.neteasemusiccontroller.MainActivity.ControlName.PAUSE_PLAY;
import static my.limuyang.neteasemusiccontroller.SocketClient.getSocketInputStream;

public class MainActivity extends AppCompatActivity {
    @StringDef({PAUSE_PLAY, LAST})
    @Documented
    @interface ControlName{
        String PAUSE_PLAY="PAUSE_PLAY";
        String LAST="LAST";
        String NEXT="NEXT";
    }

    @BindView(R.id.pause_play)
    Button pausePlay;
    @BindView(R.id.last)
    Button last;

    private ExecutorService singleThreadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        singleThreadPool= Executors.newSingleThreadExecutor();
    }


    @OnClick({R.id.pause_play, R.id.last})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pause_play:
                sendInfo(PAUSE_PLAY);
                break;
            case R.id.last:
                sendInfo(LAST);
                break;
        }
    }

    private void sendInfo(@ControlName final String msg)
    {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(SocketClient.openSocket()));
                    bw.write(msg);
                    bw.newLine();
                    bw.flush();
                    BufferedReader br = new BufferedReader(new InputStreamReader(getSocketInputStream()));
                    //读取服务器返回的消息数据
                    System.out.println(">>"+br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        singleThreadPool.execute(runnable);
    }
}
