package com.dashihui.afford.util.map;

import com.dashihui.afford.util.object.UtilObject;
import com.dashihui.afford.util.string.UtilString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Map Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-7-22
 */
public class UtilMap {

	/** default separator between key and value **/
	public static final String DEFAULT_KEY_AND_VALUE_SEPARATOR = ":";
	/** default separator between key-value pairs **/
	public static final String DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR = ",";

	/**
	 * “第一个参数”Map<String, Object>是否包含键值为“第二个参数”且不等于空字符串""
	 * 
	 * @Title: mapContainName
	 * @param mapData
	 * @param strValueName
	 * @return 设定文件
	 * @return Boolean 返回类型
	 * @author 牛丰产
	 * @date 2015年1月11日 下午6:58:38
	 * @维护人:
	 * @version V1.0
	 */
	public static Boolean mapObjectContainName(Map<String, Object> mapData, String strValueName) {
		return !isEmpty(mapData) && mapData.containsKey(strValueName)&&
				mapData.get(strValueName)!=null && !"".equals(mapData.get(strValueName).toString());
	}
	
	/**
	 * 
	* @Title: mapStringContainName 
	* @Description: 指定的String类型Map中是否含有指定的键值 
	* @param @param mapData
	* @param @param strValueName
	* @param @return    设定文件 
	* @return Boolean    返回类型 
	* @author 吕军伟
	* @date 2015年2月25日 下午2:14:22 
	* @评审人:
	* @维护人: 
	* @version V1.0
	 */
	public static Boolean mapStringContainName(Map<String, String> mapData, String strValueName) {
		return !isEmpty(mapData) && mapData.containsKey(strValueName)&&
				mapData.get(strValueName)!=null && !"".equals(mapData.get(strValueName).toString());
	}
	
	/**
	 * 
	* @Title: mapContainName 
	* @Description: 判断Map中是否含有指定的键值，且键值不为空字符串 
	* @param @param mapData
	* @param @param strValueName
	* @param @return    设定文件 
	* @return Boolean    返回类型 
	* @author 牛丰产 
	* @date 2015年1月15日 上午10:06:49  
	* @评审人:
	* @维护人: 
	* @version V1.0
	 */
	public static Boolean mapContainName(Map<String, String> mapData, String strValueName) {
		return !isEmpty(mapData) && mapData.containsKey(strValueName)&&
				mapData.get(strValueName)!=null && !"".equals(mapData.get(strValueName).toString());
	}
	

	/**
	 * is null or its size is 0
	 * 
	 * <pre>
	 * isEmpty(null)   =   true;
	 * isEmpty({})     =   true;
	 * isEmpty({1, 2})    =   false;
	 * </pre>
	 * 
	 * @param sourceMap
	 * @return if map is null or its size is 0, return true, else return false.
	 */
	public static <K, V> boolean isEmpty(Map<K, V> sourceMap) {
		return (sourceMap == null || sourceMap.size() == 0);
	}

	/**
	 * add key-value pair to map, and key need not null or empty
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return <ul>
	 *         <li>if map is null, return false</li>
	 *         <li>if key is null or empty, return false</li>
	 *         <li>return {@link Map#put(Object, Object)}</li>
	 *         </ul>
	 */
	public static boolean putMapNotEmptyKey(Map<String, String> map, String key, String value) {
		if (map == null || UtilString.isEmpty(key)) {
			return false;
		}

		map.put(key, value);
		return true;
	}

	/**
	 * add key-value pair to map, and key need not null or empty
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return <ul>
	 *         <li>if map is null, return false</li>
	 *         <li>if key is null or empty, return false</li>
	 *         <li>return {@link Map#put(Object, Object)}</li>
	 *         </ul>
	 */
	public static boolean putMapNotEmptyKeyObject(Map<String, Object> map, String key, String value) {
		if (map == null || UtilString.isEmpty(key)) {
			return false;
		}

		map.put(key, value);
		return true;
	}

	/**
	 * add key-value pair to map, both key and value need not null or empty
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return <ul>
	 *         <li>if map is null, return false</li>
	 *         <li>if key is null or empty, return false</li>
	 *         <li>if value is null or empty, return false</li>
	 *         <li>return {@link Map#put(Object, Object)}</li>
	 *         </ul>
	 */
	public static boolean putMapNotEmptyKeyAndValue(Map<String, String> map, String key, String value) {
		if (map == null || UtilString.isEmpty(key) || UtilString.isEmpty(value)) {
			return false;
		}

		map.put(key, value);
		return true;
	}

	/**
	 * add key-value pair to map, key need not null or empty
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if map is null, return false</li>
	 *         <li>if key is null or empty, return false</li>
	 *         <li>if value is null or empty, put defaultValue, return true</li>
	 *         <li>if value is neither null nor empty，put value, return true</li>
	 *         </ul>
	 */
	public static boolean putMapNotEmptyKeyAndValue(Map<String, String> map, String key, String value, String defaultValue) {
		if (map == null || UtilString.isEmpty(key)) {
			return false;
		}

		map.put(key, UtilString.isEmpty(value) ? defaultValue : value);
		return true;
	}

	/**
	 * add key-value pair to map, key need not null
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return <ul>
	 *         <li>if map is null, return false</li>
	 *         <li>if key is null, return false</li>
	 *         <li>return {@link Map#put(Object, Object)}</li>
	 *         </ul>
	 */
	public static <K, V> boolean putMapNotNullKey(Map<K, V> map, K key, V value) {
		if (map == null || key == null) {
			return false;
		}

		map.put(key, value);
		return true;
	}

