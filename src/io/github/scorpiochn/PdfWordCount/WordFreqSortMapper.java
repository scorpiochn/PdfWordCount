package io.github.scorpiochn.PdfWordCount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordFreqSortMapper extends Mapper<LongWritable,Text, WordFreqWritable, NullWritable> {
    @Override
    public void map(LongWritable key, Text value, Context context)
    throws IOException, InterruptedException {
    	Configuration conf = context.getConfiguration();
    	String line[]=value.toString().split(
    			conf.get("mapreduce.output.textoutputformat.separator", "\t")
    			);
    	if(line.length==2) {
    		context.write(new WordFreqWritable(line[0], Integer.parseInt(line[1])),
    		              NullWritable.get());
    	}
    }
}
