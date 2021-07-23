/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.vnsoft.exam.data.prefs;

/**
 * Created by GNUD on 27/01/17.
 */

public interface PreferencesHelper {

    void save(String key, boolean value);

    void save(String key, String value);

    void save(String key, float value);

    void save(String key, int value);

    void save(String key, long value);

    boolean getBoolean(String key);

    String getString(String key);

    long getLong(String key);

    int getInt(String key);

    float getFloat(String key);

    void remove(String key);
}
