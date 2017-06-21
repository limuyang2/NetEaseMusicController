package my.limuyang.neteasemusiccontroller.contract;

import my.limuyang.neteasemusiccontroller.utils.Constants;

/**
 * Created by limuyang on 2017/6/20.
 */

public interface MainActivityContract {
    interface View {
        void showToastMsg(String msg);
    }

    interface Presenter {
        void sendControlInfo(@Constants.ControlName String msg);

        void showToastMsg(String msg);
    }

    interface Model {
        void sendControlInfo(@Constants.ControlName String msg);
    }
}
