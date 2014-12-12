package com.geo.rs.idmap.mr.dralation;

import java.io.IOException;

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
import com.geo.rs.idmap.mr.dralation.DirectRationIDMR.DirectRationIDMap;
import com.geo.rs.idmap.mr.dralation.DirectRationIDMR.DirectRationIDReduce;

public class DirectRationIDJob {
	static final Log log = LogFactory.getLog(DirectRationIDJob.class);

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {
		// TODO Auto-generated method stub
		JobConf conf = new JobConf();
		String[] userArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		if (userArgs.length < 2) {
			System.err.println("input <in> <out> <reduce num>!");
			return;
		}

		Job job = new Job(conf, "IDMapingJob");
		job.setInputFormatClass(AvroKeyInputFormat.class);
		AvroJob.setInputKeySchema(job, Http.getClassSchema());

		job.setJarByClass(DirectRationIDMR.class);
		job.setMapperClass(DirectRationIDMap.class);
		//job.setCombinerClass(DirectRationIDReduce.class);
		job.setReducerClass(DirectRationIDReduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		SkipBadRecords.setMapperMaxSkipRecords(conf, 1000);
		SkipBadRecords.setAttemptsToStartSkipping(conf, 5);

		job.setNumReduceTasks(args.length > 2 ? Integer.parseInt(args[2]) : 6);

		FileInputFormat.addInputPath(job, new Path(userArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(userArgs[1]));

		MultipleOutputs.addNamedOutput(job, "IDMAP", TextOutputFormat.class,
				Text.class, IntWritable.class);

		System.out.println(userArgs[0]);
		System.out.println(userArgs[1]);

		System.out.println(job.waitForCompletion(true) ? 0 : 1);

	}
}