	/**
	 * add key-value pair to map, both key and value need not null
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return <ul>
	 *         <li>if map is null, return false</li>
	 *         <li>if key is null, return false</li>
	 *         <li>if value is null, return false</li>
	 *         <li>return {@link Map#put(Object, Object)}</li>
	 *         </ul>
	 */
	public static <K, V> boolean putMapNotNullKeyAndValue(Map<K, V> map, K key, V value) {
		if (map == null || key == null || value == null) {
			return false;
		}

		map.put(key, value);
		return true;
	}

	/**
	 * get key by value, match the first entry front to back
	 * <ul>
	 * <strong>Attentions:</strong>
	 * <li>for HashMap, the order of entry not same to put order, so you may
	 * need to use TreeMap</li>
	 * </ul>
	 * 
	 * @param <V>
	 * @param map
	 * @param value
	 * @return <ul>
	 *         <li>if map is null, return null</li>
	 *         <li>if value exist, return key</li>
	 *         <li>return null</li>
	 *         </ul>
	 */
	public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
		if (isEmpty(map)) {
			return null;
		}

		for (Entry<K, V> entry : map.entrySet()) {
			if (UtilObject.isEquals(entry.getValue(), value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * 过滤空键值 parse key-value pairs to map, ignore empty key
	 * 
	 * <pre>
	 * parseKeyAndValueToMap("","","",true)=null
	 * parseKeyAndValueToMap(null,"","",true)=null
	 * parseKeyAndValueToMap("a:b,:","","",true)={(a,b)}
	 * parseKeyAndValueToMap("a:b,:d","","",true)={(a,b)}
	 * parseKeyAndValueToMap("a:b,c:d","","",true)={(a,b),(c,d)}
	 * parseKeyAndValueToMap("a=b, c = d","=",",",true)={(a,b),(c,d)}
	 * parseKeyAndValueToMap("a=b, c = d","=",",",false)={(a, b),( c , d)}
	 * parseKeyAndValueToMap("a=b, c=d","=", ",", false)={(a,b),( c,d)}
	 * parseKeyAndValueToMap("a=b; c=d","=", ";", false)={(a,b),( c,d)}
	 * parseKeyAndValueToMap("a=b, c=d", ",", ";", false)={(a=b, c=d)}
	 * </pre>
	 * 
	 * @param source
	 *            key-value pairs
	 * @param keyAndValueSeparator
	 *            separator between key and value
	 * @param keyAndValuePairSeparator
	 *            separator between key-value pairs
	 * @param ignoreSpace
	 *            whether ignore space at the begging or end of key and value
	 * @return
	 */
	public static Map<String, String> parseKeyAndValueToMap(String source,
															String keyAndValueSeparator,
															String keyAndValuePairSeparator,
															boolean ignoreSpace) {
		if (UtilString.isEmpty(source)) {
			return null;
		}

		if (UtilString.isEmpty(keyAndValueSeparator)) {
			keyAndValueSeparator = DEFAULT_KEY_AND_VALUE_SEPARATOR;
		}
		if (UtilString.isEmpty(keyAndValuePairSeparator)) {
			keyAndValuePairSeparator = DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR;
		}
		Map<String, String> keyAndValueMap = new HashMap<String, String>();
		String[] keyAndValueArray = source.split(keyAndValuePairSeparator);
		if (keyAndValueArray == null) {
			return null;
		}

		int seperator;
		for (String valueEntity : keyAndValueArray) {
			if (!UtilString.isEmpty(valueEntity)) {
				seperator = valueEntity.indexOf(keyAndValueSeparator);
				if (seperator != -1) {
					if (ignoreSpace) {
						UtilMap.putMapNotEmptyKey(keyAndValueMap, valueEntity.substring(0, seperator).trim(), valueEntity.substring(seperator + 1)
																															.trim());
					} else {
						UtilMap.putMapNotEmptyKey(keyAndValueMap, valueEntity.substring(0, seperator), valueEntity.substring(seperator + 1));
					}
				}
			}
		}
		return keyAndValueMap;
	}

	/**
	 * parse key-value pairs to map, ignore empty key
	 * 
	 * @param source
	 *            key-value pairs
	 * @param ignoreSpace
	 *            whether ignore space at the begging or end of key and value
	 * @return
	 * @see {@link UtilMap#parseKeyAndValueToMap(String, String, String, boolean)}
	 *      , keyAndValueSeparator is {@link #DEFAULT_KEY_AND_VALUE_SEPARATOR},
	 *      keyAndValuePairSeparator is
	 *      {@link #DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR}
	 */
	public static Map<String, String> parseKeyAndValueToMap(String source, boolean ignoreSpace) {
		return parseKeyAndValueToMap(source, DEFAULT_KEY_AND_VALUE_SEPARATOR, DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR, ignoreSpace);
	}

	/**
	 * parse key-value pairs to map, ignore empty key, ignore space at the
	 * begging or end of key and value
	 * 
	 * @param source
	 *            key-value pairs
	 * @return
	 * @see {@link UtilMap#parseKeyAndValueToMap(String, String, String, boolean)}
	 *      , keyAndValueSeparator is {@link #DEFAULT_KEY_AND_VALUE_SEPARATOR},
	 *      keyAndValuePairSeparator is
	 *      {@link #DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR}, ignoreSpace is true
	 */
	public static Map<String, String> parseKeyAndValueToMap(String source) {
		return parseKeyAndValueToMap(source, DEFAULT_KEY_AND_VALUE_SEPARATOR, DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR, true);
	}

	/**
	 * 把Map转换为JSON join map
	 * 
	 * @param map
	 * @return
	 */
	public static String toJson(Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return null;
		}

		StringBuilder paras = new StringBuilder();
		paras.append("{");
		Iterator<Entry<String, String>> ite = map.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) ite.next();
			paras.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
			if (ite.hasNext()) {
				paras.append(",");
			}
		}
		paras.append("}");
		return paras.toString();
	}
}
