package com.ncov.coronatracking.helpers;

import com.ncov.coronatracking.App;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CoronaDownloader {

    private static CoronaDownloader dataHelper;

    private String urlConfirmed =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    private String urlDeath =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Deaths.csv";
    private String urlRecovered =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Recovered.csv";


    private OkHttpClient okHttpClient = new OkHttpClient();

    public static CoronaDownloader get(){
        if(dataHelper==null){
            dataHelper = new CoronaDownloader();
        }
        return dataHelper;
    }

    public void load(DownLoadCallback downLoadCallback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    downLoadFile(urlRecovered,getRecoverFile());
                    downLoadFile(urlConfirmed,getConfirmFile());
                    downLoadFile(urlDeath,getDeathFile());
                    downLoadCallback.onSuccess();
                } catch (IOException e) {
                    e.printStackTrace();
                    downLoadCallback.onFail();
                }

            }
        }.start();
    }

    private void downLoadFile(String url,File file) throws IOException {
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            App.get().sendTracker("Fail Download github csv");
            throw new IOException("Fail download file: "+response);
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(response.body().bytes());
        fileOutputStream.close();
    }

    public File getConfirmFile(){
        return new File(App.get().getFilesDir(),"confirm.csv");
    }
    public File getDeathFile(){
        return new File(App.get().getFilesDir(),"death.csv");
    }
    public File getRecoverFile(){
        return new File(App.get().getFilesDir(),"recover.csv");
    }

    public interface DownLoadCallback{
        void onSuccess();
        void onFail();
    }


}
