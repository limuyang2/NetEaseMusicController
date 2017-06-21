package my.limuyang.neteasemusiccontroller.utils;

import android.support.annotation.StringDef;

import java.lang.annotation.Documented;

import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.LAST;
import static my.limuyang.neteasemusiccontroller.utils.Constants.ControlName.PAUSE_PLAY;

/**
 * Created by limuyang on 2017/6/20.
 */

public class Constants {
    @StringDef({PAUSE_PLAY, LAST})
    @Documented
    public @interface ControlName {
        String PAUSE_PLAY = "PAUSE_PLAY";
        String LAST = "LAST";
        String NEXT = "NEXT";

    }
}
