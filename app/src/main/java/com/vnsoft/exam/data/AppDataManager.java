package com.vnsoft.exam.data;

import android.content.Context;

import com.vnsoft.exam.injection.ApplicationContext;
import com.vnsoft.exam.data.prefs.PreferencesHelper;
import com.vnsoft.exam.data.realm.RealmHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by GNUD on 02/12/2017.
 */

@Singleton
public class AppDataManager implements DataManager {


    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final PreferencesHelper mPreferencesHelper;
    private final RealmHelper mRealmHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          PreferencesHelper mPreferencesHelper,
                          RealmHelper mRealmHelper) {
        this.mContext = context;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mRealmHelper = mRealmHelper;
    }

    @Override
    public void save(String key, boolean value) {
        mPreferencesHelper.save(key, value);
    }

    @Override
    public void save(String key, String value) {
        mPreferencesHelper.save(key, value);
    }

    @Override
    public void save(String key, float value) {
        mPreferencesHelper.save(key, value);
    }

    @Override
    public void save(String key, int value) {
        mPreferencesHelper.save(key, value);
    }

    @Override
    public void save(String key, long value) {
        mPreferencesHelper.save(key, value);
    }

    @Override
    public boolean getBoolean(String key) {
        return mPreferencesHelper.getBoolean(key);
    }

    @Override
    public String getString(String key) {
        return mPreferencesHelper.getString(key);
    }

    @Override
    public long getLong(String key) {
        return mPreferencesHelper.getLong(key);
    }

    @Override
    public int getInt(String key) {
        return mPreferencesHelper.getInt(key);
    }

    @Override
    public float getFloat(String key) {
        return mPreferencesHelper.getFloat(key);
    }

    @Override
    public void remove(String key) {
        mPreferencesHelper.remove(key);
    }

    @Override
    public void clear() {
        mRealmHelper.clear();
    }

    @Override
    public <T extends RealmObject> void clear(Class<T> type) {
        mRealmHelper.clear(type);
    }

    @Override
    public <T extends RealmObject> void delete(T item) {
        mRealmHelper.delete(item);
    }

    @Override
    public <T extends RealmObject> void save(T item) {
        mRealmHelper.save(item);
    }

    @Override
    public <T extends RealmObject> T findFirst(Class<T> type, String key, int value) {
        return mRealmHelper.findFirst(type, key, value);
    }

    @Override
    public <T extends RealmObject> T findFirst(Class<T> type, String key, String value) {
        return mRealmHelper.findFirst(type, key, value);
    }

    @Override
    public <T extends RealmObject> T findFirst(Class<T> type) {
        return mRealmHelper.findFirst(type);
    }

    @Override
    public <T extends RealmObject> RealmResults findAll(Class<T> type) {
        return mRealmHelper.findAll(type);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSorted(Class<T> type, String field, Sort sort) {
        return mRealmHelper.findAllSorted(type, field, sort);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllAsync(Class<T> type) {
        return mRealmHelper.findAllAsync(type);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String field, Sort sort) {
        return mRealmHelper.findAllSortedAsync(type, field, sort);
    }

    @Override
    public <T extends RealmObject> T findOneAsync(Class<T> type, String key, int value) {
        return mRealmHelper.findOneAsync(type, key, value);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key, int value, String field, Sort sort) {
        return mRealmHelper.findAllSortedAsync(type, key, value, field, sort);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key, String value) {
        return mRealmHelper.findAllSortedAsync(type, key, value);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key1, String value1, String key2, boolean value2) {
        return mRealmHelper.findAllSortedAsync(type, key1, value1, key2, value2);
    }

    @Override
    public <T extends RealmObject> T findFirst1(Class<T> type, String key, String value) {
        return mRealmHelper.findFirst1(type, key, value);
    }
}
