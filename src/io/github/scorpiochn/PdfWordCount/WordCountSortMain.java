package io.github.scorpiochn.PdfWordCount;

import java.net.URI;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCountSortMain {
    public static void main(String[] args) throws Exception {
    	
    	JobConf conf=new JobConf(WordCountSortMain.class);
    	conf.set("mapreduce.ifile.readahead", "false");
    	conf.set("mapreduce.output.textoutputformat.separator", ":");
    	String Args[] = new GenericOptionsParser(conf, args).getRemainingArgs();
    	
        Job job_wc = Job.getInstance(conf);
        String dictPath = conf.get("dict.path");
        if(dictPath!=null) {
        	URI dictUri = new URI(dictPath);
        	job_wc.addCacheArchive(dictUri);
        }
        
        job_wc.setJobName("WordCount");
        job_wc.setInputFormatClass(PdfInputFormat.class);
        FileInputFormat.addInputPath(job_wc, new Path(Args[0]));
        FileOutputFormat.setOutputPath(job_wc, new Path(Args[1]));
        job_wc.setMapperClass(WordCountMapper.class);
        job_wc.setReducerClass(WordCountReducer.class);
        job_wc.setOutputKeyClass(Text.class);
        job_wc.setOutputValueClass(IntWritable.class);
        
        Job job_sort = Job.getInstance(conf);
        job_sort.setJobName("Word Freq Sort");
        job_sort.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job_sort, new Path(Args[1]));
        FileOutputFormat.setOutputPath(job_sort, new Path(Args[2]));
        job_sort.setMapperClass(WordFreqSortMapper.class);
        job_sort.setReducerClass(WordFreqSortReducer.class);
        
        job_sort.setPartitionerClass(GroupPartitioner.class);
        job_sort.setSortComparatorClass(KeyComparator.class);
        job_sort.setGroupingComparatorClass(GroupComparator.class);
        
        job_sort.setMapOutputKeyClass(WordFreqWritable.class);
        job_sort.setMapOutputValueClass(NullWritable.class);
        job_sort.setOutputKeyClass(IntWritable.class);
        job_sort.setOutputValueClass(Text.class);
        job_sort.setNumReduceTasks(1);
        
        ControlledJob cj_wc=new  ControlledJob(conf);
        cj_wc.setJob(job_wc);
        
        ControlledJob cj_sort=new  ControlledJob(conf);
        cj_sort.setJob(job_sort);
        
        cj_sort.addDependingJob(cj_wc);
        
        JobControl jobCtrl=new JobControl("Word Count & Sort");        
        jobCtrl.addJob(cj_wc); 
        jobCtrl.addJob(cj_sort);
        
        Thread jcThread = new Thread(jobCtrl);
        jcThread.start();  
        int status = 0;
        while(true) {  
        	if(jobCtrl.allFinished()){  
        		System.out.println(jobCtrl.getSuccessfulJobList());  
                jobCtrl.stop();  
                status = 0;
                break;
            } 
            if(jobCtrl.getFailedJobList().size() > 0){  
                System.out.println(jobCtrl.getFailedJobList());  
                jobCtrl.stop();  
                status = -1;
                break;
            }  
        }
        
        Counters counter = job_sort.getCounters();
        System.out.println("All Tokens: "+counter.findCounter("WordCountMain.WordStats", "TOKENS").getValue());
        System.out.println("     Words: "+counter.findCounter("WordCountMain.WordStats", "WORDS").getValue());

        System.out.println("All Tokens: "+counter.findCounter(WordCountMain.WordStats.TOKENS).getValue());
        System.out.println("     Words: "+counter.findCounter(WordCountMain.WordStats.WORDS).getValue());

        
        System.exit(status);
    }
}
