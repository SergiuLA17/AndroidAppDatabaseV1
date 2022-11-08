package com.example.laboratordatabase;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;


@SuppressLint("SdCardPath")
public class MainActivity extends Activity {
	public DecimalFormat df = new DecimalFormat("#.##");
	String DBName = "V43";
	EditText et1, et2, et3;
	EditText txtData;
	FileOper tabel;
	SQLiteDatabase db;
	TextView tv;
	DBHelper dbHelper;
	String[][] cc = new String[20][20];
	String[][] cc1 = new String[20][20];
	String[] tabNames = new String[20];
	String tabStruct, tabContent;
	int numberOfTables;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtData = findViewById(R.id.txtData);
		txtData.setHint("Enter some lines of data here...");
		tv = findViewById(R.id.textView1);
		et1 = findViewById(R.id.editText1);
		et2 = findViewById(R.id.editText2);
		et3 = findViewById(R.id.editText3);
		tv.setMovementMethod(new ScrollingMovementMethod());
		String whereC = "  NF > ?  OR PF=? ";
		whereC = "  Nota > ?";
		String whereV = " 6;";
		et1.setText(whereC);
		et2.setText("Editable Values for Restriction(s):  ");
		et3.setText(whereV);
		cc[1][1] = "11";
		cc[1][2] = "12";
		cc1 = cc;
		Log.d(" cc=cc1", "cc[1][1]=  " + cc1[1][1] + "cc[1][2]=  " + cc1[1][2]);
	}


	public void createDB(View v) {
		dbHelper = new DBHelper(this, DBName);
		db = dbHelper.getWritableDatabase();
		Log.d("Create DB=", "The DB " + DBName + "  was created OR Opened the exiting one!");
	}

	public void createTAB(View v) {

		String aBuffer = "";
		String tt;
		int nf;
		String[] fieldsN = new String[10];
		String[] fieldsT = new String[10];
		tabel = new FileOper();
		String tabN = "tablesM";
		Log.d("Read tabN=", tabN);
		aBuffer = tabel.readTable(tabN);
		tt = aBuffer;
		String[] tn = tt.split("\n");
		int nt = tn.length;
		numberOfTables = nt;
		Log.d("", "N=" + nt + "  Nume tabel=" + tn[0] + ",  Nume tabel2=" + tn[1]);
		System.arraycopy(tn, 0, tabNames, 0, nt);
		for (String s : tn) {
			boolean te = exists(s);
			if (!te) {
				tabStruct = tabel.readTable(s + "s");
				tabContent = tabel.readTable(s);
				String[] tfS = tabStruct.split("\n");// structura
				String[] tfC = tabContent.split("\n");//  Content
				nf = tfS.length;
				for (int j = 0; j < nf; j++) {
					String[] fields = tfS[j].split("\t");
					fieldsN[j] = fields[0];
					fieldsT[j] = fields[1];
				}
				Log.d("Tabelul : ", s);
				Log.d("Fields", fieldsN[0] + " , " + fieldsT[0] + " , " + fieldsN[1] + " , " + fieldsT[1]);

				// create table i

				Log.d("before try Table:", s + "   to create???   ");
				try {
					dbHelper.createT(db, s, fieldsN, fieldsT, nf);

					Log.d("Table:", "The  " + s + "   was created   ");
				} catch (Exception e) {
					Log.d("Table:", "The table " + s + "  was existing, and was not created again." + e);
				}
			}
		}


	}
    public boolean exists(String table) {
    	Cursor c = null;
    	boolean tableExists = false;
    	/* get cursor on it */
    	try
    	{
    	    c = db.query(table, null,
    	        null, null, null, null, null);
    	        tableExists = true;
    	        Log.d("About existing ", "The table "+table+"  exists! :))))");
    	}
    	catch (Exception e) {
    	    /* fail */
    	    Log.d("The table is missing", table+" doesn't exist :(((");
    	
    	}
    	return tableExists;
	}
    
    public void fillTAB(View v)
    {
    	String tabN;
    	for(int i=0;i<numberOfTables;i++) {
			tabN = tabNames[i];
			boolean te = exists(tabN);

			Log.d("Before if", tabN);
			if (te) {  //if the tb exists then fill it
				Log.d("Inside  if", "The table:  " + tabN + "   Exists");
				//the table exests:  	//clear the table
				db.delete(tabN, null, null);
				Log.d("After delete", tabN);
				//fill the table
				String tt, tabContent;
				int nf;
				tabel = new FileOper();
				tabContent = tabel.readTable(tabN);
				Log.d("After table content", tabN);
				String[] tfC = tabContent.split("\n");//  Content
				int nr = tfC.length;
				String[] fieldsN = tfC[0].split("\t");
				String[] fieldsT = tfC[1].split("\t");
				nf = fieldsN.length;
				// insert rows
				ContentValues cv = new ContentValues();
				int sw;
				double nnf;
				for (int j = 2; j < nr; j++) //on rows nr=tfC.length;
				{
					cv.clear();
					if (tabN.equals("detbord")) {
						String[] rcd = tfC[j].toString().split("\t");
						nnf = Float.parseFloat(rcd[2]) * 0.15 + Float.parseFloat(rcd[3]) * 0.15 + Float.parseFloat(rcd[4]) * 0.1 + Float.parseFloat(rcd[5]) * 0.2 + Float.parseFloat(rcd[6]) * 0.4;
						tfC[j] = tfC[j] + "\t" + df.format(nnf);
					}
					String[] rcd = tfC[j].split("\t");
					for (int k = 0; k < nf; k++)// on fields nf=fieldsN.length;
					{
						sw = Integer.parseInt(fieldsT[k]);
						switch (sw) {
							case 1:
								cv.put(fieldsN[k], Integer.valueOf(rcd[k]));
								break;
							case 2:
								cv.put(fieldsN[k], rcd[k]);
								break;
							default:
								break;
						}
					}// end of fields
					db.insert(tabN, null, cv);


				}
				Log.d("Datele in tabel", "------" + tabN);
				Cursor cc = null;
				cc = db.query(tabN, null, null, null, null, null, null);
				logCursor(cc);
				cc.close();
				Log.d("Datele in tabel", "--- ---");
			}
		}

	}

	public void studL(View v) {

		dbHelper = new DBHelper(this, DBName);
		db = dbHelper.getWritableDatabase();
		Log.d("Create DB=", "The DB " + DBName + "  was created OR Opened the exiting one!");
		Log.d("Group By", "--- INNER JOIN with rawQuery---");


		String whereC = et1.getText().toString().trim();
		String whereV = et3.getText().toString().trim();

		whereV = whereV.replace(" ", "");
		String[] s2 = whereV.split(";");

		int np = s2.length;
		Log.d("Numar de parametri", "parametri= " + np);
		Log.d("WhereC=", "whereC= " + whereC);
		Log.d("WhereV=", "whereV= " + whereV);

		for (int j = 0; j < np; j++)
			s2[j] = s2[j].trim();

		String studLista1 = "SELECT NumeS, StudId, COUNT(*) as count " + "FROM stud  " + " GROUP BY NumeS, StudId " + " HAVING count> ?";


		studLista1 = "select br.Nota as nota, st.StudN as student, gr.NumGrup as grupa " + "from BorderouM as br " + "inner join StudentsM as st " + "on br.IdStudent=st.StudId " + "inner join GroupsM as gr " + "on st.GroupID=gr.GrID " + "where " + whereC;


		Cursor c;
		c = null;
		// c = db.rawQuery(studLista1, new String[] {"600"});
		c = db.rawQuery(studLista1, s2);
		// c = db.rawQuery(studLista1, null);
		logCursor1(c);
		c.close();
		Log.d("End Lista", "End Lista");
		txtData.setText("Example of Conditions: " + whereC);
		// txtData.append(whereV);

	}
    

    // afisare in LOG din Cursor
	@SuppressLint({"Range", "SetTextI18n"})
	void logCursor(Cursor c) {
		tv.setText("");
		String str2 = "Results For the Fields : ";
		String cnn = "";
		if (c != null) {
			if (c.moveToFirst()) {
				String str, str1;
				int klu = 0, rr = 0;
				do {
					rr = 0;
					str = "";
					str1 = "";
					if (klu == 0) //  the first record
					{
						for (String cn : c.getColumnNames()) { //if(rr<6)
							{//str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
								str = str.concat(c.getString(c.getColumnIndex(cn)) + "; ");
								cnn = cnn.concat(cn + " ; ");
								if (rr == 2) cnn = cnn + "\n";
							}

							rr++;
						}

					}
					// for the next records
            rr=0;  str = "";str1="";
            if (klu>0) //  the next records
            {  
            	for (String cn : c.getColumnNames()) {
					if (rr > 2) {
						str = str.concat(c.getString(c.getColumnIndex(cn)) + "; ");
						cnn = cnn.concat(cn + " ; ");
					}
					rr++;
				}
            
            }
              if (klu==0) {str2=str2 + cnn;tv.setText(cnn+"\n");}
            klu++;
            rr++;
            str1=str+"\n";
            tv.setText(tv.getText()+str1);
          
          } while (c.moveToNext());
        
        }
      } else
         Log.d("Rindul", "Cursor is null");
	}

	// afisare in LOG din Cursor
	@SuppressLint("Range")
	void logCursor1(Cursor c) {
		Log.d("COLUMNS NR=", "nc=" + c.getColumnCount());

		tv.setText("");
		String str2 = "Results For the Fields : ";
		String cnn = "", coln = "";
		if (c != null) {
			if (c.moveToFirst()) {
				String str, str1;

				int klu = 0, rr = 0;
				do {
					rr = 0;
					str = "";
					str1 = "";

					// for the next records
					for (String cn : c.getColumnNames()) {
						if (rr > -1) {//str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
							str = str.concat(c.getString(c.getColumnIndex(cn)) + "; ");
							cnn = cnn.concat(cn + " ; ");

						}

						rr++;
						Log.d("COLUMNS NR=", "nc=" + c.getColumnCount() + ", rr=" + rr);
					}

					if (klu == 0) {
						str2 = str2 + cnn;
						tv.setText(cnn + "\n");
					}
					klu++;
					rr++;
					str1 = str + "\n";
					Log.d(" Rindul=", str);
					tv.setText(tv.getText() + str1);

				} while (c.moveToNext());

			}
			// txtData.setText(str2 + " with WHERE Conditions : " + "\n");
		} else Log.d("Rindul", "Cursor is null");
	}

	public void save(View v) {
		try {
			FileOutputStream fileOut = new FileOutputStream("/mnt/sdcard/stoc/stock.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dbHelper.getTablesList());
			out.close();
			fileOut.close();
			System.out.println("Serializable" + dbHelper.getTablesList());
			System.out.print("Serialized data is saved in /tmp/stok.ser");

		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void read(View v) {
		ArrayList<Tables> tablesArrayList = null;
		try {
			FileInputStream fis = new FileInputStream("/mnt/sdcard/stoc/stock.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);


			tablesArrayList = (ArrayList<Tables>) ois.readObject();


			ois.close();
			fis.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
		}

		dbHelper.setTablesList(tablesArrayList);
		//Verify list data
		System.out.println("Deserializable" + dbHelper.getTablesList());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		menu.add("UpDate");
		menu.add("IneqGraphSol");
		menu.add("MovRotate");
		menu.add("Grid");
		menu.add("GraphFun");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
              if(item.getTitle()=="Grid")
		{
            	
		}
        return super.onOptionsItemSelected(item);
    }
}





