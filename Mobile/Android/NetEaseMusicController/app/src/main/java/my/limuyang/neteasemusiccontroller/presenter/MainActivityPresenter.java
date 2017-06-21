package my.limuyang.neteasemusiccontroller.presenter;

import my.limuyang.neteasemusiccontroller.contract.MainActivityContract;
import my.limuyang.neteasemusiccontroller.model.MainActivityModel;
import my.limuyang.neteasemusiccontroller.utils.Constants;

/**
 * Created by limuyang on 2017/6/20.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private MainActivityContract.Model model;
    private MainActivityContract.View view;

    public MainActivityPresenter(MainActivityContract.View view) {
        this.view = view;
        model = new MainActivityModel(this);
    }

    @Override
    public void sendControlInfo(@Constants.ControlName String msg) {
        model.sendControlInfo(msg);
    }

    @Override
    public void showToastMsg(String msg) {
        view.showToastMsg(msg);
    }
}
