package com.example.hawi.medmarket;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by Hawi on 19/02/18.
 */

public class SharedManager {

    private static SharedManager mInstance;
    private static Context mCtx;

    //هذه المتغيرات لكي نستخدمها لحفظ بيانات المستخدم وتعمل هنا كمفاتيح
    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_PHONE = "userphone";
    //private static final String KEY_ORDER_ID = "orderid";

    private SharedManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedManager(context);
        }
        return mInstance;
    }

    // هذه الدالة تستخدم لحفظ معلومات المستخدم عند تسجيل الدخول
    public boolean userLogin(int user_id , String username, int phone){
        // نأخذ مثيل من الكلاس SharedPreferences ويحمل عدد 2 براميتر الاول اسم من عندنا يميزه والثاني نجعله برايفت اي خاص بالتطبيق فقط
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        // نقوم الان بالتعديل على بيانات الكلاس SharedPreferences ونجعله يحفظ لنا المعلومات التي نعطيه
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID,user_id);
        editor.putString(KEY_USER_NAME,username);
        editor.putInt(KEY_USER_PHONE,phone);

        // ثم نعمل تطبيق على الكلاس ليتم حفظ البيانات التي اعطيناه
        editor.apply();

        return true;
    }

    // هذه الدالة تستخدم للتاكد هل تم تسجيل الدخول ام لا
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USER_NAME,null) != null){
            return true;
        }
        return false;
    }

    // هذه الدالة لعمل تسجيل الخروج
    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear().apply();
        deleteCache(mCtx);
        return true;
    }

    private static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public String getUserName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME,null);
    }

    public int getUserPhone(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_PHONE,0);
    }

    public static int getUserID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID,0);
    }

//    public static int getOrderID(){
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(KEY_USER_ID,0);
//    }
//
//    // هذه الدالة تستخدم لحفظ معلومات المستخدم عند تسجيل الدخول
//    public void setOrderID(int id ){
//        // نأخذ مثيل من الكلاس SharedPreferences ويحمل عدد 2 براميتر الاول اسم من عندنا يميزه والثاني نجعله برايفت اي خاص بالتطبيق فقط
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
//        // نقوم الان بالتعديل على بيانات الكلاس SharedPreferences ونجعله يحفظ لنا المعلومات التي نعطيه
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putInt(KEY_USER_ID,id);
//
//        // ثم نعمل تطبيق على الكلاس ليتم حفظ البيانات التي اعطيناه
//        editor.apply();
//
//    }

}
