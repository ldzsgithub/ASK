package com.jy23.util;


import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jy23.anno.MaxLength;
import com.jy23.anno.MinLength;
import com.jy23.anno.NotEmpty;
import com.jy23.anno.NotNull;
import com.jy23.anno.RE;

public class ValidateKit {
	/**
	 * 判断所有数据类型，是否为 null
	 *
	 * @param object 方法的参数
	 * @return true 不是null的， false 是null的
	 */
	public static boolean isNull(Object object) {
		return object == null;
	}

	public static boolean isEmpty(Object object) {
		if (isNull(object)) return true;

		if (object instanceof String) {
			if (object.equals("")) {
				return true;
			}
		}

		if (object instanceof Object[]) {
			if (((Object[]) object).length == 0) {
				return true;
			}
		}
		if (object instanceof Iterable) {
			if (!((Iterable) object).iterator().hasNext()) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] ra) {
	}

	public static Object[] check(Annotation an, Object arg) {
		Object[] objects = null;
		if (an.annotationType() == NotNull.class) {
			if (ValidateKit.isNull(arg)) {
				return new Object[]{((NotNull) an).msg(), ((NotNull) an).code()};
			}
		}
		if (an.annotationType() == NotEmpty.class) {
			if (ValidateKit.isEmpty(arg)) {
				return new Object[]{((NotEmpty) an).msg(), ((NotEmpty) an).code()};
			}
		}

		if (an.annotationType() == MaxLength.class) {
			if (ValidateKit.maxLength(arg, ((MaxLength) an).length())) {
				return new Object[]{((MaxLength) an).msg(), ((MaxLength) an).code()};
			}
		}

		if (an.annotationType() == MinLength.class) {
			if (ValidateKit.minLength(arg, ((MinLength) an).length())) {
				return new Object[]{((MinLength) an).msg(), ((MinLength) an).code()};
			}
		}

		if (an.annotationType() == RE.class) {
			if (ValidateKit.re(arg, ((RE) an).re())) {
				return new Object[]{((RE) an).msg(), ((RE) an).code()};
			}
		}
		return objects;
	}

	public static boolean re(Object arg, String re) {
		if (isNull(arg)) return true;
		Pattern r = Pattern.compile(re);
		Matcher m = r.matcher(String.valueOf(arg));
		if (!m.matches()) {
			return true;
		}
		return false;
	}

	private static boolean minLength(Object arg, int length) {
		if (isNull(arg)) return true;
		if (String.valueOf(arg).length() < length) {
			return true;
		}
		return false;
	}

	private static boolean maxLength(Object arg, int length) {
		if (isNull(arg)) return true;
		if (String.valueOf(arg).length() > length) {
			return true;
		}
		return false;
	}
}
