package com.geo.rs.idmap.mr.dralation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SkipBadRecords;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.geo.dmp.model.Http;
import com.geo.rs.idmap.mr.dralation.DirectRationIDMR.DirectRationIDCombiner;
import com.geo.rs.idmap.mr.dralation.DirectRationIDMR.DirectRationIDMap;
import com.geo.rs.idmap.mr.dralation.DirectRationIDMR.DirectRationIDReduce;
import com.geo.rs.idmap.mr.utils.Constants;

public class DirectRationIDJob {
	static final Log log = LogFactory.getLog(DirectRationIDJob.class);

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {
		// TODO Auto-generated method stub
		JobConf conf = new JobConf();
		String[] userArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		System.out.println(userArgs[0]);
		System.out.println(userArgs[1]);
		System.out.println(userArgs[2]);
		if (userArgs.length < 3) {
			System.err
					.println("input <in> <out_prerfix_dir> <out_suffix_dir> <reduce num>!");
			return;
		}

		Job job = new Job(conf, "IDMapingJob");

		job.setInputFormatClass(AvroKeyInputFormat.class);
		AvroJob.setInputKeySchema(job, Http.getClassSchema());

		job.setJarByClass(DirectRationIDMR.class);
		job.setMapperClass(DirectRationIDMap.class);
		job.setCombinerClass(DirectRationIDCombiner.class);
		job.setReducerClass(DirectRationIDReduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.getConfiguration().set(Constants.OUTPUT_FILE_PRIFIX, userArgs[1]);
		job.getConfiguration().set(Constants.OUTPUT_FILE_SUFFIX, userArgs[2]);
		SkipBadRecords.setMapperMaxSkipRecords(conf, 100000);
		SkipBadRecords.setAttemptsToStartSkipping(conf, 3);

		job.setNumReduceTasks(userArgs.length > 3 ? Integer.parseInt(userArgs[3])
				: 6);

		FileInputFormat.addInputPath(job, new Path(userArgs[0]));
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		String curTime = dateformat.format(new Date());
		FileOutputFormat.setOutputPath(job, new Path(userArgs[1] + "/run/"
				+ curTime));
		MultipleOutputs.addNamedOutput(job, "IDMAP", TextOutputFormat.class,
				Text.class, IntWritable.class);
		System.out.println(job.waitForCompletion(true) ? 0 : 1);

	}
}
