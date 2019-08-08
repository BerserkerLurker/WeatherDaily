package com.onadasoft.weatherdaily;

import android.util.Log;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class APIRequests {

    private final OkHttpClient client = new OkHttpClient();
    static final String API_URL = App.getAppResources().getString(R.string.base_url);
    static final String APPID = App.getAppResources().getString(R.string.api_key);

    //public Response runGetReq(String targetURL, String city, String zip, String country,
    //                        String lat, String lon, String cnt, String mode, String units) throws ExecutionException, InterruptedException {
    public Call runGetReq(String targetURL, long id, String city, String zip, String country,
                          String lat, String lon, String cnt, String mode, String units) {


        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL + targetURL).newBuilder();

        urlBuilder.addQueryParameter("appid", APPID);
        urlBuilder.addQueryParameter("units",units);
        urlBuilder.addQueryParameter("mode",mode);


        if (id != 0) {
            urlBuilder.addQueryParameter("id", Long.toString(id));
        }else {

            String query = "";

            if (validStr(city)) {
                if (validStr(country)) {
                    query += city + "," + country;
                    urlBuilder.addQueryParameter("q", query);
                } else {
                    urlBuilder.addQueryParameter("q", city);
                }
            } else if (validStr(zip)) {
                if (validStr(country)) {
                    query += zip + "," + country;
                    urlBuilder.addQueryParameter("zip", query);
                } else {
                    query += zip + ",us";
                    urlBuilder.addQueryParameter("zip", query);
                }
            } else if (validStr(lat) && validStr(lon)) {
                urlBuilder.addQueryParameter("lat", lat);
                urlBuilder.addQueryParameter("lon", lon);
            }

        }
        if(targetURL.equalsIgnoreCase("/forecast") || targetURL.equalsIgnoreCase("/forecast/daily")) {
            if (validStr(cnt)) {
                urlBuilder.addQueryParameter("cnt", cnt);
            }
        }


        String url = urlBuilder.build().toString();

        Log.d("request url", url);


        Request request = new Request.Builder()
                              .url(url)
                              .build();

        // CallbackFuture future = new CallbackFuture();
        return client.newCall(request);
        // client.newCall(request).enqueue(future);
        // Response response = future.get();
        // return response;

//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // TODO -- Handle failure
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(!response.isSuccessful()){
//                    // TODO -- Handle error codes
//                }else {
//                    String responseData = response.body().string();
//
//                    // get the data in the main thread
//                    // JSONObject json = new JSONObject(responseData);
//                    // final String id = json.getString("id");
//
//
//
//
//                }
//
//            }
//        });

    }

    private boolean validStr(String str){
        return str != null && !str.isEmpty();
    }
}
