/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */
package sample.knot.cesar.org.br.drinkingfountain.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.org.cesar.knot.lib.util.LogLib;
import sample.knot.cesar.org.br.drinkingfountain.util.Util;

public class DrinkFountainDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "drink_fountain_database";

    private static final int VERSION_DATABASE = 1;

    private static final String SQL_CREATE = "script_create_database.sql";


    private Context mContext;

    public DrinkFountainDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION_DATABASE);

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        LogLib.printD("onCreate Database"+sqLiteDatabase.toString());
        executeSqlCommand(sqLiteDatabase, SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        LogLib.printD("onUpgrade Database"+sqLiteDatabase.toString());
    }

    /**
     * Use to execute all sql instructions in sqls into the assets folder
     * @param sqLiteDatabase
     * @param sqlFileName
     */
    private void executeSqlCommand(SQLiteDatabase sqLiteDatabase, String sqlFileName) {
        LogLib.printD("executeSqlCommand Database");
        try {
            //Get all sql instructions from the SQL_NAME_ASSETS file.
            String[] sqlInstructions = Util.getStatementSql(mContext, sqlFileName);

            for (final String sql : sqlInstructions) {
                if (sql != null && !sql.isEmpty()) {
                    sqLiteDatabase.execSQL(sql);
                }
            }
        } catch (IllegalArgumentException e) {
            LogLib.printE("There is a problem on sql", e);
        }
    }
}
