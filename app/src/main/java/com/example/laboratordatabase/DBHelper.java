package com.example.laboratordatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";
    private ArrayList<Tables> tablesList = new ArrayList<>();
    String DBname;
    SQLiteDatabase db;
    public DBHelper(Context context, String dbname) {
        super(context, dbname, null, 1);
        this.DBname = dbname;
    }

    public ArrayList<Tables> getTablesList() {
        return tablesList;
    }

    public void setTablesList(ArrayList<Tables> tablesList) {
        this.tablesList = tablesList;
    }

    public void createT(SQLiteDatabase db, String tableN, String[] FieldsN,
                        String[] FieldsT, int numberOfFields) throws IOException {
        Tables tables = new Tables();
        tables.tableName = tableN;
        String ss = "create table " + tableN + "(";
        for (int i = 0; i < numberOfFields - 1; i++) {
            ss = ss + FieldsN[i] + "  " + FieldsT[i] + ", ";
           // tables.fields.add(FieldsN[i]);
        }
        ss = ss + FieldsN[numberOfFields - 1] + "  " + FieldsT[numberOfFields - 1];
        ss = ss + ");";
        Log.d("Create Table:", tableN + "  :  " + ss);
        db.execSQL(ss);
        Log.d("a fost creat", "tabelul" + tableN + "  ," + ss);
        tablesList.add(new Tables(tables.tableName,tables.fields));
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
