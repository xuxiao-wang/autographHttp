package mt.mm.autograph.utils;

/**
 * Created by wxx on 2016/5/17.
 */
public final class HttpLog{

    //测试开关，true为测试模式
    private static boolean debugMode = true;
    private static final String TAG = "AutographHttp";

    public static void setHttpDebugMode(boolean isShow) {
        debugMode = isShow;
    }

    public static void i(Object str) {
        if (debugMode) {
            android.util.Log.i(TAG, String.valueOf(str));
        }
    }

    public static void e(Object str) {
        if (debugMode) {
            android.util.Log.e(TAG, String.valueOf(str));
        }
    }

}
