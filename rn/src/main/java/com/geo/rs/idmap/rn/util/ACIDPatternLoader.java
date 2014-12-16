/**
 * Project Name:imSvc
 * File Name:PandaCompImanger.java
 * Package Name:com.geo.imanager.imSvc.dao
 * Date:2014年9月1日下午12:37:01
 * Copyright (c) 2014, 北京集奥聚合科技有限公司 All Rights Reserved.
 *
 */

package com.geo.rs.idmap.rn.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.geo.rs.idmap.rn.ACIDSeeker;
import com.geo.rs.idmap.rn.bean.GeoHttp;
import com.geo.rs.idmap.rn.pat.ACIDPattern;

/**
 * ClassName: PandaCompImanger <br/>
 * Function: TODO 对panda类型组件监控内容的管理. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年9月1日 下午12:37:01 <br/>
 * 
 * @author Eric Zhang
 * @version V1.0
 * @since JDK 1.6 Copyright (c) 2014, 北京集奥聚合科技有限公司 All Rights Reserved.
 */
public class ACIDPatternLoader {

	static Log logger = LogFactory.getLog(ACIDPatternLoader.class);

	static List<ACIDPattern> urlACIDPatternList = new LinkedList<ACIDPattern>();
	static List<ACIDPattern> referACIDPatternList = new LinkedList<ACIDPattern>();

	public static List<ACIDPattern> getUrlACIDPatternList() {
		return urlACIDPatternList;
	}

	public static void setUrlACIDPatternList(
			List<ACIDPattern> urlACIDPatternList) {
		ACIDPatternLoader.urlACIDPatternList = urlACIDPatternList;
	}

	public static List<ACIDPattern> getReferACIDPatternList() {
		return referACIDPatternList;
	}

	public static void setReferACIDPatternList(
			List<ACIDPattern> referACIDPatternList) {
		ACIDPatternLoader.referACIDPatternList = referACIDPatternList;
	}

	private static void loadMonitorToMap(Document doc) {

		Element e = doc.getRootElement();
		@SuppressWarnings("unchecked")
		Iterator<Element> it = (Iterator<Element>) e
				.elementIterator("idPatItem");

		// 遍历监控项,并转换成监控对象,并加入map中
		for (; it.hasNext();) {
			String patternUrl = "";
			try {
				Element element = (Element) it.next();

				String patternPos = element.attribute("patternPos").getText();
				String idType = element.attribute("idType").getText();
				patternUrl = element.attribute("patternUrl").getText();
				String pos = element.attribute("segPosition").getText();
				String segName = element.attribute("segName").getText();
				String needDecoderCnt = element.attribute("decodeCnt") == null ? "0"
						: element.attribute("decodeCnt").getText();

				String decodeCharset = element.attribute("decodeCode") == null ? "UTF-8"
						: element.attribute("decodeCode").getText();

				String validateRegular = element.attribute("validateRegular") == null ? null
						: element.attribute("validateRegular").getText();

				String regularExtractIndex = element
						.attribute("extractRegularIndex") == null ? "-1"
						: element.attribute("extractRegularIndex").getText();

				logger.info(String
						.format("patternPos:%s\tpatternUrl：%s\tpos:%s\tsegName:%s\tneedDocoderCnt:%s\tdecodeCharset:%s\tvalidateRegular:%s\tidType:%s\tRegularExtractIndex:%s",
								patternPos, patternUrl, pos, segName,
								needDecoderCnt, decodeCharset, validateRegular,
								idType,regularExtractIndex));
				
				if (patternPos.equalsIgnoreCase(GeoHttp.URL)) {
					urlACIDPatternList.add(new ACIDPattern(patternUrl, idType,
							pos, segName, Integer.parseInt(needDecoderCnt),
							decodeCharset, validateRegular,Integer
							.parseInt(regularExtractIndex)));
				} else if (patternPos.equalsIgnoreCase(GeoHttp.REFER)) {
					referACIDPatternList.add(new ACIDPattern(patternUrl,
							idType, pos, segName, Integer
									.parseInt(needDecoderCnt), decodeCharset,
							validateRegular,Integer
							.parseInt(regularExtractIndex)));
				}

			} catch (Exception e2) {
				logger.error(
						String.format("id SourceFile Error,Please Check!! "),
						e2);
			}

		}
	}

	/**
	 * loadMonitorAttrs:(从配置文件中加载各panda组件的监控项). <br/>
	 * TODO(适用条件 – 无).<br/>
	 * TODO(执行流程 – 无).<br/>
	 * TODO(使用方法 – 无).<br/>
	 * TODO(注意事项 – 无).<br/>
	 * 
	 * @author Eric Zhang
	 * @throws DocumentException
	 * @since JDK 1.6
	 */
	private static void loadIDAttrs() {
		try {
			// 读取xml
			SAXReader reader = new SAXReader();
			Document doc = null;

			doc = reader.read(ACIDPatternLoader.class
					.getResourceAsStream("/idRegular.xml"));

			loadMonitorToMap(doc);

		} catch (Exception e) {
			logger.error(e);
			System.exit(-1);
		}

	}

	static {
		loadIDAttrs();
	}

	public static void main(String[] args) {
		ACIDPatternLoader acp = new ACIDPatternLoader();
	}

}
