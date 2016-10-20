package com.dashihui.afford.util.array;


import com.dashihui.afford.util.object.UtilObject;

/**
 * 数组工具类 Array Utils
 * <ul>
 * <li>{@link #isEmpty(Object[])} is null or its length is 0</li>
 * <li>{@link #getLast(Object[], Object, Object, boolean)} get last element of
 * the target element, before the first one that match the target element front
 * to back</li>
 * <li>{@link #getNext(Object[], Object, Object, boolean)} get next element of
 * the target element, after the first one that match the target element front
 * to back</li>
 * <li>{@link #getLast(Object[], Object, boolean)}</li>
 * <li>{@link #getLast(int[], int, int, boolean)}</li>
 * <li>{@link #getLast(long[], long, long, boolean)}</li>
 * <li>{@link #getNext(Object[], Object, boolean)}</li>
 * <li>{@link #getNext(int[], int, int, boolean)}</li>
 * <li>{@link #getNext(long[], long, long, boolean)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-10-24
 */
public class UtilArray {

	/**
	 * 是null或个数为0时返回true is null or its length is 0
	 * 
	 * @param <V>
	 * @param sourceArray
	 * @return
	 */
	public static <V> boolean isEmpty(V[] sourceArray) {
		return (sourceArray == null || sourceArray.length == 0);
	}

	/**
	 * 得到最后一个元素 get last element of the target element, before the first one
	 * that match the target element front to back
	 * <ul>
	 * *
	 * <li>如果数组是空，返回默认值</li>
	 * <li>if array is empty, return defaultValue</li>
	 * <li>如果目标元素不在数组中，则返回默认值</li>
	 * <li>if target element is not exist in array, return defaultValue</li>
	 * <li>如果目标元素在数组中，且不是数组第一个元素，则返回最后一个元素</li>
	 * <li>if target element exist in array and its index is not 0, return the
	 * last element</li>
	 * <li>如果目标元素在数组中且是数组中的第一个元素，如果参数isCircle等于true，且返回最后一个元素，否则返回默认值</li>
	 * <li>if target element exist in array and its index is 0, return the last
	 * one in array if isCircle is true, else return defaultValue</li>
	 * </ul>
	 * 
	 * @param <V>
	 * @param sourceArray
	 * @param value
	 *            value of target element
	 * @param defaultValue
	 *            default return value
	 * @param isCircle
	 *            whether is circle
	 * @return
	 */
	public static <V> V getLast(V[] sourceArray, V value, V defaultValue, boolean isCircle) {
		if (isEmpty(sourceArray)) {
			return defaultValue;
		}

		int currentPosition = -1;
		for (int i = 0; i < sourceArray.length; i++) {
			if (UtilObject.isEquals(value, sourceArray[i])) {
				currentPosition = i;
				break;
			}
		}
		if (currentPosition == -1) {
			return defaultValue;
		}

		if (currentPosition == 0) {
			return isCircle ? sourceArray[sourceArray.length - 1] : defaultValue;
		}
		return sourceArray[currentPosition - 1];
	}

	/**
	 * 得到下一个元素 get next element of the target element, after the first one that
	 * match the target element front to back
	 * <ul>
	 * <li>if array is empty, return defaultValue如果数组为空，返回默认值</li>
	 * <li>if target element is not exist in array, return
	 * defaultValue如果目标元素不在数组中，返回默认值</li>
	 * <li>if target element exist in array and not the last one in array,
	 * return the next element如果目标元素在数组中且不是数组的最后一个，返回下一个元素</li>
	 * <li>if target element exist in array and the last one in array, return
	 * the first one in array if isCircle is true, else return defaultValue</li>
	 * </ul>
	 * 
	 * @param <V>
	 * @param sourceArray
	 * @param value
	 *            value of target element
	 * @param defaultValue
	 *            default return value
	 * @param isCircle
	 *            whether is circle
	 * @return
	 */
	public static <V> V getNext(V[] sourceArray, V value, V defaultValue, boolean isCircle) {
		if (isEmpty(sourceArray)) {
			return defaultValue;
		}

		int currentPosition = -1;
		for (int i = 0; i < sourceArray.length; i++) {
			if (UtilObject.isEquals(value, sourceArray[i])) {
				currentPosition = i;
				break;
			}
		}
		if (currentPosition == -1) {
			return defaultValue;
		}

		if (currentPosition == sourceArray.length - 1) {
			return isCircle ? sourceArray[0] : defaultValue;
		}
		return sourceArray[currentPosition + 1];
	}

	/**
	 * @see {@link UtilArray#getLast(Object[], Object, Object, boolean)}
	 *      defaultValue is null
	 */
	public static <V> V getLast(V[] sourceArray, V value, boolean isCircle) {
		return getLast(sourceArray, value, null, isCircle);
	}

	/**
	 * @see {@link UtilArray#getNext(Object[], Object, Object, boolean)}
	 *      defaultValue is null
	 */
	public static <V> V getNext(V[] sourceArray, V value, boolean isCircle) {
		return getNext(sourceArray, value, null, isCircle);
	}

	/**
	 * @see {@link UtilArray#getLast(Object[], Object, Object, boolean)} Object
	 *      is Long
	 */
	public static long getLast(long[] sourceArray, long value, long defaultValue, boolean isCircle) {
		if (sourceArray.length == 0) {
			throw new IllegalArgumentException("The length of source array must be greater than 0.");
		}

		Long[] array = UtilObject.transformLongArray(sourceArray);
		return getLast(array, value, defaultValue, isCircle);

	}

	/**
	 * @see {@link UtilArray#getNext(Object[], Object, Object, boolean)} Object
	 *      is Long
	 */
	public static long getNext(long[] sourceArray, long value, long defaultValue, boolean isCircle) {
		if (sourceArray.length == 0) {
			throw new IllegalArgumentException("The length of source array must be greater than 0.");
		}

		Long[] array = UtilObject.transformLongArray(sourceArray);
		return getNext(array, value, defaultValue, isCircle);
	}

	/**
	 * @see {@link UtilArray#getLast(Object[], Object, Object, boolean)} Object
	 *      is Integer
	 */
	public static int getLast(int[] sourceArray, int value, int defaultValue, boolean isCircle) {
		if (sourceArray.length == 0) {
			throw new IllegalArgumentException("The length of source array must be greater than 0.");
		}

		Integer[] array = UtilObject.transformIntArray(sourceArray);
		return getLast(array, value, defaultValue, isCircle);

	}

	/**
	 * @see {@link UtilArray#getNext(Object[], Object, Object, boolean)} Object
	 *      is Integer
	 */
	public static int getNext(int[] sourceArray, int value, int defaultValue, boolean isCircle) {
		if (sourceArray.length == 0) {
			throw new IllegalArgumentException("The length of source array must be greater than 0.");
		}

		Integer[] array = UtilObject.transformIntArray(sourceArray);
		return getNext(array, value, defaultValue, isCircle);
	}
}
