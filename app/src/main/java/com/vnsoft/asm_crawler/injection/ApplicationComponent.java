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

package com.vnsoft.asm_crawler.injection;

import android.app.Application;
import android.content.Context;

import com.vnsoft.asm_crawler.base.MyApp;
import com.vnsoft.asm_crawler.data.DataManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by GNUD on 27/01/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApp app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();

}