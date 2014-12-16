package com.geo.rs.idmap.rn.bean;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geo.rs.idmap.rn.util.GeoStringUtils;

public class GeoHttp {
	static Log log = LogFactory.getLog(GeoHttp.class);
	public static final String URL = "URL";
	public static final String COOKIE = "COOKIE";
	public static final String REFER = "REFER";
	
	private String host = "";
	private String uri = "";
	private String ua = "";
	private String ip = "";
	private String timestamp = "";
	private String cookie = "";
	private String geoId = "";
	private String refer = "";
	private Map<String, String> urlParams;
	private Map<String, String> referParams;
	private Map<String, String> cookieParams;
	private String urifullPath = "";
	private String referfullPath = "";

	public GeoHttp(String host, String uri, String ua, String ip,
			String timestamp, String cookie, String geoId, String refer) {
		this.host = (host == null) ? "" : host;
		this.uri = (uri == null) ? "" : uri;
		this.ua = (ua == null) ? "" : ua;
		this.ip = (ip == null) ? "" : ip;
		this.timestamp = (timestamp == null) ? "" : timestamp;
		this.cookie = (cookie == null) ? "" : cookie;
		this.geoId = (geoId == null) ? "" : geoId;
		this.refer = (refer == null) ? "" : refer;
		urlParams = GeoStringUtils.spilitToMap("?&", "=", this.uri);
		referParams = GeoStringUtils.spilitToMap("?&", "=", this.refer);
		cookieParams = GeoStringUtils.spilitToMap(" ;", "=", this.cookie);
		int uriparamStartIndex = uri.lastIndexOf("?");
		if (uriparamStartIndex <= 0) {
			uriparamStartIndex = this.uri.length();
		}
		urifullPath = this.host + this.uri.substring(0, uriparamStartIndex);
		if (refer != null) {
			try {
				URI referUri;
				referUri = new URI(this.refer);
				referfullPath = referUri.getHost() + referUri.getPath();
			} catch (URISyntaxException e) {
				log.warn("conver refer to url error");

			}
		}
	}

	public String getUrifullPath() {
		return urifullPath;
	}

	public void setUrifullPath(String urifullPath) {
		this.urifullPath = urifullPath;
	}

	public String getReferfullPath() {
		return referfullPath;
	}

	public void setReferfullPath(String referfullPath) {
		this.referfullPath = referfullPath;
	}

	public String getHost() {
		return host;
	}

	public Map<String, String> getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(Map<String, String> urlParams) {
		this.urlParams = urlParams;
	}

	public Map<String, String> getReferParams() {
		return referParams;
	}

	public void setReferParams(Map<String, String> referParams) {
		this.referParams = referParams;
	}

	public Map<String, String> getCookieParams() {
		return cookieParams;
	}

	public void setCookieParams(Map<String, String> cookieParams) {
		this.cookieParams = cookieParams;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getGeoId() {
		return geoId;
	}

	public void setGeoId(String geoId) {
		this.geoId = geoId;
	}

	public String getRefer() {
		return refer;
	}

	public void setRefer(String refer) {
		this.refer = refer;
	}

	public static void main(String[] args) {
		try {
			URI uri = new URI(
					"http://www.baidu.com/s?wd=333&rsv_spt=1&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_sug3=2&rsv_sug4=16&rsv_sug1=2&rsv_sug2=0&inputT=452");
		} catch (URISyntaxException e) {

			e.printStackTrace();
		}

	}

}
