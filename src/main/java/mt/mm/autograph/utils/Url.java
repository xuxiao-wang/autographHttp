package mt.mm.autograph.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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

    /**
     * @param arrayList 需要排序的字符串
     *                  <p>
     *                  get请求        字符串排序,返回排序后的Url
     */
    protected static String getUrl(ArrayList<String> arrayList, String key) {

        String time = time();
        String random = str();

        String md5 = sort(arrayList, key, time, random);

        arrayList.add("&sign=" + md5);
        arrayList.add("&signTime=" + time);
        arrayList.add("&signStr=" + random);
        Collections.sort(arrayList);

        StringBuffer buffer = new StringBuffer();
        for (String str : arrayList) {
            buffer.append(str);
        }
        String string = buffer.toString();
        string = string.substring(1);

        return string;
    }

    /**
     * @param arrayList 需要排序的字符串数组
     *                  <p>
     *                  post请求        字符串排序,返回加密字符串，随机数，时间戳
     */
    protected static String[] postUrl(ArrayList<String> arrayList, String key) {

        String time = time();
        String random = str();

        String md5 = sort(arrayList, key, time, random);

        String[] strings = {md5, random, time};
        return strings;
    }

    private static String sort(ArrayList<String> arrayList, String key, String time, String random) {

        //初始参数集合
        ArrayList<String> list = new ArrayList<>();

        list.add("&signTime=" + time);
        list.add("&signStr=" + random);

        for (int i = 0; i < arrayList.size(); i++) {

            String[] split = arrayList.get(i).split("=");
            try {
                if (TextUtils.isEmpty(split[1])) {
                    continue;
                }
            } catch (Exception e) {
                continue;
            }

            list.add(arrayList.get(i));

        }

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
