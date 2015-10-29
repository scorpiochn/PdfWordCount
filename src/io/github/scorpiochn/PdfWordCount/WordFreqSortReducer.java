package io.github.scorpiochn.PdfWordCount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordFreqSortReducer extends Reducer<WordFreqWritable, NullWritable, IntWritable, Text> {
	
	@Override
	public void reduce(WordFreqWritable key, Iterable<NullWritable> values, 
					   Context context)
		    throws IOException, InterruptedException {
		for (@SuppressWarnings("unused") NullWritable w : values) {
			context.write(key.getFreq(), key.getWord());
			}
		}
}
