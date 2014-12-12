package com.geo.rs.idmap.rn.pat;

import java.util.Map;

import com.geo.rs.idmap.rn.bean.GeoHttp;

public abstract class ACPattern {
	public abstract void match(Map<String, String> idMap,GeoHttp http);
}
