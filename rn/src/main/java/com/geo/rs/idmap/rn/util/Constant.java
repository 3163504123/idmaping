package com.geo.rs.idmap.rn.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Constant {
	public static final String USERID_GEO = "uid";
	public static final String USERID_IMEI = "imei";
	public static final String USERID_IMSI = "imsi";
	public static final String USERID_INTERWEIBO = "interWebo";
	public static final String USERID_TAOBAO = "taobao";
	public static final String USERID_WEIBO = "weibo";
	public static final String USERID_MYQQ = "mqq";
	public static final String USERID_FRIENDQQ = "fqq";

	
	public static String[] allID = { USERID_GEO, USERID_IMEI, USERID_IMSI,
			USERID_INTERWEIBO, USERID_WEIBO,USERID_TAOBAO,USERID_MYQQ,USERID_FRIENDQQ};
	

	public static List<String> getAllMapResult(Map<String, String> AppIDs) {
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < allID.length; i++) {
			String ID1 = AppIDs.get(allID[i]);
			if (ID1 != null) {
				for (int j = i + 1; j < allID.length; j++) {
					String ID2 = AppIDs.get(allID[j]);
					if (AppIDs.get(allID[j]) != null) {
						list.add(String.format("%s-%s\005%s\t%s", allID[i],
								allID[j], ID1, ID2));
					}
				}
			}
		}

		return list;
	}
}
