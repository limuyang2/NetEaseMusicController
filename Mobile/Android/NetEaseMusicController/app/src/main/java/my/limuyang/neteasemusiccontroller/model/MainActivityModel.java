package my.limuyang.neteasemusiccontroller.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import my.limuyang.neteasemusiccontroller.SocketClient;
import my.limuyang.neteasemusiccontroller.contract.MainActivityContract;
import my.limuyang.neteasemusiccontroller.utils.Constants;

import static my.limuyang.neteasemusiccontroller.SocketClient.getSocketInputStream;
import static my.limuyang.neteasemusiccontroller.utils.Constants.IpAddress;

/**
 * Created by limuyang on 2017/6/20.
 */

public class MainActivityModel implements MainActivityContract.Model {

    private ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
    private MainActivityContract.Presenter presenter;

    public MainActivityModel(MainActivityContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sendControlInfo(@Constants.ControlName final String msg) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(SocketClient.openSocket()));
                    bw.write(msg);
                    bw.newLine();
                    bw.flush();
                    BufferedReader br = new BufferedReader(new InputStreamReader(getSocketInputStream()));
                    //读取服务器返回的消息数据
//                    System.out.println(">>" + br.readLine());
                } catch (SocketTimeoutException e) {
                    presenter.showToastMsg(IpAddress + "连接超时...");
                } catch (IOException e) {
                    presenter.showToastMsg(IpAddress + "连接错误！");
                    e.printStackTrace();
                }
            }
        };
        singleThreadPool.execute(runnable);
    }
}
