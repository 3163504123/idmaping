package com.geo.rs.idmap.rn.pat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geo.rs.idmap.rn.bean.GeoHttp;

public class ACIDPattern extends ACPattern {
	static final Log log = LogFactory.getLog(ACIDPattern.class);

	private String idType = "";
	private String pos = "";
	private String segName = "";
	private int needDecoderCnt = 0;
	private String decodeCharset = "UTF-8";
	private String validateRegular;
	private Pattern validatePat;
	private String urlPattern;

	public String getValidateRegular() {
		return validateRegular;
	}

	public void setValidateRegular(String validateRegular) {
		this.validateRegular = validateRegular;
	}

	public Pattern getValidatePat() {
		return validatePat;
	}

	public void setValidatePat(Pattern validatePat) {
		this.validatePat = validatePat;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public ACIDPattern(String urlPattern, String idType, String pos,
			String segName, int needDecoderCnt, String decodeCharset,
			String validateRegular) {
		this.idType = idType;
		this.pos = pos;
		this.segName = segName;
		this.needDecoderCnt = needDecoderCnt;
		this.decodeCharset = decodeCharset;
		this.validateRegular = validateRegular;
		this.urlPattern = urlPattern;
		
		if (this.validateRegular != null) {
			try {
				this.validatePat = Pattern.compile(validateRegular);
			} catch (Exception e) {
				log.warn(String.format("complie regular str [%s] error ",
						validateRegular), e);
			}
		}
	}

	public ACIDPattern(String urlPattern, String idType, String pos,
			String segName, String validateRegular) {
		this(urlPattern, idType, pos, segName, 0, segName, validateRegular);
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getsegName() {
		return segName;
	}

	public void setsegName(String segName) {
		this.segName = segName;
	}

	public int getNeedDecoderCnt() {
		return needDecoderCnt;
	}

	public void setNeedDecoderCnt(int needDecoderCnt) {
		this.needDecoderCnt = needDecoderCnt;
	}

	public String getDecodeCharset() {
		return decodeCharset;
	}

	public void setDecodeCharset(String decodeCharset) {
		this.decodeCharset = decodeCharset;
	}

	public String decode(String target) {
		String midStr = target;
		for (int i = 0; i < needDecoderCnt; i++) {
			try {
				midStr = URLDecoder.decode(midStr, decodeCharset);
			} catch (Exception e) {
				log.warn(String.format("decode seg value [%s]error", midStr));
				return null;
			}
		}

		return midStr;
	}

	@Override
	public void match(Map<String, String> idMap, GeoHttp http) {
		String segValue;
		if (pos.equals(GeoHttp.URL)) {
			segValue = http.getUrlParams().get(segName);
		} else if (pos.equals(GeoHttp.REFER)) {
			segValue = http.getReferParams().get(segName);
		} else if (pos.equals(GeoHttp.COOKIE)) {
			segValue = http.getCookieParams().get(segName);
		} else {
			return;
		}

		if (segValue != null) {

			if (idMap.get(idType) == null
					&& (validatePat == null || validatePat.matcher(segValue)
							.matches())) {
				String targetValue = decode(segValue);
				if (targetValue != null) {
					idMap.put(idType, targetValue);
				}
			}

		}

	}

}
