package mt.mm.autograph.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mt.mm.autograph.AutographHttp;
import mt.mm.autograph.interfaces.FailureCallback;
import mt.mm.autograph.interfaces.ResponseCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求类
 * <p>
 * Created by wxx on 2016/10/15.
 */
public class AutographRequest {
    private String path;                    //地址
    private Map<String, String> map = null;        //请求参数
    private static String URL = null;       //基地址
    private Activity activity;
    private ArrayList<String> parameters;   //排序参数
    private String http = null;

    public AutographRequest(String http, String path, Map<String, String> map, Activity activity) {

        this.http = http;
        this.map = map;
        this.path = path;
        this.activity = activity;
        /**
         * 将需要排序的参数map集合拼装后添加到list集合
         * */
        parameters = new ArrayList<>();

        synchronized (AutographRequest.class) {
            Set set = map.keySet();

            StringBuffer sb = new StringBuffer();
            for (Iterator ite = set.iterator(); ite.hasNext(); ) {
                String key = (String) ite.next();
                String value = map.get(key);
                sb.delete(0, sb.length());
                sb.append("&").append(key).append("=").append(value);
                parameters.add(sb.toString());
            }

        }
    }


    //初始化基地址
    public static void init(String url) {
        URL = url;
    }

    //返回get请求数据
    public synchronized void get(final ResponseCallback responseCallback, final FailureCallback failureCallback, final String autographKey) {

        //返回排序后的地址
        String getUrl = Url.getUrl(parameters, autographKey);
        StringBuffer sb = new StringBuffer();
        sb.delete(0, sb.length());


        if (!Patterns.WEB_URL.matcher(path).matches()) {

            if (TextUtils.isEmpty(http)) {
                sb.append(URL);
            } else {
                sb.append(http);
            }

        }

        sb.append(path);

        sb.append("?").append(getUrl);

        HttpLog.i("get：" + sb.toString());

        Request.Builder requestBuilder = new Request.Builder().url(sb.toString());
        requestBuilder.get();
        Request request = requestBuilder.build();
        Call mcall = AutographHttp.getOkHttpClient().newCall(request);

        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {

                if (activity == null) {
                    if (failureCallback != null) {
                        failureCallback.onFailure(call, e.toString());
                    }
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (failureCallback != null) {
                                failureCallback.onFailure(call, e.toString());
                            }
                        }
                    });
                }


                HttpLog.e(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String str = response.body().string();


                if (activity == null) {
                    if (responseCallback != null) {
                        responseCallback.onResponse(str);
                    }
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseCallback != null) {
                                responseCallback.onResponse(str);
                            }
                        }
                    });
                }


            }
        });
    }

    //返回post请求数据
    public synchronized void post(final ResponseCallback responseCallback, final FailureCallback failureCallback, String autographKey) {
        //返回排序后的地址
        String[] strs = Url.postUrl(parameters, autographKey);

        String url;


        if (Patterns.WEB_URL.matcher(path).matches()) {
            url = path;
        } else {
            if (TextUtils.isEmpty(http))
                url = URL + path;
            else
                url = http + path;
        }

        FormBody.Builder builder = new FormBody.Builder();

        HttpLog.i("post:" + url);

        builder.add("sign", strs[0]);
        builder.add("signStr", strs[1]);
        builder.add("signTime", strs[2]);

        HttpLog.i("post: sign=" + strs[0]);
        HttpLog.i("post: signStr=" + strs[1]);
        HttpLog.i("post: signTime=" + strs[2]);

        Set set = map.keySet();
        for (Iterator ite = set.iterator(); ite.hasNext(); ) {
            String key = (String) ite.next();
            String value = map.get(key);
            builder.add(key, value);
            HttpLog.i("post:" + key + "=" + value);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = AutographHttp.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (failureCallback != null) {
                    failureCallback.onFailure(call, e.toString());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (responseCallback != null) {
                    responseCallback.onResponse(response.body().string());
                }

            }

        });
    }
}
