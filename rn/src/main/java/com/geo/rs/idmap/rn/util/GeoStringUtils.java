package com.geo.rs.idmap.rn.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class GeoStringUtils {

	public static Map<String, String> spilitToMap(String ItemSpiltFlag,
			String KeyValueSpiltFlag, String splitObj) {
		
		

		Map<String, String> map = new HashMap<String, String>();
		
		
		if(splitObj == null) {
			return map;
		}
		StringTokenizer stringTokenizer = new StringTokenizer(splitObj,
				ItemSpiltFlag);
		while (stringTokenizer.hasMoreTokens()) {
			String[] keyValue = stringTokenizer.nextToken().split(
					KeyValueSpiltFlag);
			if (keyValue.length >= 2) {
				map.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
			}
		}

		return map;
	}

	public static void main(String[] args) {
		GeoStringUtils.spilitToMap("&", "=", "123=456&456=123");
		GeoStringUtils.spilitToMap(";", "=", "123=456; 456=123");
		GeoStringUtils.spilitToMap(";?", "=", "/dfsdf/dfjasdjfkldjasfj?123=2222;dsfasdfdas=fdasf;");
		GeoStringUtils.spilitToMap(";?", "=", "http://www.baidu.com/dfsdf/dfjasdjfkldjasfj?123=2222;dsfasdfdas=fdasf;");
	}

}
