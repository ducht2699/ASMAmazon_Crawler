package com.vnsoft.exam.data.realm;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by GNUD on 05/12/2017.
 */

public interface RealmHelper {

    void clear();

    <T extends RealmObject> void clear(Class<T> type);

    <T extends RealmObject> void delete(T item);

    <T extends RealmObject> void save(T item);

    <T extends RealmObject> T findFirst(Class<T> type, String key, int value);

    <T extends RealmObject> T findFirst(Class<T> type, String key, String value);

    <T extends RealmObject> T findFirst(Class<T> type);

    <T extends RealmObject> RealmResults findAll(Class<T> type);

    <T extends RealmObject> RealmResults findAllSorted(Class<T> type, String field, Sort sort);

    <T extends RealmObject> RealmResults findAllAsync(Class<T> type);

    <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String field, Sort sort);

    <T extends RealmObject> T findOneAsync(Class<T> type, String key, int value);

    <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key, int value, String field, Sort sort);

    <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key, String value);

    <T extends RealmObject> RealmResults findAllSortedAsync(Class<T> type, String key1, String value1, String key2, boolean value2);

    <T extends RealmObject> T findFirst1(Class<T> type, String key, String value);
}
