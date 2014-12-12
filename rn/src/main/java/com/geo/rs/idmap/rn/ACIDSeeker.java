package com.geo.rs.idmap.rn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rs.idmap.rn.bean.GeoHttp;
import com.geo.rs.idmap.rn.pat.ACIDPattern;
import com.geo.rs.idmap.rn.pat.ACPattern;
import com.geo.rs.idmap.rn.util.Constant;

/**
 * @author Administrator 通过自动机匹配网站，并
 */
public class ACIDSeeker {
	static final Log logger = LogFactory.getLog(ACIDSeeker.class);

	private AhoCorasick urltree = new AhoCorasick();
	private AhoCorasick refertree = new AhoCorasick();

	public boolean compile() {

		logger.info("start compile AC");

		urltree.add(".qq.".getBytes(), new ACIDPattern(Constant.USERID_MYQQ,
				GeoHttp.COOKIE, "o_cookie", "\\d{5,12}"));
		urltree.add("btrace.qq.com/collect".getBytes(), new ACIDPattern(
				Constant.USERID_MYQQ, GeoHttp.URL, "iqq", "\\d{5,12}"));
		urltree.add(".qq.".getBytes(), new ACIDPattern(
				Constant.USERID_FRIENDQQ, GeoHttp.URL, "dstuin", "\\d{5,12}"));
		urltree.add(".qq.".getBytes(), new ACIDPattern(
				Constant.USERID_FRIENDQQ, GeoHttp.URL, "touin", "\\d{5,12}"));
		urltree.add(".qq.".getBytes(), new ACIDPattern(
				Constant.USERID_FRIENDQQ, GeoHttp.URL, "dstuin", "\\d{5,12}"));
		urltree.add(".qq.".getBytes(), new ACIDPattern(
				Constant.USERID_FRIENDQQ, GeoHttp.URL, "openuin", "\\d{5,12}"));
		urltree.add(".qq.".getBytes(), new ACIDPattern(
				Constant.USERID_FRIENDQQ, GeoHttp.URL, "guestuin", "\\d{5,12}"));

		refertree.add(".qq.".getBytes(), new ACIDPattern(Constant.USERID_MYQQ,
				GeoHttp.COOKIE, "o_cookie", "\\d{5,12}"));
		refertree.add("btrace.qq.com/collect".getBytes(), new ACIDPattern(
				Constant.USERID_MYQQ, GeoHttp.URL, "iqq", "\\d{5,12}"));
		refertree
				.add(".qq.".getBytes(), new ACIDPattern(
						Constant.USERID_FRIENDQQ, GeoHttp.REFER, "dstuin",
						"\\d{5,12}"));
		refertree.add(".qq.".getBytes(), new ACIDPattern(
				Constant.USERID_FRIENDQQ, GeoHttp.REFER, "touin", "\\d{5,12}"));
		refertree
				.add(".qq.".getBytes(), new ACIDPattern(
						Constant.USERID_FRIENDQQ, GeoHttp.REFER, "dstuin",
						"\\d{5,12}"));
		refertree.add(".qq.".getBytes(),
				new ACIDPattern(Constant.USERID_FRIENDQQ, GeoHttp.REFER,
						"openuin", "\\d{5,12}"));
		refertree.add(".qq.".getBytes(), new ACIDPattern(
				Constant.USERID_FRIENDQQ, GeoHttp.REFER, "guestuin",
				"\\d{5,12}"));

		/*
		 * tree.add("taobao.com".getBytes(), new ACMatcher("taobao.com",
		 * ".*(?:tracknick|lgc)=([^;]+);.*", 1, "taobao_"));
		 * 
		 * tree.add(".jd.com".getBytes(), new UrlDecoderACMatcher(".jd.com",
		 * ".*(?:_pst|pin)=([^;]+);.*", 1, "jd_"));
		 * 
		 * tree.add(".qq.com".getBytes(), new ACMatcher(".qq.com",
		 * ".*(?:o_cookie|pt2gguin|[^\\w]uin|pin)=[o0]*(\\d+);.*", 1, "qq_"));
		 * 
		 * tree.add(".baidu.com".getBytes(), new ACMatcher(".baidu.com",
		 * ".*(?:BAIDUPSID)=([^;]{32});.*", 1, "baidu_"));
		 * 
		 * tree.add(".sina.com".getBytes(), new UrlDecoderACMatcher(".sina.com",
		 * ".*%26name%3D(.*?)%26.*", 1, "sina_"));
		 * 
		 * tree.add(".weibo.".getBytes(), new UrlDecoderACMatcher(".weibo.",
		 * ".*%26name%3D(.*?)%26.*", 1, "sina_"));
		 */
		urltree.prepare();
		refertree.prepare();
		logger.info("compile AC Success");
		return true;
	}

	public void seek(GeoHttp http, Map<String, String> idMap) {
		// 匹配网址，得到抽取标识
		// 返回

		for (Iterator iter = urltree.search(http.getUrifullPath().getBytes()); iter
				.hasNext();) {
			SearchResult sr = (SearchResult) iter.next();

			Iterator<ACPattern> acms = sr.getOutputs().iterator();
			while (acms.hasNext()) {
				ACPattern acm = acms.next();
				acm.match(idMap, http);
			}

		}

		for (Iterator iter = refertree.search(http.getReferfullPath()
				.getBytes()); iter.hasNext();) {
			SearchResult sr = (SearchResult) iter.next();

			Iterator<ACPattern> acms = sr.getOutputs().iterator();
			while (acms.hasNext()) {
				ACPattern acm = acms.next();
				acm.match(idMap, http);
			}

		}

	}

	public static void main(String[] args) {
		GeoHttp http = new GeoHttp(
				"v.gdt.qq.com",
				"/gdt_stats.fcg?viewid=iH9U!EQjo!_JU1bC!5GFLaMZzsawgFf2p9UPIG8nV6xbID_oitNE6k7hiQ5P06piNNPLyOI8h0pF8y!RPRc0fVugrTbZXce26F1uByKh61HdkC3JUEBL4gTJahU821H2&i=1&datatype=jsonp&callback=_cb_gdtjson1",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36",
				"10.111.23.214",
				"201402220111",
				"snsuid=137221430210376; gdtrst=1366*768; qz_gdtinner=2dqigvapaaafoekqu35q; RK=GYnL27DROK; pt2gguin=o0361849250; ptcz=59b302530c393dbf83daade6e87bd25b4a701da7bb44eb6d971af99e2581a7ab; pgv_info=ssid=s1619843223; pgv_pvid=2923141782; o_cookie=361849250",
				"xxx",
				"http://union.paipai.com/billboard/minsite/ads/qq_index_big.shtml");
		Map<String, String> map1 = new HashMap<String, String>();
		ACIDSeeker acidSeeker = new ACIDSeeker();
		acidSeeker.compile();
		acidSeeker.seek(http, map1);
		System.err.println(JSONObject.toJSONString(map1));

	}
}
