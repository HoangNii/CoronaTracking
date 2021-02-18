package com.ncov.coronatracking.helpers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ncov.coronatracking.models.ChartData;
import com.ncov.coronatracking.models.CoronaCountry;
import com.ncov.coronatracking.models.Country;
import com.ncov.coronatracking.models.CoronaMaker;
import com.ncov.coronatracking.models.CoronaMain;
import com.ncov.coronatracking.models.Province;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.ncov.coronatracking.utils.Utils.sortByComparator;

public class CoronaDataHelper {

    private static CoronaDataHelper coronaDataHelper;

    private List<String[]> confirms,deaths,recoveries;

    public static CoronaDataHelper get(){
        if(coronaDataHelper==null){
            coronaDataHelper = new CoronaDataHelper();
        }
        return coronaDataHelper;
    }

    public void syn(Context context){
        CSVReader s1 = new CSVReader(context, 0);
        CSVReader s2 = new CSVReader(context, 1);
        CSVReader s3 = new CSVReader(context, 2);
        try {
            confirms = s1.readCSV();
            deaths = s2.readCSV();
            recoveries = s3.readCSV();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getMain(AppCompatActivity activity, Loader loader){
        if(confirms==null){
            syn(activity);
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                ArrayList<Country> countryModels = findCountries();
                CoronaMain model = findCoronaInfoModel();
                activity.runOnUiThread(() -> loader.onSuccess(model,countryModels));
            }
        }.start();
    }
    public void getCountry(AppCompatActivity activity,String country, LoaderCountry loader){
        if(confirms==null){
            syn(activity);
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                ArrayList<Province> provinces = findProvinces(country);
                CoronaCountry coronaCountry = findCoronaCountryModel(country);
                activity.runOnUiThread(() -> loader.onSuccess(coronaCountry,provinces));
            }
        }.start();
    }
    private ArrayList<Country> findCountries(){
        ArrayList<Country> countries = new ArrayList<>();
        Map<String,Integer> confirmMap = new HashMap<>();
        Map<String,Integer> deathMap = new HashMap<>();
        Map<String,Integer> recoverMap = new HashMap<>();

        int size = confirms.get(0).length;

        for (int i = 1; i < confirms.size(); i++){
            String[] strings = confirms.get(i);
            String key;
            if(strings.length!=size){
                key = strings[2];
            }else {
                key = strings[1];
            }
            int sum = Integer.parseInt(confirms.get(i)[confirms.get(i).length-1]);
            if(confirmMap.containsKey(key)&&confirmMap.get(key) != null){
                Integer ctv = confirmMap.get(key);
                if(ctv!=null){
                    int ctVl = ctv+sum;
                    confirmMap.put(key,ctVl);
                }else {
                    confirmMap.put(key,sum);
                }
            }else {
                confirmMap.put(key,sum);
            }
        }
        for (int i = 1; i < deaths.size(); i++){

            String[] strings = deaths.get(i);
            String key;
            if(strings.length!=size){
                key = strings[2];
            }else {
                key = strings[1];
            }

            int sum = Integer.parseInt(deaths.get(i)[deaths.get(i).length-1]);
            if(deathMap.containsKey(key)&&deathMap.get(key) != null){
                Integer ctv = deathMap.get(key);
                if(ctv!=null){
                    int ctVl = ctv+sum;
                    deathMap.put(key,ctVl);
                }else {
                    deathMap.put(key,sum);
                }
            }else {
                deathMap.put(key,sum);
            }
        }

        for (int i = 1; i < recoveries.size(); i++){
            String[] strings = recoveries.get(i);
            String key;
            if(strings.length!=size){
                key = strings[2];
            }else {
                key = strings[1];
            }
            int sum = Integer.parseInt(recoveries.get(i)[recoveries.get(i).length-1]);
            if(recoverMap.containsKey(key)&&recoverMap.get(key) != null){
                Integer ctv = recoverMap.get(key);
                if(ctv!=null){
                    int ctVl = ctv+sum;
                    recoverMap.put(key,ctVl);
                }else {
                    recoverMap.put(key,sum);
                }
            }else {
                recoverMap.put(key,sum);
            }
        }
        Map<String,Integer> newConfirm = sortByComparator(confirmMap,false);
        for (String key:newConfirm.keySet()){
            Integer confirm = newConfirm.get(key);
            Integer death = deathMap.get(key);
            Integer recover = recoverMap.get(key);
            if(confirm==null){ confirm = 0; }
            if(death==null){ death = 0; }
            if(recover==null){ recover = 0; }
            Country countryModel = new Country(key,confirm,death,recover);
            countries.add(countryModel);
        }
        return countries;
    }
    private ArrayList<Province> findProvinces(String country){

        ArrayList<Province> provinces = new ArrayList<>();

        int size = confirms.get(0).length;
        for (int i = 1; i < confirms.size(); i++){
            String[] confirmArray = confirms.get(i);
            String[] deathArray = deaths.get(i);
            String[] recoverArray = recoveries.get(i);
            String countryName;
            String key;
            if(confirmArray.length!=size){
                countryName = confirmArray[2];
                key = confirmArray[0]+","+confirmArray[1];
            }else {
                countryName = confirmArray[1];
                key = confirmArray[0];
            }
            if(countryName.equals(country)){
                float confirm = Float.parseFloat(confirmArray[confirmArray.length-1]);
                float death = Float.parseFloat(deathArray[deathArray.length-1]);
                float recover = Float.parseFloat(recoverArray[recoverArray.length-1]);

                Province countryModel = new Province(key,(int)confirm,(int)death,(int)recover);
                provinces.add(countryModel);
            }

        }

        Collections.sort(provinces, (p1, p2) -> Integer.compare(p2.getConfirm(),p1.getConfirm()));
        return provinces;
    }
    private CoronaMain findCoronaInfoModel(){
        CoronaMain coronaMain = new CoronaMain();
        int sumConfirm = 0;
        int sumDeath = 0;
        int sumRecover = 0;
        List<String> countries = new ArrayList<>();
        int size = confirms.get(0).length;
        for (int i = 1; i < confirms.size(); i++) {
            String[] confirmArray = confirms.get(i);
            String[] deathArray = deaths.get(i);
            String[] recoverArray = recoveries.get(i);

            float co = Float.parseFloat(confirmArray[confirmArray.length-1]);
            float de = Float.parseFloat(deathArray[deathArray.length-1]);
            float re = Float.parseFloat(recoverArray[recoverArray.length-1]);

            sumConfirm = (int) (sumConfirm+co);
            sumDeath = (int) (sumDeath+de);
            sumRecover = (int) (sumRecover+re);

            if(confirmArray.length!=size){
                if(!countries.contains(confirmArray[2])){
                    countries.add(confirmArray[2]);
                }
            }else {
                if(!countries.contains(confirmArray[1])){
                    countries.add(confirmArray[1]);
                }
            }
        }
        coronaMain.setConfirm(sumConfirm);
        coronaMain.setDeath(sumDeath);
        coronaMain.setRecover(sumRecover);
        coronaMain.setCountry(countries.size());

        float def =  100/(float)sumConfirm*(float)sumDeath;
        float ref =  100/(float)sumConfirm*(float)sumRecover;
        coronaMain.setPercentageDeath(def);
        coronaMain.setPercentageRecover(ref);

        return coronaMain;
    }
    private CoronaCountry findCoronaCountryModel(String country){
        CoronaCountry coronaCountry = new CoronaCountry();
        int sumConfirm = 0;
        int sumDeath = 0;
        int sumRecover = 0;
        List<String> provinces = new ArrayList<>();
        int size = confirms.get(0).length;
        for (int i = 1; i < confirms.size(); i++) {
            String[] confirmArray = confirms.get(i);
            String[] deathArray = deaths.get(i);
            String[] recoverArray = recoveries.get(i);

            float co = Float.parseFloat(confirmArray[confirmArray.length-1]);
            float de = Float.parseFloat(deathArray[deathArray.length-1]);
            float re = Float.parseFloat(recoverArray[recoverArray.length-1]);
            if(confirmArray.length!=size){
                if(confirmArray[2].equals(country)){
                    provinces.add(confirmArray[0]+","+confirmArray[1]);
                    sumConfirm = (int) (sumConfirm+co);
                    sumDeath = (int) (sumDeath+de);
                    sumRecover = (int) (sumRecover+re);
                }
            }else {
                if(confirmArray[1].equals(country)){
                    provinces.add(confirmArray[0]);
                    sumConfirm = (int) (sumConfirm+co);
                    sumDeath = (int) (sumDeath+de);
                    sumRecover = (int) (sumRecover+re);
                }
            }
        }
        coronaCountry.setConfirm(sumConfirm);
        coronaCountry.setDeath(sumDeath);
        coronaCountry.setRecover(sumRecover);
        coronaCountry.setProvince(provinces.size());

        float def =  100/(float)sumConfirm*(float)sumDeath;
        float ref =  100/(float)sumConfirm*(float)sumRecover;
        coronaCountry.setPercentageDeath(def);
        coronaCountry.setPercentageRecover(ref);

        return coronaCountry;
    }
    public ArrayList<CoronaMaker> findCoronaMakers(Context context){
        if(confirms==null){
            syn(context);
        }
        ArrayList<CoronaMaker> coronaMakers = new ArrayList<>();
        int size = confirms.get(0).length;
        for (int i = 1; i < confirms.size(); i++) {
            String[] confirmArray = confirms.get(i);
            String[] deathArray = deaths.get(i);
            String[] recoverArray = recoveries.get(i);
            int confirm = Integer.parseInt(confirmArray[confirmArray.length-1]);
            int death = Integer.parseInt(deathArray[deathArray.length-1]);
            int recover = Integer.parseInt(recoverArray[recoverArray.length-1]);
            if(confirmArray.length!=size){
                String province = confirmArray[0]+","+confirmArray[1];
                String country = confirmArray[2];
                double lat = Double.parseDouble(confirmArray[3]);
                double lon = Double.parseDouble(confirmArray[4]);
                CoronaMaker coronaMaker = new CoronaMaker(province,country,lat,lon,confirm,death,recover);
                coronaMakers.add(coronaMaker);
            }else {
                String province = confirmArray[0];
                String country = confirmArray[1];
                double lat = Double.parseDouble(confirmArray[2]);
                double lon = Double.parseDouble(confirmArray[3]);
                CoronaMaker coronaMaker = new CoronaMaker(province,country,lat,lon,confirm,death,recover);
                coronaMakers.add(coronaMaker);
            }

        }
        return coronaMakers;
    }
    public ArrayList<CoronaMaker> findCoronaMakers(Context context,String country){
        if(confirms==null){
            syn(context);
        }
        ArrayList<CoronaMaker> coronaMakers = new ArrayList<>();
        int size = confirms.get(0).length;
        for (int i = 1; i < confirms.size(); i++) {
            String[] confirmArray = confirms.get(i);
            String[] deathArray = deaths.get(i);
            String[] recoverArray = recoveries.get(i);
            int confirm = Integer.parseInt(confirmArray[confirmArray.length-1]);
            int death = Integer.parseInt(deathArray[deathArray.length-1]);
            int recover = Integer.parseInt(recoverArray[recoverArray.length-1]);
            if(confirmArray.length!=size){
                String province = confirmArray[0]+","+confirmArray[1];
                String countryName = confirmArray[2];
                if(country.equals(countryName)){
                    double lat = Double.parseDouble(confirmArray[3]);
                    double lon = Double.parseDouble(confirmArray[4]);
                    CoronaMaker coronaMaker = new CoronaMaker(province,country,lat,lon,confirm,death,recover);
                    coronaMakers.add(coronaMaker);
                }
            }else {
                String province = confirmArray[0];
                String countryName = confirmArray[1];
                if(country.equals(countryName)){
                    double lat = Double.parseDouble(confirmArray[2]);
                    double lon = Double.parseDouble(confirmArray[3]);
                    CoronaMaker coronaMaker = new CoronaMaker(province,country,lat,lon,confirm,death,recover);
                    coronaMakers.add(coronaMaker);
                }
            }

        }
        return coronaMakers;
    }


