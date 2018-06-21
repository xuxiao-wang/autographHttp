package mt.mm.autograph.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by wxx on 2016/9/29.
 */
public class Url {

    /**
     * @ 获取当前时间,单位为秒
     */
    protected static String time() {
        long l = System.currentTimeMillis();
        int s = (int) (l / 1000);
        return String.valueOf(s);
    }

    /**
     * @ 随机16位字符串
     */
    protected static String str() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(Information.getChar(random.nextInt(36)));
        }
        return sb.toString();
    }

    protected static String getUrl(Map<String, String> map, String key) {
        String time = time();
        String random = str();

        String md5 = getMd5(map, key, time, random);

        //初始参数集合
        ArrayList<String> list = new ArrayList<>();

        Set set = map.keySet();

        StringBuffer sb = new StringBuffer();
        for (Iterator ite = set.iterator(); ite.hasNext(); ) {
            String mKey = (String) ite.next();
            String mValue = map.get(mKey);

            sb.delete(0, sb.length());

            if (!TextUtils.isEmpty(mKey)) {
                sb.append("&").append(mKey).append("=");
                try {
                    sb.append(URLEncoder.encode(mValue, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    sb.append(mValue);
                }
            }
            list.add(sb.toString());
        }

        list.add("&sign=" + md5);
        list.add("&signTime=" + time);
        list.add("&signStr=" + random);

        StringBuffer buffer = new StringBuffer();
        for (String str : list) {
            buffer.append(str);
        }
        String string = buffer.toString();
        string = string.substring(1);

        return string;
    }


    protected static String[] postUrl(Map<String, String> map, String key) {

        String time = time();
        String random = str();

        String md5 = getMd5(map, key, time, random);

        String[] strings = {md5, random, time};
        return strings;
    }

    private static String getMd5(Map<String, String> map, String key, String time, String random) {

        //初始参数集合
        ArrayList<String> list = new ArrayList<>();

        Set set = map.keySet();

        StringBuffer sb = new StringBuffer();
        for (Iterator ite = set.iterator(); ite.hasNext(); ) {
            String mKey = (String) ite.next();
            String mValue = map.get(mKey);
            if (!(TextUtils.isEmpty(mValue) || TextUtils.isEmpty(mKey))) { //过滤当键值为空的情况
                sb.delete(0, sb.length());
                sb.append("&").append(mKey).append("=").append(mValue);
                list.add(sb.toString());
            }
        }

        list.add("&signTime=" + time);
        list.add("&signStr=" + random);

        Collections.sort(list);

        StringBuffer buffer = new StringBuffer();
        for (String str : list) {
            if (TextUtils.isEmpty(str)) {
                continue;
            }
            buffer.append(str);
        }
        buffer.append("&key=").append(key);

        return MD5.getMD5(buffer.toString().substring(1));
    }
}
