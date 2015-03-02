package com.supwisdom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.supwisdom.utilities.KeyValueBean;
import com.supwisdom.utilities.LocaUserInfor;
import com.supwisdom.utilities.CommonUtil;

import java.util.List;

/**
 * Created by jov on 2015/1/23.
 */
public class KeyValueMapDao {
    private static KeyValueMapDao keyValueMapDao;
    private DBHelper dbHelper;

    private KeyValueMapDao(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public static KeyValueMapDao getInstance(Context context) {
        if (keyValueMapDao == null) {
            keyValueMapDao = new KeyValueMapDao(context);
        }
        return keyValueMapDao;
    }

    public boolean saveOrUpdateKeyValue(KeyValueBean bean) {
        if (bean == null) {
            return false;
        }
        if (CommonUtil.isEmpty(bean.gettKey())) {
            return false;
        }
        if (CommonUtil.isEmpty(bean.gettType())) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //check if in
        String sql = " select " + BeanPropEnum.KeyValue.tValue + ","
                + BeanPropEnum.KeyValue.tType + " from " + DBHelper.TABLE_NAME_KEYVALUE + " where " + BeanPropEnum.KeyValue.tKey + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{bean.gettKey()});
        if (cursor != null && cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.gettValue());
            values.put(BeanPropEnum.KeyValue.tType.toString(), bean.gettType());
            db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{bean.gettKey()});
            cursor.close();
        } else {
            sql = "insert into  " + DBHelper.TABLE_NAME_KEYVALUE + "( "
                    + BeanPropEnum.KeyValue.tKey + ","
                    + BeanPropEnum.KeyValue.tValue + ","
                    + BeanPropEnum.KeyValue.tType
                    + ") values(?,?,?)";
            db.execSQL(sql, new String[]{bean.gettKey(), bean.gettValue(), bean.gettType()});
        }
        db.close();
        return true;
    }

    public boolean saveOrUpdateKeyValue(String key, String value) {
        if (CommonUtil.isEmpty(key)) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //check if in
        String sql = " select " + BeanPropEnum.KeyValue.tValue + " from " + DBHelper.TABLE_NAME_KEYVALUE + " where " + BeanPropEnum.KeyValue.tKey + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{key});
        if (cursor != null && cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(BeanPropEnum.KeyValue.tValue.toString(), value);
            db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{key});
            cursor.close();
        } else {
            sql = "insert into  " + DBHelper.TABLE_NAME_KEYVALUE + "( "
                    + BeanPropEnum.KeyValue.tKey + ","
                    + BeanPropEnum.KeyValue.tValue
                    + ") values(?,?)";
            db.execSQL(sql, new String[]{key, value});
        }
        db.close();
        return true;
    }

    /**
     * 注：value不更新时，type更新情况，不要使用这个方法
     */
    public boolean saveKeyValue(List<KeyValueBean> beans) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        StringBuilder sql = new StringBuilder(0);
        String value = null;
        for (KeyValueBean bean : beans) {
            sql.setLength(0);
            if (bean == null) {
                continue;
            }
            if (CommonUtil.isEmpty(bean.gettKey())) {
                continue;
            }
            if (CommonUtil.isEmpty(bean.gettValue())) {
                continue;
            }
            //check if in
            sql.append(" select " + BeanPropEnum.KeyValue.tValue + ","
                    + BeanPropEnum.KeyValue.tType + " from " + DBHelper.TABLE_NAME_KEYVALUE + " where " + BeanPropEnum.KeyValue.tKey + "=?");
            cursor = db.rawQuery(sql.toString(), new String[]{bean.gettKey()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || value.equals(bean.gettValue())) {
                    continue;
                }
                ContentValues values = new ContentValues();
                values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.gettValue());
                values.put(BeanPropEnum.KeyValue.tType.toString(), bean.gettType());
                db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{bean.gettKey()});
                cursor.close();
            } else {
                sql.setLength(0);
                sql.append(" insert into  " + DBHelper.TABLE_NAME_KEYVALUE + "( "
                        + BeanPropEnum.KeyValue.tKey + ","
                        + BeanPropEnum.KeyValue.tValue + ","
                        + BeanPropEnum.KeyValue.tType
                        + ") values(?,?,?)");
                db.execSQL(sql.toString(), new String[]{bean.gettKey(), bean.gettValue(), bean.gettType()});
            }
        }
        db.close();
        return true;
    }


    public boolean updateKeyValue(KeyValueBean bean) {
        if (bean == null) {
            return false;
        }
        if (CommonUtil.isEmpty(bean.gettKey())) {
            return false;
        }
        if (CommonUtil.isEmpty(bean.gettType())) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.gettValue());
        values.put(BeanPropEnum.KeyValue.tType.toString(), bean.gettType());
        db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{bean.gettKey()});
        db.close();
        return true;
    }

    public KeyValueBean getKeyValue(String keyname) {
        if (CommonUtil.isEmpty(keyname)) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = " select " + BeanPropEnum.KeyValue.tValue + ","
                + BeanPropEnum.KeyValue.tType + " from " + DBHelper.TABLE_NAME_KEYVALUE + " where " + BeanPropEnum.KeyValue.tKey + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{keyname});
        KeyValueBean bean = null;
        if (cursor != null && cursor.moveToNext()) {
            bean = new KeyValueBean();
            bean.settKey(keyname);
            bean.settValue(cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString())));
            bean.settType(cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tType.toString())));
        }
        db.close();
        return bean;
    }

    public String getValue(String keyname) {
        if (CommonUtil.isEmpty(keyname)) {
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = " select " + BeanPropEnum.KeyValue.tValue + " from " + DBHelper.TABLE_NAME_KEYVALUE + " where " + BeanPropEnum.KeyValue.tKey + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{keyname});
        String value = null;
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
        }
        db.close();
        return value;
    }

    /**
     * 取用户信息:
     * accesstoken
     * refreshtoken
     * userid
     * gid
     * passwd
     * isPwdSeted
     * isAutoLogin
     * isRemembed
     * isLogined
     */
    public LocaUserInfor getUserLess() {
        LocaUserInfor bean = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = " select " + BeanPropEnum.KeyValue.tValue + " from " + DBHelper.TABLE_NAME_KEYVALUE + " where " + BeanPropEnum.KeyValue.tKey + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.accesstoken.toString()});
        String value = null;
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setAccesstoken(value);
        }

        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.refreshtoken.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setRefreshtoken(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.userid.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setUserid(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.gid.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setGid(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.passwd.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setPasswd(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.isPwdSeted.toString()});
        if (cursor != null && cursor.moveToNext()) {
            String pwdsetStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            int pwdset = 0;
            if (!CommonUtil.isEmpty(pwdsetStr)) {
                pwdset = Integer.parseInt(pwdsetStr);
            }
            bean.setPwdSeted(false);
            if (pwdset != 0) {
                bean.setPwdSeted(true);
            }
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.isAutoLogin.toString()});
        if (cursor != null && cursor.moveToNext()) {
            String autoLoginStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            int autologin = 0;
            if (!CommonUtil.isEmpty(autoLoginStr)) {
                autologin = Integer.parseInt(autoLoginStr);
            }
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setAutoLogin(false);
            if (autologin != 0) {
                bean.setAutoLogin(true);
            }
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.isRemembed.toString()});
        if (cursor != null && cursor.moveToNext()) {
            String autoLoginStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            int autologin = 0;
            if (!CommonUtil.isEmpty(autoLoginStr)) {
                autologin = Integer.parseInt(autoLoginStr);
            }
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setRemembed(false);
            if (autologin != 0) {
                bean.setRemembed(true);
            }
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.isLogined.toString()});
        if (cursor != null && cursor.moveToNext()) {
            String autoLoginStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            int autologin = 0;
            if (!CommonUtil.isEmpty(autoLoginStr)) {
                autologin = Integer.parseInt(autoLoginStr);
            }
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setLogined(false);
            if (autologin != 0) {
                bean.setLogined(true);
            }
        }
        db.close();
        return bean;
    }

    /**
     * 取用户信息(全):
     */
    public LocaUserInfor getUserFull() {
        LocaUserInfor bean = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = " select " + BeanPropEnum.KeyValue.tValue + " from " + DBHelper.TABLE_NAME_KEYVALUE + " where " + BeanPropEnum.KeyValue.tKey + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.accesstoken.toString()});
        String value = null;
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setAccesstoken(value);
        }

        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.refreshtoken.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setRefreshtoken(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.userid.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setUserid(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.gid.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setGid(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.passwd.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setPasswd(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.username.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setUsername(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.isPwdSeted.toString()});
        if (cursor != null && cursor.moveToNext()) {
            String pwdsetStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            int pwdset = 0;
            if (!CommonUtil.isEmpty(pwdsetStr)) {
                pwdset = Integer.parseInt(pwdsetStr);
            }
            bean.setPwdSeted(false);
            if (pwdset != 0) {
                bean.setPwdSeted(true);
            }
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.isAutoLogin.toString()});
        if (cursor != null && cursor.moveToNext()) {
            String autoLoginStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            int autologin = 0;
            if (!CommonUtil.isEmpty(autoLoginStr)) {
                autologin = Integer.parseInt(autoLoginStr);
            }
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setAutoLogin(false);
            if (autologin != 0) {
                bean.setAutoLogin(true);
            }
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.isRemembed.toString()});
        if (cursor != null && cursor.moveToNext()) {
            String autoLoginStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            int autologin = 0;
            if (!CommonUtil.isEmpty(autoLoginStr)) {
                autologin = Integer.parseInt(autoLoginStr);
            }
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setRemembed(false);
            if (autologin != 0) {
                bean.setRemembed(true);
            }
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.isLogined.toString()});
        if (cursor != null && cursor.moveToNext()) {
            String autoLoginStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            int autologin = 0;
            if (!CommonUtil.isEmpty(autoLoginStr)) {
                autologin = Integer.parseInt(autoLoginStr);
            }
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setLogined(false);
            if (autologin != 0) {
                bean.setLogined(true);
            }
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.uid.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setUid(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.school.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setSchool(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.idno.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setIdno(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.phone.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setPhone(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.rsapbulic.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setRsapbulic(value);
        }
        cursor = db.rawQuery(sql, new String[]{BeanPropEnum.LocalUserProp.money.toString()});
        if (cursor != null && cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
            if (bean == null) {
                bean = new LocaUserInfor();
            }
            bean.setMoney(value);
        }
        db.close();
        return bean;
    }

    public boolean saveOrUpdateUser(LocaUserInfor bean) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        StringBuilder sqlSelect = new StringBuilder(" select " + BeanPropEnum.KeyValue.tValue + ","
                + BeanPropEnum.KeyValue.tType + " from " + DBHelper.TABLE_NAME_KEYVALUE + " where " + BeanPropEnum.KeyValue.tKey + "=?");
        StringBuilder sqlInsert = new StringBuilder(" insert into  " + DBHelper.TABLE_NAME_KEYVALUE + "( "
                + BeanPropEnum.KeyValue.tKey + ","
                + BeanPropEnum.KeyValue.tValue
                + ") values(?,?)");
        String value = null;
        if (bean == null) {
            return false;
        }
        if (!CommonUtil.isEmpty(bean.getUid())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.uid.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getUid())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getUid());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.uid.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.uid.toString(), bean.getUid()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getPhone())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.phone.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getPhone())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getPhone());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.phone.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.phone.toString(), bean.getPhone()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getIdno())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.idno.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getIdno())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getIdno());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.idno.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.idno.toString(), bean.getIdno()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getMoney())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.money.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getMoney())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getMoney());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.money.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.money.toString(), bean.getMoney()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getRsapbulic())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.rsapbulic.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getRsapbulic())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getRsapbulic());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.rsapbulic.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.rsapbulic.toString(), bean.getRsapbulic()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getSchool())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.school.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getSchool())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getSchool());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.school.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.school.toString(), bean.getSchool()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getSchoolcode())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.schoolcode.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getSchoolcode())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getSchoolcode());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.schoolcode.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.schoolcode.toString(), bean.getSchoolcode()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getAccesstoken())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.accesstoken.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getAccesstoken())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getAccesstoken());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.accesstoken.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.accesstoken.toString(), bean.getAccesstoken()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getRefreshtoken())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.refreshtoken.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getRefreshtoken())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getRefreshtoken());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.refreshtoken.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.refreshtoken.toString(), bean.getRefreshtoken()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getGid())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.gid.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getGid())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getGid());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.gid.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.gid.toString(), bean.getGid()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getUserid())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.userid.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getUserid())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getUserid());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.userid.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.userid.toString(), bean.getUserid()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getUsername())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.username.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getUsername())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getUsername());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.username.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.username.toString(), bean.getUsername()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getPasswd())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.passwd.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getPasswd())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getPasswd());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.passwd.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.passwd.toString(), bean.getPasswd()});
            }
        }
        if (!CommonUtil.isEmpty(bean.getSecretkey())) {
            cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.secretkey.toString()});
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BeanPropEnum.KeyValue.tValue.toString()));
                if (value == null || !value.equals(bean.getSecretkey())) {
                    ContentValues values = new ContentValues();
                    values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.getSecretkey());
                    db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.secretkey.toString()});
                }
                cursor.close();
            } else {
                db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.secretkey.toString(), bean.getSecretkey()});
            }
        }
        cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.isPwdSeted.toString()});
        if (cursor != null && cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.isPwdSeted() ? "1" : "0");
            db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.isPwdSeted.toString()});
            cursor.close();
        } else {
            db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.isPwdSeted.toString(), bean.isPwdSeted() ? "1" : "0"});
        }
        cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.isLogined.toString()});
        if (cursor != null && cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.isLogined() ? "1" : "0");
            db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.isLogined.toString()});
            cursor.close();
        } else {
            db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.isLogined.toString(), bean.isLogined() ? "1" : "0"});
        }
        cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.isRemembed.toString()});
        if (cursor != null && cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.isRemembed() ? "1" : "0");
            db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.isRemembed.toString()});
            cursor.close();
        } else {
            db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.isRemembed.toString(), bean.isRemembed() ? "1" : "0"});
        }
        cursor = db.rawQuery(sqlSelect.toString(), new String[]{BeanPropEnum.LocalUserProp.isAutoLogin.toString()});
        if (cursor != null && cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(BeanPropEnum.KeyValue.tValue.toString(), bean.isAutoLogin() ? "1" : "0");
            db.update(DBHelper.TABLE_NAME_KEYVALUE, values, BeanPropEnum.KeyValue.tKey + "=?", new String[]{BeanPropEnum.LocalUserProp.isAutoLogin.toString()});
            cursor.close();
        } else {
            db.execSQL(sqlInsert.toString(), new String[]{BeanPropEnum.LocalUserProp.isAutoLogin.toString(), bean.isAutoLogin() ? "1" : "0"});
        }
        db.close();
        return true;
    }
}
