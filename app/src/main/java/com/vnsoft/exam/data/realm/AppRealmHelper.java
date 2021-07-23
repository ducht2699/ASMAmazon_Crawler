package com.vnsoft.exam.data.realm;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.vnsoft.exam.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Case;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.Sort;

/**
 * Created by GNUD on 05/12/2017.
 */

@Singleton
public class AppRealmHelper implements RealmHelper {

    private static final String TAG = AppRealmHelper.class.getSimpleName();

    private Realm mRealm;

    @Inject
    public AppRealmHelper(@ApplicationContext Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .schemaVersion(0)
                .migration(new MyMigration())
                .build();
        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void clear() {
        mRealm = Realm.getDefaultInstance();
        RealmConfiguration configs = mRealm.getConfiguration();
        mRealm.close();
        Realm.deleteRealm(configs);
    }

    @Override
    public <T extends RealmObject> void clear(Class<T> type) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.delete(type);
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    public <T extends RealmObject> void delete(T item) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        item.deleteFromRealm();
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    public <T extends RealmObject> void save(T item) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(item);
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    public <T extends RealmObject> T findFirst(Class<T> type, String key, int value) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).equalTo(key, value).findFirst();
    }

    @Override
    public <T extends RealmObject> T findFirst(Class<T> type, String key, String value) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).equalTo(key, value).findFirst();
    }

    @Override
    public <T extends RealmObject> T findFirst(Class<T> type) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).findFirst();
    }

    @Override
    public <T extends RealmObject> RealmResults findAll(Class<T> type) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).findAll();
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSorted(Class<T> type, String field, Sort sort) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).findAll().sort(field, sort);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllAsync(Class<T> type) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).findAllAsync();
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String field, Sort sort) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).findAllAsync().sort(field, sort);
    }

    @Override
    public <T extends RealmObject> T findOneAsync(Class<T> type, String key, int value) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).equalTo(key, value).findFirstAsync();
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key, int value, String field, Sort sort) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).equalTo(key, value).findAllAsync().sort(field, sort);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key, String value) {
        mRealm = Realm.getDefaultInstance();
        return mRealm.where(type).contains(key, value, Case.INSENSITIVE).findAllAsync().sort("name", Sort.ASCENDING);
    }

    @Override
    public <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key1, String value1, String key2, boolean value2) {
        return mRealm.where(type).equalTo(key2, value2).contains(key1, value1, Case.INSENSITIVE).findAllAsync().sort("name", Sort.ASCENDING);
    }

    @Override
    public <T extends RealmObject> T findFirst1(Class<T> type, String key, String value) {
        return mRealm.where(type).contains(key, value, Case.INSENSITIVE).findFirst();
    }


    private class MyMigration implements RealmMigration {

        @Override
        public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
            Log.i(TAG, "migrate: " + oldVersion + " " + newVersion);
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();

//            if (oldVersion == 1) {
//                schema.create("Account")
//                        .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
//                        .addField("_id", String.class)
//                        .addField("first_name", String.class)
//                        .addField("last_name", String.class)
//                        .addField("username", String.class)
//                        .addField("email", String.class)
//                        .addField("password", String.class)
//                        .addField("access_token", String.class);
//                Log.i(TAG, "migrate: create Account.class");
//            }
        }
    }
}
