package com.supwisdom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.supwisdom.utilities.LocaUserInfor;
import com.supwisdom.utilities.CommonUtil;

/**
 * Created by jov on 2015/1/21.
 */
public class UserDao {
    private static UserDao userDao;
    private DBHelper dbHelper;

    private UserDao(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public static UserDao getInstance(Context context) {
        if (userDao == null) {
            userDao = new UserDao(context);
        }
        return userDao;
    }

    public boolean saveUser(LocaUserInfor user) throws SQLException {
        if (user == null) {
            return false;
        }
        if (CommonUtil.isEmpty(user.getUid())) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "insert into  " + DBHelper.TABLE_NAME_USER + "( "
                + BeanPropEnum.LocalUserProp.uid + ","
                + BeanPropEnum.LocalUserProp.idno + ","
                + BeanPropEnum.LocalUserProp.username + ","
                + BeanPropEnum.LocalUserProp.phone + ","
                + BeanPropEnum.LocalUserProp.isAutoLogin + ","
                + BeanPropEnum.LocalUserProp.isLogined + ","
                + BeanPropEnum.LocalUserProp.gid + ","
                + BeanPropEnum.LocalUserProp.isRemembed + ","
                + BeanPropEnum.LocalUserProp.money + ","
                + BeanPropEnum.LocalUserProp.passwd + ","
                + BeanPropEnum.LocalUserProp.rsapbulic + ","
                + BeanPropEnum.LocalUserProp.school + ","
                + BeanPropEnum.LocalUserProp.isPwdSeted + ","
                + BeanPropEnum.LocalUserProp.userid + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new String[]{user.getUid(), user.getIdno(), user.getUsername(), user.getPhone()
                , user.isAutoLogin() ? "1" : "0", user.isLogined() ? "1" : "0", user.getGid(), user.isRemembed() ? "1" : "0", user.getMoney()
                , user.getPasswd(), user.getRsapbulic(), user.getSchool(), user.isPwdSeted() ? "1" : "0", user.getUserid()});
        Log.d("insert sql=", sql);
        db.close();
        return true;
    }

    public boolean updateUser(LocaUserInfor user, String uid) {
        if (user == null) {
            return false;
        }
        if (CommonUtil.isEmpty(user.getUid())) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BeanPropEnum.LocalUserProp.idno.toString(), user.getIdno());
        values.put(BeanPropEnum.LocalUserProp.uid.toString(), user.getUid());
        values.put(BeanPropEnum.LocalUserProp.username.toString(), user.getUsername());
        values.put(BeanPropEnum.LocalUserProp.phone.toString(), user.getPhone());
        values.put(BeanPropEnum.LocalUserProp.isAutoLogin.toString(), user.isAutoLogin() ? "1" : "0");
        values.put(BeanPropEnum.LocalUserProp.isLogined.toString(), user.isLogined() ? "1" : "0");
        values.put(BeanPropEnum.LocalUserProp.gid.toString(), user.getGid());
        values.put(BeanPropEnum.LocalUserProp.isRemembed.toString(), user.isRemembed() ? "1" : "0");
        values.put(BeanPropEnum.LocalUserProp.money.toString(), user.getMoney());
        values.put(BeanPropEnum.LocalUserProp.passwd.toString(), user.getPasswd());
        values.put(BeanPropEnum.LocalUserProp.rsapbulic.toString(), user.getRsapbulic());
        values.put(BeanPropEnum.LocalUserProp.school.toString(), user.getSchool());
        values.put(BeanPropEnum.LocalUserProp.userid.toString(), user.getUserid());
        values.put(BeanPropEnum.LocalUserProp.isPwdSeted.toString(), user.isPwdSeted() ? "1" : "0");
        db.update(DBHelper.TABLE_NAME_USER, values, BeanPropEnum.LocalUserProp.uid + "=?", new String[]{uid});
        db.close();
        return true;
    }

    public LocaUserInfor getUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = " select " + BeanPropEnum.LocalUserProp.uid + ","
                + BeanPropEnum.LocalUserProp.idno + ","
                + BeanPropEnum.LocalUserProp.username + ","
                + BeanPropEnum.LocalUserProp.phone + ","
                + BeanPropEnum.LocalUserProp.isAutoLogin + ","
                + BeanPropEnum.LocalUserProp.isLogined + ","
                + BeanPropEnum.LocalUserProp.gid + ","
                + BeanPropEnum.LocalUserProp.isRemembed + ","
                + BeanPropEnum.LocalUserProp.money + ","
                + BeanPropEnum.LocalUserProp.passwd + ","
                + BeanPropEnum.LocalUserProp.rsapbulic + ","
                + BeanPropEnum.LocalUserProp.school + ","
                + BeanPropEnum.LocalUserProp.isPwdSeted + " , "
                + BeanPropEnum.LocalUserProp.userid + " from " + DBHelper.TABLE_NAME_USER;
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        LocaUserInfor bean = null;
        if (cursor != null && cursor.moveToNext()) {
            bean = new LocaUserInfor();
            bean.setUid(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.uid.toString())));
            String autoLoginStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.isAutoLogin.toString()));
            int autologin = 0;
            if (!CommonUtil.isEmpty(autoLoginStr)) {
                autologin = Integer.parseInt(autoLoginStr);
            }
            bean.setAutoLogin(false);
            if (autologin != 0) {
                bean.setAutoLogin(true);
            }
            String loginStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.isLogined.toString()));
            int login = 0;
            if (!CommonUtil.isEmpty(loginStr)) {
                login = Integer.parseInt(loginStr);
            }
            bean.setLogined(false);
            if (login != 0) {
                bean.setLogined(true);
            }
            String rememberStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.isRemembed.toString()));
            int remember = 0;
            if (!CommonUtil.isEmpty(rememberStr)) {
                remember = Integer.parseInt(rememberStr);
            }
            bean.setRemembed(false);
            if (remember != 0) {
                bean.setRemembed(true);
            }
            String pwdsetStr = cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.isPwdSeted.toString()));
            int pwdset = 0;
            if (!CommonUtil.isEmpty(pwdsetStr)) {
                pwdset = Integer.parseInt(pwdsetStr);
            }
            bean.setPwdSeted(false);
            if (pwdset != 0) {
                bean.setPwdSeted(true);
            }
            bean.setGid(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.gid.toString())));
            bean.setIdno(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.idno.toString())));
            bean.setMoney(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.money.toString())));
            bean.setPasswd(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.passwd.toString())));
            bean.setPhone(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.phone.toString())));
            bean.setRsapbulic(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.rsapbulic.toString())));
            bean.setSchool(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.school.toString())));
            bean.setUserid(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.userid.toString())));
            bean.setUsername(cursor.getString(cursor.getColumnIndex(BeanPropEnum.LocalUserProp.username.toString())));
        }
        return bean;
    }
}
