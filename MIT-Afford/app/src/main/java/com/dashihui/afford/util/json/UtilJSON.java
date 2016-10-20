package com.dashihui.afford.util.json;


import com.dashihui.afford.util.map.UtilMap;
import com.dashihui.afford.util.string.UtilString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Json Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class UtilJSON {

	public static boolean isPrintException = true;

	/**
	 * get Long from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link JSONObject#getLong(String)} exception, return
	 *         defaultValue</li>
	 *         <li>return {@link JSONObject#getLong(String)}</li>
	 *         </ul>
	 */
	public static Long getLong(JSONObject jsonObject, String key, Long defaultValue) {
		if (jsonObject == null || UtilString.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getLong(key);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Long from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return
	 *         {@link UtilJSON#getLong(String, String, Long)}</li>
	 *         </ul>
	 */
	public static Long getLong(String jsonData, String key, Long defaultValue) {
		if (UtilString.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getLong(jsonObject, key, defaultValue);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see UtilJSON#getLong(JSONObject, String, Long)
	 */
	public static long getLong(JSONObject jsonObject, String key, long defaultValue) {
		return getLong(jsonObject, key, (Long) defaultValue);
	}

	/**
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see UtilJSON#getLong(String, String, Long)
	 */
	public static long getLong(String jsonData, String key, long defaultValue) {
		return getLong(jsonData, key, (Long) defaultValue);
	}

	/**
	 * get Int from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link JSONObject#getInt(String)} exception, return
	 *         defaultValue</li>
	 *         <li>return {@link JSONObject#getInt(String)}</li>
	 *         </ul>
	 */
	public static Integer getInt(JSONObject jsonObject, String key, Integer defaultValue) {
		if (jsonObject == null || UtilString.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getInt(key);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Int from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return
	 *         {@link UtilJSON#getInt(JSONObject, String, int)} </li>
	 *         </ul>
	 */
	public static Integer getInt(String jsonData, String key, Integer defaultValue) {
		if (UtilString.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getInt(jsonObject, key, defaultValue);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see UtilJSON#getInt(JSONObject, String, Integer)
	 */
	public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
		return getInt(jsonObject, key, (Integer) defaultValue);
	}

	/**
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see UtilJSON#getInt(String, String, Integer)
	 */
	public static int getInt(String jsonData, String key, int defaultValue) {
		return getInt(jsonData, key, (Integer) defaultValue);
	}

	/**
	 * get Double from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link JSONObject#getDouble(String)} exception, return
	 *         defaultValue</li>
	 *         <li>return {@link JSONObject#getDouble(String)}</li>
	 *         </ul>
	 */
	public static Double getDouble(JSONObject jsonObject, String key, Double defaultValue) {
		if (jsonObject == null || UtilString.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getDouble(key);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Double from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return
	 *         {@link UtilJSON#getDouble(String, String, Double)}</li>
	 *         </ul>
	 */
	public static Double getDouble(String jsonData, String key, Double defaultValue) {
		if (UtilString.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getDouble(jsonObject, key, defaultValue);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see UtilJSON#getDouble(JSONObject, String, Double)
	 */
	public static double getDouble(JSONObject jsonObject, String key, double defaultValue) {
		return getDouble(jsonObject, key, (Double) defaultValue);
	}

	/**
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see UtilJSON#getDouble(String, String, Double)
	 */
	public static double getDouble(String jsonData, String key, double defaultValue) {
		return getDouble(jsonData, key, (Double) defaultValue);
	}

	/**
	 * get String from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link JSONObject#getString(String)} exception, return
	 *         defaultValue</li>
	 *         <li>return {@link JSONObject#getString(String)}</li>
	 *         </ul>
	 */
	public static String getString(JSONObject jsonObject, String key, String defaultValue) {
		if (jsonObject == null || UtilString.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getString(key);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get String from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return
	 *         {@link UtilJSON#getString(String, String, String)} </li>
	 *         </ul>
	 */
	public static String getString(String jsonData, String key, String defaultValue) {
		if (UtilString.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getString(jsonObject, key, defaultValue);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get String array from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link JSONObject#getJSONArray(String)} exception, return
	 *         defaultValue</li>
	 *         <li>if {@link JSONArray#getString(int)} exception, return
	 *         defaultValue</li>
	 *         <li>return string array</li>
	 *         </ul>
	 */
	public static String[] getStringArray(JSONObject jsonObject, String key, String[] defaultValue) {
		if (jsonObject == null || UtilString.isEmpty(key)) {
			return defaultValue;
		}

		try {
			JSONArray statusArray = jsonObject.getJSONArray(key);
			if (statusArray != null) {
				String[] value = new String[statusArray.length()];
				for (int i = 0; i < statusArray.length(); i++) {
					value[i] = statusArray.getString(i);
				}
				return value;
			}
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
		return defaultValue;
	}

	/**
	 * get String array from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return
	 *         {@link UtilJSON#getStringArray(String, String, String[])} </li>
	 *         </ul>
	 */
	public static String[] getStringArray(String jsonData, String key, String[] defaultValue) {
		if (UtilString.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getStringArray(jsonObject, key, defaultValue);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get JSONObject from jsonObject
	 * 
	 * @param jsonObject
	 *            <em><em></em></em>
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link JSONObject#getJSONObject(String)} exception, return
	 *         defaultValue</li>
	 *         <li>return {@link JSONObject#getJSONObject(String)}</li>
	 *         </ul>
	 */
	public static JSONObject getJSONObject(JSONObject jsonObject, String key, JSONObject defaultValue) {
		if (jsonObject == null || UtilString.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getJSONObject(key);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get JSONObject from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return
	 *         {@link UtilJSON#getJSONObject(JSONObject, String, JSONObject)}</li>
	 *         </ul>
	 */
	public static JSONObject getJSONObject(String jsonData, String key, JSONObject defaultValue) {
		if (UtilString.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getJSONObject(jsonObject, key, defaultValue);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get JSONArray from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link JSONObject#getJSONArray(String)} exception, return
	 *         defaultValue</li>
	 *         <li>return {@link JSONObject#getJSONArray(String)}</li>
	 *         </ul>
	 */
	public static JSONArray getJSONArray(JSONObject jsonObject, String key, JSONArray defaultValue) {
		if (jsonObject == null || UtilString.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getJSONArray(key);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get JSONArray from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return
	 *         {@link UtilJSON#getJSONArray(String, String, JSONArray)}</li>
	 *         </ul>
	 */
	public static JSONArray getJSONArray(String jsonData, String key, JSONArray defaultValue) {
		if (UtilString.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getJSONArray(jsonObject, key, defaultValue);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Boolean from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>return {@link JSONObject#getBoolean(String)}</li>
	 *         </ul>
	 */
	public static boolean getBoolean(JSONObject jsonObject, String key, Boolean defaultValue) {
		if (jsonObject == null || UtilString.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getBoolean(key);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Boolean from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return
	 *         {@link UtilJSON#getBoolean(JSONObject, String, Boolean)}</li>
	 *         </ul>
	 */
	public static boolean getBoolean(String jsonData, String key, Boolean defaultValue) {
		if (UtilString.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getBoolean(jsonObject, key, defaultValue);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get map from jsonObject.
	 * 
	 * @param jsonObject
	 *            key-value pairs json
	 * @param key
	 * @return <ul>
	 *         <li>if jsonObject is null, return null</li>
	 *         <li>return {@link UtilJSON#parseKeyAndValueToMap(String)}</li>
	 *         </ul>
	 */
	public static Map<String, String> getMap(JSONObject jsonObject, String key) {
		return UtilJSON.parseKeyAndValueToMap(UtilJSON.getString(jsonObject, key, null));
	}

	/**
	 * get map from jsonData.
	 * 
	 * @param jsonData
	 *            key-value pairs string
	 * @param key
	 * @return <ul>
	 *         <li>if jsonData is null, return null</li>
	 *         <li>if jsonData length is 0, return empty map</li>
	 *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
	 *         return null</li>
	 *         <li>return {@link UtilJSON#getMap(JSONObject, String)}</li>
	 *         </ul>
	 */
	public static Map<String, String> getMap(String jsonData, String key) {

		if (jsonData == null) {
			return null;
		}
		if (jsonData.length() == 0) {
			return new HashMap<String, String>();
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getMap(jsonObject, key);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * parse key-value pairs to map. ignore empty key, if getValue exception,
	 * put empty value
	 * 
	 * @param sourceObj
	 *            key-value pairs json
	 * @return <ul>
	 *         <li>if sourceObj is null, return null</li>
	 *         <li>else parse entry by
	 *         one by
	 *         one</li>
	 *         </ul>
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) {
		if (sourceObj == null) {
			return null;
		}

		Map<String, String> keyAndValueMap = new HashMap<String, String>();
		for (Iterator iter = sourceObj.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			UtilMap.putMapNotEmptyKey(keyAndValueMap, key, getString(sourceObj, key, ""));

		}
		return keyAndValueMap;
	}

	public static Map<String, Object> parseKeyStrAndValueObjectToMap(JSONObject sourceObj) {
		if (sourceObj == null) {
			return null;
		}

		Map<String, Object> keyAndValueMap = new HashMap<String, Object>();
		for (Iterator iter = sourceObj.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			UtilMap.putMapNotEmptyKeyObject(keyAndValueMap, key, getString(sourceObj, key, ""));

		}
		return keyAndValueMap;
	}

	/**
	 * parse key-value pairs to map. ignore empty key, if getValue exception,
	 * put empty value
	 * 
	 * @param source
	 *            key-value pairs json
	 * @return <ul>
	 *         <li>if source is null or source's length is 0, return empty map</li>
	 *         <li>if source {@link JSONObject#JSONObject(String)} exception,
	 *         return null</li>
	 *         <li>return {@link UtilJSON#parseKeyAndValueToMap(JSONObject)}</li>
	 *         </ul>
	 */
	public static Map<String, String> parseKeyAndValueToMap(String source) {
		if (UtilString.isEmpty(source)) {
			return null;
		}

		try {
			JSONObject jsonObject = new JSONObject(source);
			return parseKeyAndValueToMap(jsonObject);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * parse key-value pairs to map. ignore empty key, if getValue exception,
	 * put empty value
	 * 
	 * @param source
	 *            key-value pairs json
	 * @return <ul>
	 *         <li>if source is null or source's length is 0, return empty map</li>
	 *         <li>if source {@link JSONObject#JSONObject(String)} exception,
	 *         return null</li>
	 *         <li>return {@link UtilJSON#parseKeyAndValueToMap(JSONObject)}</li>
	 *         </ul>
	 */
	public static Map<String, Object> parseKeyStrAndValueObjectToMap(String source) {
		if (UtilString.isEmpty(source)) {
			return null;
		}

		try {
			JSONObject jsonObject = new JSONObject(source);
			return parseKeyStrAndValueObjectToMap(jsonObject);
		}
		catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return null;
		}
	}

	
	/** 
	* 格式化json字符串，现在用于logcat输出request的json时格式化方便查看使用
	* @Title: formatJson 
	* @param @param json
	* @param @param fillStringUnit
	* @param @return    设定文件 
	* @return String    返回类型 
	* @author 张博
	* @date 2015-1-21 上午9:48:16 
	*/ 
	public static String formatJson(String json, String fillStringUnit) {
		if (json == null || json.trim().length() == 0) {
			return "result json null";
		}

		int fixedLenth = 0;
		ArrayList<String> tokenList = new ArrayList<String>();
		{
			String jsonTemp = json;
			// 预读取
			while (jsonTemp.length() > 0) {
				String token = getToken(jsonTemp);
				jsonTemp = jsonTemp.substring(token.length());
				token = token.trim();
				tokenList.add(token);
			}
		}

		for (int i = 0; i < tokenList.size(); i++) {
			String token = tokenList.get(i);
			int length = token.getBytes().length;
			if (length > fixedLenth && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
				fixedLenth = length;
			}
		}

		StringBuilder buf = new StringBuilder();
		int count = 0;
		for (int i = 0; i < tokenList.size(); i++) {

			String token = tokenList.get(i);

			if (token.equals(",")) {
				buf.append(token);
				doFill(buf, count, fillStringUnit);
				continue;
			}
			if (token.equals(":")) {
				buf.append(" ").append(token).append(" ");
				continue;
			}
			if (token.equals("{")) {
				String nextToken = tokenList.get(i + 1);
				if (nextToken.equals("}")) {
					i++;
					buf.append("{ }");
				} else {
					count++;
					buf.append(token);
					doFill(buf, count, fillStringUnit);
				}
				continue;
			}
			if (token.equals("}")) {
				count--;
				doFill(buf, count, fillStringUnit);
				buf.append(token);
				continue;
			}
			if (token.equals("[")) {
				String nextToken = tokenList.get(i + 1);
				if (nextToken.equals("]")) {
					i++;
					buf.append("[ ]");
				} else {
					count++;
					buf.append(token);
					doFill(buf, count, fillStringUnit);
				}
				continue;
			}
			if (token.equals("]")) {
				count--;
				doFill(buf, count, fillStringUnit);
				buf.append(token);
				continue;
			}

			if (token.length() > 100) {
				token = token.substring(0, 100);
			}
			buf.append(token);
			// 左对齐
			if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
				int fillLength = fixedLenth - token.getBytes().length;
				if (fillLength > 0) {
					for (int j = 0; j < fillLength; j++) {
						buf.append(" ");
					}
				}
			}
		}
		return buf.toString();
	}

	private static String getToken(String json) {
		StringBuilder buf = new StringBuilder();
		boolean isInYinHao = false;
		while (json.length() > 0) {
			String token = json.substring(0, 1);
			json = json.substring(1);

			if (!isInYinHao
				&& (token.equals(":") || token.equals("{") || token.equals("}") || token.equals("[") || token.equals("]") || token.equals(","))) {
				if (buf.toString().trim().length() == 0) {
					buf.append(token);
				}

				break;
			}

			if (token.equals("\\")) {
				buf.append(token);
				buf.append(json.substring(0, 1));
				json = json.substring(1);
				continue;
			}
			if (token.equals("\"")) {
				buf.append(token);
				if (isInYinHao) {
					break;
				} else {
					isInYinHao = true;
					continue;
				}
			}
			buf.append(token);
		}
		return buf.toString();
	}

	private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
		buf.append("\n");
		for (int i = 0; i < count; i++) {
			buf.append(fillStringUnit);
		}
	}
}
