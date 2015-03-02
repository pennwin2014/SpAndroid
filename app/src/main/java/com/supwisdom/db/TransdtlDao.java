package com.supwisdom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.supwisdom.utilities.ErrorDef;
import com.supwisdom.utilities.ErrorInfo;
import com.supwisdom.utilities.TransRecord;

import java.io.IOException;

/**
 * Created by Penn on 2015-03-02.
 */
public class TransdtlDao {
    private static TransdtlDao transdtlDao;
    private DBHelper dbHelper;
    private String tag = "com.supwisdom.db.TransdtlDao";

    private TransdtlDao(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public static TransdtlDao getInstance(Context context) {
        if (transdtlDao == null) {
            transdtlDao = new TransdtlDao(context);
        }
        return transdtlDao;
    }

    public ErrorInfo saveTransdtl(TransRecord record) throws SQLException {
        if (record == null) {
            return ErrorDef.SP_TRANS_RECORD_NULL;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "insert into  " + DBHelper.TABLE_NAME_TRANSDTL + "( "
                + BeanPropEnum.Transdtl.transNo + ","
                + BeanPropEnum.Transdtl.cardNo + ","
                + BeanPropEnum.Transdtl.lastTransCount + ","
                + BeanPropEnum.Transdtl.lastLimitAmount + ","
                + BeanPropEnum.Transdtl.lastAmount + ","
                + BeanPropEnum.Transdtl.lastTransFlag + ","
                + BeanPropEnum.Transdtl.lastTermno + ","
                + BeanPropEnum.Transdtl.lastDatetime + ","
                + BeanPropEnum.Transdtl.cardBeforeBalance + ","
                + BeanPropEnum.Transdtl.cardBeforeCount + ","
                + BeanPropEnum.Transdtl.amount + ","
                + BeanPropEnum.Transdtl.extraAmount + ","
                + BeanPropEnum.Transdtl.transDatatime + ","
                + BeanPropEnum.Transdtl.samNo + ","
                + BeanPropEnum.Transdtl.tac + ","
                + BeanPropEnum.Transdtl.transFlag + ","
                + BeanPropEnum.Transdtl.reserve + ","
                + BeanPropEnum.Transdtl.crc + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try{
            db.execSQL(sql, new String[]{String.valueOf(record.getTransno()), String.valueOf(record.getCardno()), String.valueOf(record.getLastTransCount()), String.valueOf(record.getLastLimitAmount())
                    , String.valueOf(record.getLastAmount()), String.valueOf(record.getLastTransFlag()), record.getLastTermno(), record.getLastDatetime(), String.valueOf(record.getCardBeforeBalance())
                    , String.valueOf(record.getCardBeforeCount()), String.valueOf(record.getAmount()), String.valueOf(record.getExtraAmount()), record.getTransDatatime(), record.getSamNo(),
                    record.getTac(), String.valueOf(record.getTransFlag()), record.getReserve(), record.getCrc()});
            Log.i("insert sql=", sql);
        }catch (Exception e){
            Log.d(tag, "插入数据库报错 "+e.getMessage());
            return ErrorDef.SP_INSERT_TRANSDTL_ERROR;
        }
        return ErrorDef.SP_SUCCESS;
    }

    public ErrorInfo updateTransdtl(TransRecord record, int transNo) {
        if (record == null) {
            return ErrorDef.SP_TRANS_RECORD_NULL;
        }
        if (record.getTransno() <= 0) {
            return ErrorDef.SP_TRANSNO_ERROR;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BeanPropEnum.Transdtl.cardNo.toString(), record.getCardno());
        values.put(BeanPropEnum.Transdtl.lastTransCount.toString(), record.getLastTransCount());
        values.put(BeanPropEnum.Transdtl.lastLimitAmount.toString(), record.getLastLimitAmount());
        values.put(BeanPropEnum.Transdtl.lastAmount.toString(), record.getLastAmount());
        values.put(BeanPropEnum.Transdtl.lastTransFlag.toString(), record.getLastTransFlag());
        values.put(BeanPropEnum.Transdtl.lastTermno.toString(), record.getLastTermno());
        values.put(BeanPropEnum.Transdtl.lastDatetime.toString(), record.getLastDatetime());
        values.put(BeanPropEnum.Transdtl.cardBeforeBalance.toString(), record.getCardBeforeBalance());
        values.put(BeanPropEnum.Transdtl.cardBeforeCount.toString(), record.getCardBeforeCount());
        values.put(BeanPropEnum.Transdtl.amount.toString(), record.getAmount());
        values.put(BeanPropEnum.Transdtl.extraAmount.toString(), record.getExtraAmount());
        values.put(BeanPropEnum.Transdtl.transDatatime.toString(), record.getTransDatatime());
        values.put(BeanPropEnum.Transdtl.samNo.toString(), record.getSamNo());
        values.put(BeanPropEnum.Transdtl.tac.toString(), record.getTac());
        values.put(BeanPropEnum.Transdtl.transFlag.toString(), record.getTransFlag());
        values.put(BeanPropEnum.Transdtl.reserve.toString(), record.getReserve());
        values.put(BeanPropEnum.Transdtl.crc.toString(), record.getCrc());
        db.update(DBHelper.TABLE_NAME_TRANSDTL, values, BeanPropEnum.Transdtl.transNo + "=?", new String[]{String.valueOf(transNo)});
        db.close();
        return ErrorDef.SP_SUCCESS;
    }

    public TransRecord getTransdtl() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = " select " + BeanPropEnum.Transdtl.transNo + ","
                + BeanPropEnum.Transdtl.cardNo + ","
                + BeanPropEnum.Transdtl.lastTransCount + ","
                + BeanPropEnum.Transdtl.lastLimitAmount + ","
                + BeanPropEnum.Transdtl.lastAmount + ","
                + BeanPropEnum.Transdtl.lastTransFlag + ","
                + BeanPropEnum.Transdtl.lastTermno + ","
                + BeanPropEnum.Transdtl.lastDatetime + ","
                + BeanPropEnum.Transdtl.cardBeforeBalance + ","
                + BeanPropEnum.Transdtl.cardBeforeCount + ","
                + BeanPropEnum.Transdtl.amount + ","
                + BeanPropEnum.Transdtl.extraAmount + ","
                + BeanPropEnum.Transdtl.transDatatime + ","
                + BeanPropEnum.Transdtl.samNo + ","
                + BeanPropEnum.Transdtl.tac + ","
                + BeanPropEnum.Transdtl.transFlag + ","
                + BeanPropEnum.Transdtl.reserve + ","
                + BeanPropEnum.Transdtl.crc + " from " + DBHelper.TABLE_NAME_TRANSDTL;
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        TransRecord record = null;
        if (cursor != null && cursor.moveToNext()) {
            record = new TransRecord();
            record.setTransNo(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.transNo.toString())));
            record.setCardno(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.cardNo.toString())));
            record.setLastTransCount(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.lastTransCount.toString())));
            record.setLastLimitAmount(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.lastLimitAmount.toString())));
            record.setAmount(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.lastAmount.toString())));
            record.setLastTransFlag(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.lastTransFlag.toString())));
            record.setLastTermno(cursor.getString(cursor.getColumnIndex(BeanPropEnum.Transdtl.lastTermno.toString())));
            record.setLastDatetime(cursor.getString(cursor.getColumnIndex(BeanPropEnum.Transdtl.lastDatetime.toString())));
            record.setCardBeforeBalance(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.cardBeforeBalance.toString())));
            record.setCardBeforeCount(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.cardBeforeCount.toString())));
            record.setAmount(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.amount.toString())));
            record.setExtraAmount(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.extraAmount.toString())));
            record.setTransDatatime(cursor.getString(cursor.getColumnIndex(BeanPropEnum.Transdtl.transDatatime.toString())));
            record.setSamNo(cursor.getString(cursor.getColumnIndex(BeanPropEnum.Transdtl.samNo.toString())));
            record.setTac(cursor.getString(cursor.getColumnIndex(BeanPropEnum.Transdtl.tac.toString())));
            record.setTransFlag(cursor.getInt(cursor.getColumnIndex(BeanPropEnum.Transdtl.transFlag.toString())));
            record.setReserve(cursor.getString(cursor.getColumnIndex(BeanPropEnum.Transdtl.reserve.toString())));
            record.setCrc(cursor.getString(cursor.getColumnIndex(BeanPropEnum.Transdtl.crc.toString())));
        }
        return record;
    }
}
