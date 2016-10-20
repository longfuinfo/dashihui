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
package com.ta.util.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @Title TAPreferenceConfig
 * @Package com.ta.util.config
 * @Description TAPreferenceConfig是ThinkAndroid的Preference类型配置文件操作类
 * @author 白猫
 * @date 2013-1-7
 * @version V1.0
 */
public class TAPreferenceConfig implements TAIConfig
{
	private static TAIConfig mPreferenceConfig;
	private Context mContext;
	private Editor edit = null;
	private SharedPreferences mSharedPreferences;
	private String filename = "thinkandroid";
	private Boolean isLoad = false;

	private TAPreferenceConfig(Context context)
	{
		this.mContext = context;

	}

	/**
	 * 获得系统资源类
	 * 
	 * @param context
	 * @return
	 */
	public static TAIConfig getPreferenceConfig(Context context)
	{
		if (mPreferenceConfig == null)
		{
			mPreferenceConfig = new TAPreferenceConfig(context);
		}
		return mPreferenceConfig;
	}

	@Override
	public void loadConfig()
	{
		// TODO Auto-generated method stub
		try
		{
			mSharedPreferences = mContext.getSharedPreferences(filename,
//					Context.MODE_WORLD_WRITEABLE);
					Context.MODE_PRIVATE);
			edit = mSharedPreferences.edit();
			isLoad = true;
		} catch (Exception e)
		{
			// TODO: handle exception
			isLoad = false;
		}

	}

	@Override
	public Boolean isLoadConfig()
	{
		// TODO Auto-generated method stub
		return isLoad;
	}

	@Override
	public void close()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isClosed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setString(String key, String value)
	{
		// TODO Auto-generated method stub
		edit.putString(key, value);
		edit.commit();
	}

	@Override
	public void setInt(String key, int value)
	{
		// TODO Auto-generated method stub
		edit.putInt(key, value);
		edit.commit();
	}

	@Override
	public void setBoolean(String key, Boolean value)
	{
		// TODO Auto-generated method stub
		edit.putBoolean(key, value);
		edit.commit();
	}

	@Override
	public void setByte(String key, byte[] value)
	{
		// TODO Auto-generated method stub
		setString(key, String.valueOf(value));
	}

	@Override
	public void setShort(String key, short value)
	{
		// TODO Auto-generated method stub
		setString(key, String.valueOf(value));
	}

	@Override
	public void setLong(String key, long value)
	{
		// TODO Auto-generated method stub
		edit.putLong(key, value);
		edit.commit();
	}

	@Override
	public void setFloat(String key, float value)
	{
		// TODO Auto-generated method stub
		edit.putFloat(key, value);
		edit.commit();
	}

	@Override
	public void setDouble(String key, double value)
	{
		// TODO Auto-generated method stub
		setString(key, String.valueOf(value));
	}

	@Override
	public void setString(int resID, String value)
	{
		// TODO Auto-generated method stub
		setString(this.mContext.getString(resID), value);

	}

	@Override
	public void setInt(int resID, int value)
	{
		// TODO Auto-generated method stub
		setInt(this.mContext.getString(resID), value);
	}

	@Override
	public void setBoolean(int resID, Boolean value)
	{
		// TODO Auto-generated method stub
		setBoolean(this.mContext.getString(resID), value);
	}

	@Override
	public void setByte(int resID, byte[] value)
	{
		// TODO Auto-generated method stub
		setByte(this.mContext.getString(resID), value);
	}

	@Override
	public void setShort(int resID, short value)
	{
		// TODO Auto-generated method stub
		setShort(this.mContext.getString(resID), value);
	}

	@Override
	public void setLong(int resID, long value)
	{
		// TODO Auto-generated method stub
		setLong(this.mContext.getString(resID), value);
	}

	@Override
	public void setFloat(int resID, float value)
	{
		// TODO Auto-generated method stub
		setFloat(this.mContext.getString(resID), value);
	}

	@Override
	public void setDouble(int resID, double value)
	{
		// TODO Auto-generated method stub
		setDouble(this.mContext.getString(resID), value);
	}

	@Override
	public String getString(String key, String defaultValue)
	{
		// TODO Auto-generated method stub
		return mSharedPreferences.getString(key, defaultValue);
	}

	@Override
	public int getInt(String key, int defaultValue)
	{
		// TODO Auto-generated method stub
		return mSharedPreferences.getInt(key, defaultValue);
	}

	@Override
	public boolean getBoolean(String key, Boolean defaultValue)
	{
		// TODO Auto-generated method stub
		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	@Override
	public byte[] getByte(String key, byte[] defaultValue)
	{
		// TODO Auto-generated method stub
		try
		{
			return getString(key, "").getBytes();
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return defaultValue;
	}

	@Override
	public short getShort(String key, Short defaultValue)
	{
		// TODO Auto-generated method stub
		try
		{
			return Short.valueOf(getString(key, ""));
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return defaultValue;
	}

	@Override
	public long getLong(String key, Long defaultValue)
	{
		// TODO Auto-generated method stub
		return mSharedPreferences.getLong(key, defaultValue);
	}

	@Override
	public float getFloat(String key, Float defaultValue)
	{
		// TODO Auto-generated method stub
		return mSharedPreferences.getFloat(key, defaultValue);
	}

	@Override
	public double getDouble(String key, Double defaultValue)
	{
		// TODO Auto-generated method stub
		try
		{
			return Double.valueOf(getString(key, ""));
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return defaultValue;
	}

	@Override
	public String getString(int resID, String defaultValue)
	{
		// TODO Auto-generated method stub
		return getString(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public int getInt(int resID, int defaultValue)
	{
		// TODO Auto-generated method stub
		return getInt(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public boolean getBoolean(int resID, Boolean defaultValue)
	{
		// TODO Auto-generated method stub
		return getBoolean(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public byte[] getByte(int resID, byte[] defaultValue)
	{
		// TODO Auto-generated method stub
		return getByte(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public short getShort(int resID, Short defaultValue)
	{
		// TODO Auto-generated method stub
		return getShort(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public long getLong(int resID, Long defaultValue)
	{
		// TODO Auto-generated method stub
		return getLong(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public float getFloat(int resID, Float defaultValue)
	{
		// TODO Auto-generated method stub
		return getFloat(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public double getDouble(int resID, Double defaultValue)
	{
		// TODO Auto-generated method stub
		return getDouble(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public void remove(String key)
	{
		// TODO Auto-generated method stub
		edit.remove(key);
		edit.commit();
	}

	@Override
	public void remove(String... keys)
	{
		// TODO Auto-generated method stub
		for (String key : keys)
			remove(key);
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
		edit.clear();
		edit.commit();
	}

	@Override
	public void open()
	{
		// TODO Auto-generated method stub

	}

}
