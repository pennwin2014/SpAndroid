package com.supwisdom.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    /**
     * table
     */
    public static final String TABLE_NAME_TRANSDTL = "tb_transdtl";
    public static final String TABLE_NAME_USER = "tb_user";
    public static final String TABLE_NAME_KEYVALUE = "tb_mapkey";
    private static final String DB_NAME = "supwisdom.swpos3.db";
    /**
     * version
     */
    private static final int VERSION = 1;
    /**
     * SQL for create table
     */
    private static final String CREATE_TABLE_TRANSDTL = "create table IF NOT EXISTS  "
            + TABLE_NAME_TRANSDTL
            + "( " + BeanPropEnum.Transdtl.transNo + " integer primary key,"
            + BeanPropEnum.Transdtl.cardNo + " integer,"
            + BeanPropEnum.Transdtl.lastTransCount + " integer,"
            + BeanPropEnum.Transdtl.lastLimitAmount + "  integer,"
            + BeanPropEnum.Transdtl.lastAmount + " integer," + BeanPropEnum.Transdtl.lastTransFlag + " integer,"
            + BeanPropEnum.Transdtl.lastTermno + " varchar(6)," + BeanPropEnum.Transdtl.lastDatetime + "  varchar(6),"
            + BeanPropEnum.Transdtl.cardBeforeBalance + " integer," + BeanPropEnum.Transdtl.cardBeforeCount + " integer,"
            + BeanPropEnum.Transdtl.amount + " integer," + BeanPropEnum.Transdtl.extraAmount + " integer,"
            + BeanPropEnum.Transdtl.transDatatime + " varchar(6)," + BeanPropEnum.Transdtl.samNo + " varchar(6),"
            + BeanPropEnum.Transdtl.tac + " varchar(4)," + BeanPropEnum.Transdtl.transFlag + " integer,"
            + BeanPropEnum.Transdtl.reserve + " varchar(2)," + BeanPropEnum.Transdtl.crc + " varchar(2) )";
    private static final String CREATE_TABLE_USER = "create table IF NOT EXISTS  "
            + TABLE_NAME_USER
            + "( " + BeanPropEnum.LocalUserProp.uid + " varchar(100) primary key,"
            + BeanPropEnum.LocalUserProp.userid + " varchar(20),"
            + BeanPropEnum.LocalUserProp.phone + " varchar(12),"
            + BeanPropEnum.LocalUserProp.username + "  varchar(40),"
            + BeanPropEnum.LocalUserProp.idno + " varchar(10)," + BeanPropEnum.LocalUserProp.gid + " varchar(40),"
            + BeanPropEnum.LocalUserProp.rsapbulic + " varchar(260)," + BeanPropEnum.LocalUserProp.isLogined + "  char(1),"
            + BeanPropEnum.LocalUserProp.isRemembed + " char(1)," + BeanPropEnum.LocalUserProp.isAutoLogin + " char(1),"
            + BeanPropEnum.LocalUserProp.school + " varchar(30)," + BeanPropEnum.LocalUserProp.money + " varchar(10),"
            + BeanPropEnum.LocalUserProp.isPwdSeted + " char(1)," + BeanPropEnum.LocalUserProp.passwd + " varchar(34) )";
    private static final String CREATE_TABLE_KEYVALUE = "create table if not exists "
            + TABLE_NAME_KEYVALUE + "("
            + BeanPropEnum.KeyValue.tKey + " varchar(40) primary key ,"
            + BeanPropEnum.KeyValue.tValue + " varchar(300) ,"
            + BeanPropEnum.KeyValue.tType + " varchar(10) )";

    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "
            + TABLE_NAME_USER;
    private SQLiteDatabase db;
    private static DBHelper instance = null;


    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_KEYVALUE);
        db.execSQL(CREATE_TABLE_TRANSDTL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }
}