    public void findChartData(LoaderChartData chartData,String country){
        new Thread(){
            @Override
            public void run() {
                super.run();
                ArrayList<ChartData> charts = new ArrayList<>();
                int sizeCheck = confirms.get(0).length-4;
                int size = confirms.get(0).length;
                for (int d=1;d<=sizeCheck;d++){
                    String date = "";
                    int sumConfirm = 0;
                    int sumDeath = 0;
                    int sumRecover = 0;
                    for (int i = 0; i < confirms.size(); i++) {
                        String[] confirmArray = confirms.get(i);
                        String[] deathArray = deaths.get(i);
                        String[] recoverArray = recoveries.get(i);
                        if(i==0){
                            date = confirmArray[confirmArray.length-d];
                        }else {

                            if(confirmArray.length!=size){
                                if(confirmArray[2].equals(country)){
                                    float co = Float.parseFloat(confirmArray[confirmArray.length-d]);
                                    float de = Float.parseFloat(deathArray[deathArray.length-d]);
                                    float re = Float.parseFloat(recoverArray[recoverArray.length-d]);
                                    sumConfirm = sumConfirm+ (int) co;
                                    sumDeath = sumDeath+ (int) de;
                                    sumRecover = sumRecover+ (int) re;
                                }
                            }else {
                                if(confirmArray[1].equals(country)){
                                    float co = Float.parseFloat(confirmArray[confirmArray.length-d]);
                                    float de = Float.parseFloat(deathArray[deathArray.length-d]);
                                    float re = Float.parseFloat(recoverArray[recoverArray.length-d]);
                                    sumConfirm = sumConfirm+ (int) co;
                                    sumDeath = sumDeath+ (int) de;
                                    sumRecover = sumRecover+ (int) re;
                                }
                            }

                        }
                    }
                    charts.add(new ChartData(date,sumConfirm,sumDeath,sumRecover));
                }
                Collections.reverse(charts);
                chartData.onSuccess(charts);
            }
        }.start();

    }
    public void findChartData(LoaderChartData chartData){
        new Thread(){
            @Override
            public void run() {
                super.run();
                ArrayList<ChartData> charts = new ArrayList<>();
                int size = confirms.get(0).length-4;
                for (int d=1;d<=size;d++){
                    String date = "";
                    int sumConfirm = 0;
                    int sumDeath = 0;
                    int sumRecover = 0;
                    for (int i = 0; i < confirms.size(); i++) {
                        String[] confirmArray = confirms.get(i);
                        String[] deathArray = deaths.get(i);
                        String[] recoverArray = recoveries.get(i);
                        if(i==0){
                            date = confirmArray[confirmArray.length-d];
                        }else {
                            float co = Float.parseFloat(confirmArray[confirmArray.length-d]);
                            float de = Float.parseFloat(deathArray[deathArray.length-d]);
                            float re = Float.parseFloat(recoverArray[recoverArray.length-d]);
                            sumConfirm = sumConfirm+ (int) co;
                            sumDeath = sumDeath+ (int) de;
                            sumRecover = sumRecover+ (int) re;
                        }
                    }
                    charts.add(new ChartData(date,sumConfirm,sumDeath,sumRecover));
                }
                Collections.reverse(charts);
                chartData.onSuccess(charts);
            }
        }.start();

    }

    public interface Loader{
        void onSuccess(CoronaMain model, ArrayList<Country> countries);
    }
    public interface LoaderChartData{
        void onSuccess(ArrayList<ChartData> charts);
    }
    public interface LoaderCountry{
        void onSuccess(CoronaCountry model, ArrayList<Province> provinces);
    }
}
