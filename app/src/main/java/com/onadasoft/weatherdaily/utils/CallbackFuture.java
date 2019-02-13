package com.onadasoft.weatherdaily.utils;

import java.io.IOException;
import java9.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * https://stackoverflow.com/questions/50139888/how-to-get-async-call-to-return-response-to-main-thread-using-okhttp
 *
 * CallbackFuture future = new CallbackFuture();
 * client.newCall(request).enqueue(future);
 * Response response = future.get();
 */
public class CallbackFuture extends CompletableFuture<Response> implements Callback {
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        super.complete(response);
    }
    @Override
    public void onFailure(Call call, IOException e) {
        super.completeExceptionally(e);
    }

}
