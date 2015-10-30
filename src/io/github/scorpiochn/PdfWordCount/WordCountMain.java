package io.github.scorpiochn.PdfWordCount;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCountMain {
	
	enum WordStats {
		TOKENS,
		WORDS
	}
	
	
    public static void main(String[] args) throws Exception {
        Job job = Job.getInstance();
        Configuration conf = job.getConfiguration();
        
        conf.set("mapreduce.ifile.readahead", "false");
        
        String Args[] = new GenericOptionsParser(conf, args).getRemainingArgs();
        
        String dictPath = conf.get("dict.path");
        if(dictPath!=null) {
        	URI dictUri = new URI(dictPath);
        	job.addCacheArchive(dictUri);
        }
        
        job.setInputFormatClass(PdfInputFormat.class);
        job.setJarByClass(WordCountMain.class);
        job.setJobName("WordCount");
        FileInputFormat.addInputPath(job, new Path(Args[0]));
        FileOutputFormat.setOutputPath(job, new Path(Args[1]));
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
