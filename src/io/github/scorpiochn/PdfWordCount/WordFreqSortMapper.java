package io.github.scorpiochn.PdfWordCount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordFreqSortMapper extends Mapper<LongWritable,Text, IntWritable, Text> {
    @Override
    public void map(LongWritable key, Text value, Context context)
    throws IOException, InterruptedException {
    	String line[]=value.toString().split("\t");
    	if(line.length==2) {
    		context.write(new IntWritable(Integer.parseInt(line[1])), new Text(line[0]));
    	}
    }    
}
