package com.jainelibrary.retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class ApiLogInterceptor implements okhttp3.Interceptor {
    private static final String LOG_TAG = "REQ_LOG";

    public static String bodyToString(Request request) {
        try {
            if (request.body() != null) {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            }
            return "";
        } catch (final IOException e) {
            return "did not work";
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        //	builder.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36");
        builder.header("Accept", "application/json");
        builder.header("Content-Type", "application/json");

		/*if (!TextUtils.isEmpty(AppPreferences.getAuthToken()))
			builder.header("Authorization", AppPreferences.getAuthToken());*/
        Request newRequest = builder.build();
        Log.e(LOG_TAG, "URL:" + newRequest.url());
        Log.e(LOG_TAG, "REQ:" + bodyToString(newRequest));
        long t1 = System.nanoTime();
        Response response = chain.proceed(newRequest);
//		Utils.log(LOG_TAG, "RESPONSE:" +response.body().string());
        long t2 = System.nanoTime();
//		String responses=response.body().string();
        // if(newRequest.url().toString().contains("comment/"))
        //Utils.log(LOG_TAG, "RES:"+response.body().string());
        //Utils.log(LOG_TAG, "TIM:" + ((t2 - t1) / 1e6d));
//		String  bodyString = new String(response.body().bytes());
//		Utils.writeToFile(bodyString);
        return response;
    }


}
