package mt.mm.autograph.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by android-王 on 2017/6/12.
 */

public class Information {

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取app名称
     *
     * @return 去包名最后节点
     */
    public static String getApplicationName(Context context) {
        String name = "";
        try {
            String packageName = context.getPackageName();
            String[] split = packageName.split("\\.");
            name = split[split.length - 1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        String model;
        try {
            model = Build.MODEL;
        } catch (Exception e) {
            model = "No_Model";
            e.printStackTrace();
        }

        return model;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getOSName() {
        String release;
        try {
            release = Build.VERSION.RELEASE;
        } catch (Exception e) {
            release = "no_os";
        }
        return release;
    }


    public static char getChar(int position) {
        char[] strings = new char[]{
                '1','2','3', '4', '5', '6','7', '8', '9', '0',
                'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
                'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
                'z', 'x', 'c', 'v', 'b', 'n', 'm'
        };


        if (position < 0) {
            position = 0;
        } else if (position >= strings.length) {
            position = strings.length - 1;
        }

        return strings[position];
    }

}
