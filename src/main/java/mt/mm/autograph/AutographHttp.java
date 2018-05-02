package mt.mm.autograph;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import mt.mm.autograph.interfaces.FailureCallback;
import mt.mm.autograph.interfaces.ResponseCallback;
import mt.mm.autograph.utils.AutographRequest;
import mt.mm.autograph.utils.HttpLog;
import mt.mm.autograph.utils.Information;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by wang_xu_xiao on 8/28/2017 5:15 PM.
 */

public class AutographHttp {
    private static String autographKey;  // 默认key
    private static OkHttpClient okHttpClient;

    private AutographHttp(){}

    private AutographHttp(final String url, final Map<String,String> map, final RequestMethod method, final ResponseCallback responseCallback, final FailureCallback failureCallback, final String key, final Activity activity,final String http){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AutographRequest request = new AutographRequest(http,url, map,activity);

                switch (method) {
                    case GET:
                        request.get(responseCallback,failureCallback,key);
                        break;
                    case POST:
                        request.post(responseCallback,failureCallback,key);
                        break;
                }
            }
        }).start();


        /**/

    }


    public static void init(Context context,String key,String url){
        init(context,key,url,false);
    }

    public static void init(Context context, String key, String url,boolean debugMode) {

        HttpLog.setHttpDebugMode(debugMode);

        autographKey = key;

        final StringBuilder sb = new StringBuilder();

        sb.append(Information.getApplicationName(context))
                .append("/")
                .append(Information.getVersion(context))
                .append(" (")
                .append(Information.getModel())//手机型号
                .append(";Android ")
                .append(Information.getOSName())//系统版本号
                .append(")");

        AutographRequest.init(url);

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        okhttp3.Request request = chain.request()
                                .newBuilder()
                                .removeHeader("User-Agent")
                                //应用名，应用版本，手机类型，操作系统
                                .addHeader("User-Agent", sb.toString())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .connectTimeout(30 * 1000L, TimeUnit.MILLISECONDS)
                .readTimeout(30 * 1000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            throw new NullPointerException("You might not initialize by calling the 'Autograph.init()' method");
        }
        return okHttpClient;
    }

    public static class Builder {
        private String url;
        private Map<String, String> map = new ConcurrentHashMap<>();
        private RequestMethod method = RequestMethod.GET;
        private ResponseCallback responseCallback;
        private FailureCallback failureCallback;
        private String key = autographKey;
        private Activity activity;
        private String http;

        //请求的url
        public Builder with(String url) {
            if(TextUtils.isEmpty(url)) throw new NullPointerException("url cannot be empty");

            this.url = url;
            return this;
        }


        public Builder changeHttp(String http){
            this.http = http;
            return this;
        }

        public Builder callbackOnUiThread(Activity activity){
            this.activity = activity;
            return this;
        }

        //请求参数
        public Builder addParameter(Map<String, String> map) {
            if (map != null) {
                this.map.putAll(map);
            }
            return this;
        }

        public Builder add(String key,Object value){
            map.put(key,String.valueOf(value));
            return this;
        }

        public Builder requestMethod(RequestMethod method){
            this.method = method;
            return this;
        }

        public Builder get() {
            this.method = RequestMethod.GET;
            return this;
        }

        public Builder post() {
            this.method = RequestMethod.POST;
            return this;
        }

        //回调
        public Builder onResponse(ResponseCallback responseCallback){
            this.responseCallback = responseCallback;
            return this;
        }

        public Builder onFailure(FailureCallback failureCallback){
            this.failureCallback = failureCallback;
            return this;
        }

        //加密key
        public Builder alterKey(String key) {
            this.key = key;
            return this;
        }

        public void builder() {
            if(TextUtils.isEmpty(url)) throw new NullPointerException("you may not have used the 'with(String url)' method");

            new AutographHttp(url,map,method,responseCallback,failureCallback,key,activity,http);
        }

    }

}
