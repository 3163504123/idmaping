package com.geo.rs.idmap.rn;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geo.rs.idmap.rn.bean.GeoHttp;
import com.geo.rs.idmap.rn.util.Constant;
import com.geo.rs.idmap.rn.util.GeoStringUtils;

public class AppIDSearcher {
	static Log log = LogFactory.getLog(AppIDSearcher.class);
	private ACIDSeeker acSeeker = new ACIDSeeker();

	public AppIDSearcher() {
		acSeeker.compile();
	}

	public Map<String, String> search(GeoHttp http) {

		Map<String, String> idMap = new HashMap<String, String>();
		// 得到imsi
		String imsi = getParamsFromAllParam("imsi", http.getUrlParams(),
				http.getReferParams(), http.getCookieParams());

		if (imsi != null && imsi.matches("460\\d{12}")) {
			idMap.put(Constant.USERID_IMSI, imsi);
		}

		// 得到imei
		String imei = getParamsFromAllParam("imei", http.getUrlParams(),
				http.getReferParams(), http.getCookieParams());
		if (imei != null && imei.matches("\\d{15}")) {
			idMap.put(Constant.USERID_IMEI, imei);
		}

		acSeeker.seek(http, idMap);

		// 得到GEOID
		idMap.put(Constant.USERID_GEO, http.getGeoId());

		/*// 得到weibo帐号
		if (http.getHost().contains(".weibo.")
				|| http.getHost().contains(".sina.")) {
			String urlParam = http.getUrlParams().get("uid");
			if (urlParam != null && urlParam.length() >= 10
					&& urlParam.matches("\\d{10}")) {
				idMap.put(Constant.USERID_INTERWEIBO, urlParam);
			}
		} else if (http.getRefer().contains(".weibo.")
				|| http.getRefer().contains(".sina.")) {
			String referParam = http.getReferParams().get("uid");
			if (referParam != null && referParam.length() >= 10
					&& referParam.matches("\\d{10}")) {
				idMap.put(Constant.USERID_INTERWEIBO, referParam);
			}
		}

		// taobao帐号
		if (http.getHost().contains(".taobao.")
				|| http.getHost().contains(".tmall.")) {
			String taobaoID = http.getCookieParams().get("lgc");
			if (taobaoID == null) {
				taobaoID = http.getCookieParams().get("tracknick");
			}
			if (taobaoID != null) {
				try {
					String decodeID = URLDecoder.decode(taobaoID, "UTF-8");
					taobaoID = decodeID;
				} catch (Exception e) {
					log.info(String.format(
							"Decode taobaoID [%s]to UTF-8 ERROR ", taobaoID));
				}
				idMap.put(Constant.USERID_TAOBAO, taobaoID);
			}
		}
		*/

		return idMap;
	}

	private String getParamsFromAllParam(String findstr,
			Map<String, String> urlParams, Map<String, String> referParams,
			Map<String, String> cookieParams) {
		String findValue = urlParams.get(findstr);
		if (findValue == null || findValue.length()==0) {
			findValue = referParams.get(findstr);
			if (findValue == null) {
				findValue = cookieParams.get(findValue);
			}
		}
		return findValue;
	}
}
