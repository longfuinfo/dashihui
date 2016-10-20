package com.dashihui.afford.util.number;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: UtilNumber
 * @Description:
 * @author NiuFC
 * @date 2014年4月10日 上午10:22:09
 * @维护人:
 * @version V1.0
 * 
 */
public class UtilNumber {
	private static final String TAG = "UtilNumber";

	/**
	 * String转换为Integer TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @Title: IntegerValueOf
	 * @param str
	 * @return 设定文件
	 * @return Integer 返回类型
	 * @author 牛丰产
	 * @date 2015年1月7日 下午3:38:22
	 * @维护人:
	 * @version V1.0
	 */
	public static Integer IntegerValueOf(String str) {
		try {
			if (isNumeric(str)) {
				if (str.contains(".")) {
					return Integer.valueOf(str.substring(0, str.indexOf(".")));
				} else {
					return Integer.valueOf(str);
				}
			} else {
				return 0;
			}
		}
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * 
	 * @Title: DoubleValueOf 四舍五入
	 * @Description: 字符串转double
	 * @param @param str
	 * @param @return 设定文件
	 * @return Double 返回类型
	 * @author NiuFC
	 * @date 2015年1月17日 上午11:34:26
	 * @评审人:
	 * @维护人:
	 * @version V1.0
	 */
	public static Double DoubleValueOf(String str) {
		if (isNumeric(str)) {
			return UtilArith.round(Double.valueOf(str),2);
		} else {
			return 0d;
		}

	}

	/**
	 *
	 * @param str
	 * @return
	 */
	public static Float FloatValueOf(String str) {
		if (isNumeric(str)) {
			return Float.valueOf(str);
		} else {
			return 0f;
		}

	}



	


	/**
	 * @Description: 根据最大值和最小值得到最大涨跌幅值
	 * @param @param max
	 * @param @param min
	 * @param @return
	 * @return String
	 * @author NiuFC
	 * @date 2014年5月18日 下午1:28:52
	 * @维护人:
	 * @version V1.0
	 */
	public static String getMaxAndMinToZDF(double max, double min) {
		double middle = min + (max - min) / 2;
		double x = (max - min) / 2 / middle * 100;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setRoundingMode(RoundingMode.HALF_UP);// 设置四舍五入
		nf.setMinimumFractionDigits(2);// 设置最小保留几位小数
		nf.setMaximumFractionDigits(2);// 设置最大保留几位小数
		return nf.format(x);
	}

	/**
	 * 转换含有零的数字字符串
	 * 
	 * @Title: parseStrHasZero
	 * @Description: 转换含有零的数字字符串
	 * @param @param decimalStr 被转换的字符串（数字格式）
	 * @param @param parseType 转换类型（1：结果为0.00时返回“--”，2：结果为0.00时返回0）
	 * @param @return 转换后的结果
	 * @return String 返回类型
	 * @author NiuFC
	 * @date 2014年9月23日 上午11:30:21
	 * @维护人:
	 * @version V1.0
	 */
	public static String parseStrHasZero(String decimalStr, int parseType) {
		// 将数字字符串进行格式化，如果含有小数时保留两位
		String result = null;
		result = filterStrzero(decimalStr, -1);
		if (0 == UtilNumber.DoubleValueOf(result)) {
			switch (parseType) {
			case 1:
				result = "--";
				break;
			case 2:
				result = "0";
				break;
			}
			return result;
		} else {
			return result;
		}
		
	}
	
	public static String parseStrHasZero(String decimalStr,  boolean isAccurate) {
		// 将数字字符串进行格式化，如果含有小数时保留两位
		String result = null;
		result = filterStrzero(decimalStr, -1);
		if (0 == UtilNumber.DoubleValueOf(result)) {
			result = "--";
			return result;
		} else {
			if (isAccurate) {
				return UtilNumber.DoubleValueOf(result).toString();
			}else {
				return result;
			}
			
		}
		
	}
	


	/**
	 * 
	 * 过滤小数点后面的零
	 * 
	 * @author NiuFC
	 * @date 2013-10-8 下午5:21:16
	 * @version
	 * 
	 * @param decimalStr
	 * @return
	 */
	public static String filterStrzero(String decimalStr) {
		return filterStrzero(decimalStr, 2);
	}

	/**
	 * 返回数字的大数部分，即扔掉最后两位
	 * 
	 * @Title: getBigNumberStr
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param decimalStr
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @author NiuFC
	 * @date 2014年8月20日 上午11:48:31
	 * @维护人:
	 * @version V1.0
	 */
	public static String getBigNumberStr(String decimalStr) {
		String val = filterStrzero(decimalStr);
		if (!"--".equals(val)) {
			if (val.length() > 2) {
				if (val.endsWith(".0")) {
					val = val.substring(0, val.indexOf("."));
					if (val.length() > 2) {
						return val.substring(0, val.length() - 2);
					} else {
						return "";
					}
				} else {
					if (val.contains(".")) {
						return val.substring(0, val.indexOf("."));
					} else {
						return val.substring(0, val.length() - 2);
					}
				}
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * 获取高显数字
	 * 
	 * @Title: getPointNumberStr
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param decimalStr
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @author NiuFC
	 * @date 2014年8月20日 下午2:03:39
	 * @维护人:
	 * @version V1.0
	 */
	public static String getPointNumberStr(String decimalStr ,boolean isAccurate) {
		String val = "";
		if(isAccurate){
			val = filterStrzero(decimalStr,4);
		}else{
			val = filterStrzero(decimalStr,2);
		}
		if (!"--".equals(val)) {
			if (val.length() > 2) {
				if (val.endsWith(".0")) {
					val = val.substring(0, val.indexOf("."));
					if (val.length() > 2) {
						return val.substring(val.length() - 2);
					} else {
						return "val";
					}
				} else {
					if (val.contains(".")) {
						return val.substring(val.indexOf("."));
					} else {
						return val.substring(val.length() - 2);
					}
				}
			} else {
				return val;
			}
		} else {
			return "--";
		}
	}

	/**
	 * @Description: 转换为double类型
	 * @param @param decimalStr
	 * @param @return
	 * @return double
	 * @author NiuFC
	 * @date 2014年5月16日 下午1:54:47
	 * @维护人:
	 * @version V1.0
	 */
	public static double str2Double(String decimalStr) {
		if ("--".equals(decimalStr) || "null".equals(decimalStr) || decimalStr == null) {
			return 0;
		}
		return Double.valueOf(decimalStr);
	}

	/**
	 * @Title: str2Int
	 * @Description: String转换为int值
	 * @param @param decimalStr
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @author NiuFC
	 * @date 2014年8月1日 下午5:06:03
	 * @维护人:
	 * @version V1.0
	 */
	public static int str2Int(String decimalStr) {
		if ("--".equals(decimalStr) || "null".equals(decimalStr) || decimalStr == null) {
			return 0;
		}
		if (decimalStr.contains(".")) {
			return Integer.valueOf(decimalStr.substring(0, decimalStr.indexOf(".")));
		}
		return Integer.valueOf(decimalStr);
	}



	/**
	 * 判断是否为数值
	 * 
	 * @param @param str
	 * @param @return
	 * @return boolean
	 * @author NiuFC
	 * @date 2014年4月10日 上午10:20:33
	 * @维护人:
	 * @version V1.0
	 */
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int countPoint = 0;
		if ("".equals(str.trim()) || "--".equals(str.trim()) ||".".equals(str.trim())) {
			return false;
		}
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr == 46) {
				// 46是“.”的ASCII码值
				countPoint++;
				if (countPoint > 1) {
					return false;
				}
				continue;
			}
			if (chr == 47)// "/"
				return false;
			// 考虑到负号，兼容到45
			if (chr < 45 || chr > 57)
				return false;
		}
		return true;
	}

	/**
	 * 资金数字判断（只含有两位小数）
	 * 
	 * @param @param str
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @author NiuFC
	 * @date 2014年10月17日 下午7:57:42
	 * @维护人:
	 * @version V1.0
	 */
	public static boolean isFundsNumeric(String str) {
		int countPoint = 0;
		if ("".equals(str) || "--".equals(str) || str == null || ".".equals(str)) {
			return false;
		}
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr == 46) {
				// 46是“.”的ASCII码值
				countPoint++;
				if (countPoint > 1) {
					return false;
				}
				continue;
			}
			if (chr == 47)// "/"
				return false;
			// 考虑到负号，兼容到45
			if (chr < 45 || chr > 57)
				return false;
		}
		if (pointSize(str) > 2) {
			return false;
		}
		return true;
	}

	/**
	 * 判断小数点位数
	 * 
	 * @param str
	 * @return
	 */
	public static int pointSize(String str) {
		int size = 0;
		int index = str.indexOf(".");
		if (index != -1) {
			size = str.substring(index + 1, str.length()).length();
		}
		return size;
	}


	/**
	 * 格式化数字格式的字符串，小数位保留指定位数
	 * 
	 * @Title: filterStrzero
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param decimalStr
	 * @param @param iLen -1对小数点后面保留数不做处理，非负数时做相应处理，如0、1、2，保留相应小数
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @author NiuFC
	 * @date 2014年9月23日 上午11:42:17
	 * @维护人:
	 * @version V1.0
	 */
	@SuppressWarnings("unused")
	public static String filterStrzero(String decimalStr, int iLen) {
		if (decimalStr == null || "--".equals(decimalStr)) {
			return "0";
		}
		// 如果decimalStr为整数
		if (decimalStr != null || decimalStr.lastIndexOf(".") == -1) {
	
			return decimalStr;
		}
		// 含有小数的处理
		// 整数部分
		String intStr = decimalStr.substring(0, decimalStr.lastIndexOf("."));
		// 小数部分
		String decStr = decimalStr.substring(decimalStr.lastIndexOf(".") + 1, decimalStr.length());
		if ("00".equals(decStr) || "0".equals(decStr)) {
			// 如果是0.00返回0
			if ("0".equals(intStr)) {
				return "0";
			}
			return intStr;
		} else {
			// 保留指定位数
			BigDecimal bd = new BigDecimal(decimalStr);
			String sA;
			if (iLen ==-1) {
				sA = bd.setScale(decStr.length(), BigDecimal.ROUND_HALF_UP).toString();
			}else {
				sA = bd.setScale(iLen, BigDecimal.ROUND_HALF_UP).toString();
			}
			
			return sA;
		}
	}

	/**
	 * 判断一个字符串是否包含数字
	 * 
	 * @Title: hasDigit
	 * @Description: 判断一个字符串是否包含数字
	 * @param @param content
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @author NiuFC
	 * @date 2014年7月28日 下午4:50:42
	 * @维护人:
	 * @version V1.0
	 */
	public static boolean hasDigit(String content) {
		if (content.startsWith(".") || content.endsWith(".")) {
			return false;
		}
		if (content.substring(0, content.length() / 2).contains(".") && content.substring(content.length() / 2).contains(".")) {
			return false;
		}
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}

	/**
	 * 
	 * @Title: getPageCount
	 * @Description: 计算分页总页数
	 * @param @param rows 总记录数
	 * @param @param pageSize 分页大小
	 * @param @return 总页数
	 * @return int 返回类型
	 * @author NiuFC
	 * @date 2015年1月22日 上午10:02:37
	 * @评审人:
	 * @维护人:
	 * @version V1.0
	 */
	public static int getPageCount(int rows, int pageSize) {
		int _totalPages = 0;
		if (rows % pageSize == 0) {
			_totalPages = rows / pageSize;
		} else {
			_totalPages = rows / pageSize + 1;
		}
		return _totalPages;

	}
}
