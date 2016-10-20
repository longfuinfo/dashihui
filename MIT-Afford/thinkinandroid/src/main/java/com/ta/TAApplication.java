/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ta;

import android.app.Application;

import com.ta.exception.TAAppException;
import com.ta.exception.TANoSuchCommandException;
import com.ta.mvc.command.TACommandExecutor;
import com.ta.mvc.command.TAICommand;
import com.ta.mvc.common.TAIResponseListener;
import com.ta.mvc.common.TARequest;
import com.ta.util.cache.TAFileCache;
import com.ta.util.cache.TAFileCache.TACacheParams;
import com.ta.util.config.TAIConfig;
import com.ta.util.config.TAPreferenceConfig;
import com.ta.util.config.TAPropertiesConfig;

import java.lang.Thread.UncaughtExceptionHandler;

public class TAApplication extends Application {
    /**
     * 配置器 为Preference
     */
    public final static int PREFERENCECONFIG = 0;
    /**
     * 配置器 为PROPERTIESCONFIG
     */
    public final static int PROPERTIESCONFIG = 1;
    /**
     * 配置器
     */
    private TAIConfig mCurrentConfig;

    /**
     * App异常崩溃处理器
     */
    private UncaughtExceptionHandler uncaughtExceptionHandler;
    private static TAApplication application;
    private TACommandExecutor mCommandExecutor;
    /**
     * ThinkAndroid 文件缓存
     */
    private TAFileCache mFileCache;
    /**
     * ThinkAndroid 应用程序运行Activity管理器
     */
    private TAAppManager mAppManager;
    private Boolean networkAvailable = false;
    private static final String SYSTEMCACHE = "thinkandroid";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        onPreCreateApplication();
        super.onCreate();
        doOncreate();
        onAfterCreateApplication();
        getAppManager();
    }

    private void doOncreate() {
        // TODO Auto-generated method stub
        this.application = this;
        // 注册App异常崩溃处理器
//        Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler());
        mCommandExecutor = TACommandExecutor.getInstance();
    }

    /**
     * 获取Application
     *
     * @return
     */
    public static TAApplication getApplication() {
        return application;
    }

    protected void onAfterCreateApplication() {
        // TODO Auto-generated method stub

    }

    protected void onPreCreateApplication() {
        // TODO Auto-generated method stub

    }

    public TAIConfig getPreferenceConfig() {
        return getConfig(PREFERENCECONFIG);
    }

    public TAIConfig getPropertiesConfig() {
        return getConfig(PROPERTIESCONFIG);
    }

    public TAIConfig getConfig(int confingType) {
        if (confingType == PREFERENCECONFIG) {
            mCurrentConfig = TAPreferenceConfig.getPreferenceConfig(this);

        } else if (confingType == PROPERTIESCONFIG) {
            mCurrentConfig = TAPropertiesConfig.getPropertiesConfig(this);
        } else {
            mCurrentConfig = TAPropertiesConfig.getPropertiesConfig(this);
        }
        if (!mCurrentConfig.isLoadConfig()) {
            mCurrentConfig.loadConfig();
        }
        return mCurrentConfig;
    }

    public TAIConfig getCurrentConfig() {
        if (mCurrentConfig == null) {
            getPreferenceConfig();
        }
        return mCurrentConfig;
    }

    /**
     * 设置 App异常崩溃处理器
     *
     * @param uncaughtExceptionHandler
     */
    public void setUncaughtExceptionHandler(
            UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    private UncaughtExceptionHandler getUncaughtExceptionHandler() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = TAAppException.getInstance(this);
        }
        return uncaughtExceptionHandler;
    }

    public void doCommand(String commandKey, TARequest request,
                          TAIResponseListener listener) {
        if (listener != null) {
            try {
                TACommandExecutor.getInstance().enqueueCommand(commandKey,
                        request, listener);

            } catch (TANoSuchCommandException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void registerCommand(int resID, Class<? extends TAICommand> command) {

        String commandKey = getString(resID);
        registerCommand(commandKey, command);

    }

    public void registerCommand(String commandKey,
                                Class<? extends TAICommand> command) {
        if (command != null) {
            mCommandExecutor.registerCommand(commandKey, command);
        }
    }

    public void unregisterCommand(int resID) {
        String commandKey = getString(resID);
        unregisterCommand(commandKey);
    }

    public void unregisterCommand(String commandKey) {

        mCommandExecutor.unregisterCommand(commandKey);
    }

    public TAFileCache getFileCache() {
        if (mFileCache == null) {
            TACacheParams cacheParams = new TACacheParams(this, SYSTEMCACHE);
            TAFileCache fileCache = new TAFileCache(cacheParams);
            application.setFileCache(fileCache);

        }
        return mFileCache;
    }

    public void setFileCache(TAFileCache fileCache) {
        this.mFileCache = fileCache;
    }

    public TAAppManager getAppManager() {
        if (mAppManager == null) {
            mAppManager = TAAppManager.getAppManager();
        }
        return mAppManager;
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行,如果为true则为后台运行
     */
    public void exitApp(Boolean isBackground) {
        mAppManager.AppExit(this, isBackground);
    }

    /**
     * 获取当前网络状态，true为网络连接成功，否则网络连接失败
     *
     * @return
     */
    public Boolean isNetworkAvailable() {
        return networkAvailable;
    }

}
