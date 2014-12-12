package com.geo.rs.idmap.rn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;

import com.alibaba.fastjson.JSONObject;
import com.geo.rs.idmap.rn.bean.GeoHttp;
import com.geo.rs.idmap.rn.pat.ACIDPattern;
import com.geo.rs.idmap.rn.pat.ACPattern;
import com.geo.rs.idmap.rn.util.ACIDPatternLoader;

/**
 * @author Administrator 通过自动机匹配网站，并
 */
public class ACIDSeeker {
	static final Log logger = LogFactory.getLog(ACIDSeeker.class);

	private AhoCorasick urlTree = new AhoCorasick();
	private AhoCorasick referTree = new AhoCorasick();

	public boolean compile() {

		logger.info("start compile AC");
		List<ACIDPattern> referPatList = (List<ACIDPattern>) ACIDPatternLoader
				.getReferACIDPatternList();
		List<ACIDPattern> urlPatList = (List<ACIDPattern>) ACIDPatternLoader
				.getUrlACIDPatternList();
		
		for (Iterator iterator = urlPatList.iterator(); iterator.hasNext();) {
			ACIDPattern acPattern = (ACIDPattern) iterator.next();
			urlTree.add(acPattern.getUrlPattern().getBytes(),acPattern);
		}
		
		for (Iterator iterator = referPatList.iterator(); iterator.hasNext();) {
			ACIDPattern acPattern = (ACIDPattern) iterator.next();
			referTree.add(acPattern.getUrlPattern().getBytes(),acPattern);
		}

		urlTree.prepare();
		referTree.prepare();
		logger.info("compile AC Success");
		return true;
	}

	public void seek(GeoHttp http, Map<String, String> idMap) {
		// 匹配网址，得到抽取标识
		// 返回

		for (Iterator iter = urlTree.search(http.getUrifullPath().getBytes()); iter
				.hasNext();) {
			SearchResult sr = (SearchResult) iter.next();

			Iterator<ACPattern> acms = sr.getOutputs().iterator();
			while (acms.hasNext()) {
				ACPattern acm = acms.next();
				acm.match(idMap, http);
			}

		}

		for (Iterator iter = referTree.search(http.getReferfullPath()
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
				"snsuid=137221430210376; gdtrst=1366*768; qz_gdtinner=2dqigvapaaafoekqu35q; RK=GYnL27DROK; pt2gguin=o0361849250; ptcz=59b302530c393dbf83daade6e87bd25b4a701da7bb44eb6d971af99e2581a7ab; pgv_info=ssid=s1619843223; pgv_pvid=2923141782; o_cookie=361849250;",
				"xxx",
				"http://union.paipai.com/billboard/minsite/ads/qq_index_big.shtml");
		Map<String, String> map1 = new HashMap<String, String>();
		ACIDSeeker acidSeeker = new ACIDSeeker();
		acidSeeker.compile();
		acidSeeker.seek(http, map1);
		System.err.println(JSONObject.toJSONString(map1));
	}
}
