package com.geo.rs.idmap.mr.dralation;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.avro.mapred.AvroKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.geo.dmp.model.Http;
import com.geo.rs.idmap.rn.AppIDSearcher;
import com.geo.rs.idmap.rn.bean.GeoHttp;
import com.geo.rs.idmap.rn.util.Constant;

public class DirectRationIDMR {

	public static class DirectRationIDMap extends
			Mapper<AvroKey<Http>, NullWritable, Text, IntWritable> {
		final static Log logger = LogFactory.getLog(DirectRationIDMap.class);
		private Text k = new Text();
		private IntWritable one = new IntWritable(1);
		private AppIDSearcher ais = new AppIDSearcher();

		@Override
		protected void map(AvroKey<Http> key, NullWritable value,
				Context context) throws IOException, InterruptedException {

			Http http = key.datum();

			// 得到所有帐号集

			Map<String, String> data = ais.search(new GeoHttp(http.getHost()
					.toString(), http.getUrl().toString(), http.getUserAgent()
					.toString(), http.getUserIpAddr().toString(), http
					.getTimeStamp().toString(), http.getCookie().toString(),
					http.getUserId().toString(), http.getRefer().toString()));

			List<String> list = Constant.getAllMapResult(data);

			for (Iterator<String> iterator = list.iterator(); iterator
					.hasNext();) {
				String idMap = iterator.next();
				k.set(idMap);
				context.write(k, one);
			}

		}
	}

	public static class DirectRationIDReduce extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private MultipleOutputs<Text, IntWritable> mos;

		@Override
		protected void setup(
				Reducer<Text, IntWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			mos = new MultipleOutputs<Text, IntWritable>(context);
		}

		protected void reduce(Text key, Iterable<IntWritable> values,
				Context output) throws IOException, InterruptedException {

			int count = 0;
			Iterator<IntWritable> itor = values.iterator();
			while (itor.hasNext()) {
				count += itor.next().get();
			}

			String[] segs = key.toString().split("\005");
			mos.write("IDMAP", segs[1], new IntWritable(count), segs[0] + "/");
		}

		@Override
		protected void cleanup(
				Reducer<Text, IntWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			mos.close();
		}
	}

}
