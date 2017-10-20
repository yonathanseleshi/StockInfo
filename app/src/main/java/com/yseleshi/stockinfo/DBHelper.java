package com.yseleshi.stockinfo;

/**
 * Created by llasslo on 10/28/14.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public final class DBHelper
{
    private static final String LOGTAG = "DBHelper";

    private static final String DATABASE_NAME = "stock.db";
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "stocks";

    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;

    // Column Names
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SYMBOL = "symbol";
    public static final String KEY_EXCHANGE = "exchange";
    public static final String KEY_LASTTRADEPRICE = "last";
    public static final String KEY_CHANGE = "change";
    public static final String KEY_CHANGEPERCENT = "changepercent";
    public static final String KEY_OPEN = "open";
    public static final String KEY_TEXTCOLOR = "textcolor";
    public static final String KEY_DAYSHIGH = "high";
    public static final String KEY_DAYSLOW = "low";



    private static final String INSERT =
            "INSERT INTO " + TABLE_NAME + " (" +
                    KEY_NAME + ", " +
                    KEY_SYMBOL + ", " +
                    KEY_EXCHANGE + ", " +
                    KEY_OPEN + ", " +
                    KEY_LASTTRADEPRICE + ", " +
                    KEY_CHANGE + ", " +
                    KEY_CHANGEPERCENT + ", " +
                    KEY_DAYSHIGH + ", " +
                    KEY_DAYSLOW + ", " +
                    KEY_TEXTCOLOR + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public DBHelper(Context context) throws Exception
    {
        this.context = context;
        try {
            OpenHelper openHelper = new OpenHelper(this.context);
           // Open a database for reading and writing
            db = openHelper.getWritableDatabase();
           //  compile a sqlite insert statement into re-usable statement object.
            insertStmt = db.compileStatement(INSERT);

        } catch (Exception e) {
            Log.e(LOGTAG, " DBHelper constructor: could not get database " + e);
            throw (e);
        }
    }

    // Column indexes
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_SYMBOL = 2;
    public static final int COLUMN_EXCHANGE  = 3;
    public static final int COLUMN_OPEN = 4;
    public static final int COLUMN_LASTTRADEPRICE  = 5;
    public static final int COLUMN_CHANGE = 6;
    public static final int COLUMN_CHANGEPERCENT = 7;
    public static final int COLUMN_DAYSHIGH = 8;
    public static final int COLUMN_DAYSLOW = 9;
    public static final int COLUMN_TEXTCOLOR = 10;


    public long insert (Stock stockInfo)
    {
        // bind values to the pre-compiled SQL statement "inserStmt"
        insertStmt.bindString(COLUMN_NAME, stockInfo.getName());
        insertStmt.bindString(COLUMN_EXCHANGE, stockInfo.getExchange());
        insertStmt.bindString(COLUMN_SYMBOL, stockInfo.getSymbol());
        insertStmt.bindString(COLUMN_EXCHANGE, stockInfo.getExchange());
        insertStmt.bindString(COLUMN_OPEN, stockInfo.getOpen());
        insertStmt.bindString(COLUMN_LASTTRADEPRICE, stockInfo.getLastTradePrice());
        insertStmt.bindString(COLUMN_CHANGE, stockInfo.getChange());
        insertStmt.bindString(COLUMN_CHANGEPERCENT, stockInfo.getChangePercent());
        insertStmt.bindString(COLUMN_DAYSHIGH, stockInfo.getDaysHigh());
        insertStmt.bindString(COLUMN_DAYSLOW, stockInfo.getDaysLow());
        insertStmt.bindString(COLUMN_TEXTCOLOR, stockInfo.getTextColor());


        long value =-1;
        try {
//  Execute the sqlite statement.
            value = insertStmt.executeInsert();
        } catch (Exception e) {
            Log.e(LOGTAG, " executeInsert problem: " + e);
        }
        Log.d(LOGTAG, "value=" + value);
        return value;
    }

    public void deleteAll()
    {
        db.delete(TABLE_NAME, null, null);
    }

    // delete a row in the database
    public boolean deleteRecord(long rowId)
    {
        return db.delete(TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
    }

    public ArrayList<Stock> selectAll()
    {
        ArrayList<Stock> list = new ArrayList<Stock>();


        // query takes the following parameters
        // dbName :  the table name
        // columnNames:  a list of which table columns to return
        // whereClause:  filter of selection of data;  null selects all data
        // selectionArg: values to fill in the ? if any are in the whereClause
        // group by:   Filter specifying how to group rows, null means no grouping
        // having:  filter for groups, null means none
        // orderBy:  Table columns used to order the data, null means no order.

        // A Cursor provides read-write access to the result set returned by a database query.
        // A Cursor represents the result of the query and points to one row of the query result.
        Cursor cursor = db.query(TABLE_NAME,
                new String[] { KEY_ID, KEY_NAME, KEY_SYMBOL, KEY_EXCHANGE, KEY_OPEN, KEY_LASTTRADEPRICE, KEY_CHANGE, KEY_CHANGEPERCENT, KEY_DAYSHIGH, KEY_DAYSLOW, KEY_TEXTCOLOR},
                null, null, null, null, null, null);


        if (cursor.moveToFirst())
        {
            do
            {
                Stock stockInfo = new Stock();
                stockInfo.setName(cursor.getString(COLUMN_NAME));
                stockInfo.setSymbol(cursor.getString(COLUMN_SYMBOL));
                stockInfo.setExchange(cursor.getString(COLUMN_EXCHANGE));
                stockInfo.setOpen(cursor.getString(COLUMN_OPEN));
                stockInfo.setLastTradePrice(cursor.getString(COLUMN_LASTTRADEPRICE));
                stockInfo.setChange(cursor.getString(COLUMN_CHANGE));
                stockInfo.setChangePercent(cursor.getString(COLUMN_CHANGEPERCENT));
                stockInfo.setDaysHigh(cursor.getString(COLUMN_DAYSHIGH));
                stockInfo.setDaysLow(cursor.getString(COLUMN_DAYSLOW));
                stockInfo.setTextColor(cursor.getString(COLUMN_TEXTCOLOR));
                stockInfo.setId(cursor.getLong(COLUMN_ID));
                list.add(stockInfo);
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        return list;
    }

    // Helper class for DB creation/update
    // SQLiteOpenHelper provides getReadableDatabase() and getWriteableDatabase() methods
    // to get access to an SQLiteDatabase object; either in read or write mode.

    private static class OpenHelper extends SQLiteOpenHelper {
        private static final String LOGTAG = "OpenHelper";

        private static final String CREATE_TABLE =
                "CREATE TABLE " +
                        TABLE_NAME +
                        " (" + KEY_ID + " integer primary key autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_SYMBOL + " TEXT, " +
                        KEY_EXCHANGE + " TEXT, " +
                        KEY_OPEN + " TEXT, " +
                        KEY_LASTTRADEPRICE + " TEXT, " +
                        KEY_CHANGE + " TEXT, " +
                        KEY_CHANGEPERCENT + " TEXT, " +
                        KEY_DAYSHIGH + " TEXT, " +
                        KEY_DAYSLOW + " TEXT, " +
                        KEY_TEXTCOLOR + " TEXT);";

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Creates the tables.
         * This function is only run once or after every Clear Data
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOGTAG, " onCreate");
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.e(LOGTAG, " onCreate:  Could not create SQL database: " + e);
            }
        }

        /**
         * called, if the database version is increased in your application code.
         * This method updating an existing database schema or dropping the existing
         * database and recreating it via the onCreate() method.
         */

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOGTAG, "Upgrading database, this will drop tables and recreate.");
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e) {
                Log.e(LOGTAG, " onUpgrade:  Could not update SQL database: " + e);
            }

            // Technique to add a column rather than recreate the tables.
            //   String upgradeQuery_ADD_AREA =
//       "ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + KEY_AREA + " TEXT ";
            //   if(oldVersion <2 ){
            //    	db.execSQL(upgradeQuery_ADD_AREA);

        }


    }
}

