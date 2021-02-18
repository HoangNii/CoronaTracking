package com.ncov.coronatracking.helpers;

import android.content.Context;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private Context context;
    private int fileName;
    private List<String[]> rows = new ArrayList<>();

    public CSVReader(Context context, int fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public List<String[]> readCSV() throws IOException {
        File file;
        InputStream is;
        if(fileName==0){
            file = CoronaDownloader.get().getConfirmFile();
            if(file.exists()){
                is = new FileInputStream(file);
            }else {
                is = context.getAssets().open("cf.csv");
            }
        }else if(fileName==1){
            file = CoronaDownloader.get().getDeathFile();
            if(file.exists()){
                is = new FileInputStream(file);
            }else {
                is = context.getAssets().open("de.csv");
            }
        }else {
            file = CoronaDownloader.get().getRecoverFile();
            if(file.exists()){
                is = new FileInputStream(file);
            }else {
                is = context.getAssets().open("re.csv");
            }
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        String csvSplitBy = ",";
        line = br.readLine();
        while (!TextUtils.isEmpty(line)) {
            String[] row = line.split(csvSplitBy);
            rows.add(row);
            line = br.readLine();
        }
        return rows;
    }
}
