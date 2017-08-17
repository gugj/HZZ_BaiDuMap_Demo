package com.roch.hzz_baidumap_demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * 判断当前的object是否为null或为空-""
	 */
	public static boolean isEmpty(Object obj) {
		return null == obj || "".equals(obj.toString().trim());
	}

	/**
	 * 判断当前对象是否不为null或不为空-""
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static String getSequenceId() {
		String mark = String.valueOf(System.currentTimeMillis());
		return mark;
	}

	/**
	 * 获取当前时间(yyyy-MM-dd HH:mm:ss)
	 */
	public static String getCurrentlyDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return dateFormat.format(new Date());
	}

	/**
	 * 获取时间的长日期(yyyy-MM-dd HH:mm:ss)
	 */
	public static String transformDateTime(long t) {
		Date date = new Date(t);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return dateFormat.format(date);
	}

	/**
	 * 获取当前日期(yyyy-MM-dd)
	 */
	public static String getCurrentlyDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		return dateFormat.format(new Date(System.currentTimeMillis()));
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 字符串特殊处理
	 */
	public static SpannableString handlerText(String str, int start, Context context) {
		SharedPreferences sh = context.getSharedPreferences("dpi", 0);
		SpannableString sp = new SpannableString(str);
		if (sh.getInt("dpi_type", 0) == 1) {
			sp.setSpan(new AbsoluteSizeSpan(25), start, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			sp.setSpan(new AbsoluteSizeSpan(18), start, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return sp;
	}

	/**
	 * 判断该字符串是不是为手机号
	 */
	public static boolean checkPhone(String phone) {
		Pattern pattern = Pattern.compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");
		Matcher matcher = pattern.matcher(phone);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	// string类型转换为long类型
	// strTime要转换的String类型的时间
	// formatType时间格式
	// strTime的时间格式和formatType的时间格式必须相同
	public static long stringToLong(String strTime, String formatType) {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	// string类型转换为date类型
	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	// HH时mm分ss秒，
	// strTime的时间格式必须要与formatType的时间格式相同
	public static Date stringToDate(String strTime, String formatType){
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// date类型转换为long类型
	// date要转换的date类型的时间
	public static long dateToLong(Date date) {
		return date.getTime();
	}

//----------------------------------------------------------------------------------------------------

	// long类型转换为String类型
	// currentTime要转换的long类型的时间
	// formatType要转换的string类型的时间格式
	public static String longToString(long currentTime, String formatType) {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}

	// long转换为Date类型
	// currentTime要转换的long类型的时间
	// formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	public static Date longToDate(long currentTime, String formatType) {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	// date类型转换为String类型
	// formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	// data Date类型的时间
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

}
