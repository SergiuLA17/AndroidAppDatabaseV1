package com.example.laboratordatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileOper {
    String tablesN;
    int numberOfTables;

    public FileOper() {
    }

    public FileOper(String tablesN) {
        this.tablesN = tablesN;
    }

    public FileOper(int n, String tablesN) {
        this.tablesN = tablesN;
        this.numberOfTables = n;
    }

    public String readTable(String tablesN) {
        String aDataRow = "";
        String aBuffer = "";
        final String rez;
        {// read from ListOfTables
            try {
                //File myFile = new File("/storage/extSdCard/"+tablesN+".txt");
                File myFile = new File("/mnt/sdcard/download/" + tablesN + ".txt");
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                //tv.setText(aBuffer);
                myReader.close();
            } catch (Exception e) {
				System.out.println("Error: " + e);
            }

        }
        rez = aBuffer;
        return rez;
    }

    public int proba() {
        int i = 3;
        i = i + 2;
        return i;
    }
}

