package io.github.scorpiochn.PdfWordCount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class WordFreqSortReducer extends Reducer<WordFreqWritable, NullWritable, IntWritable, Text> {
	
	private MultipleOutputs<IntWritable, Text> multipleOutputs;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void setup(Context context) 
			throws IOException, InterruptedException {
		multipleOutputs = new MultipleOutputs(context);
	}
	
	@Override
	public void reduce(WordFreqWritable key, Iterable<NullWritable> values, 
					   Context context)
		    throws IOException, InterruptedException {
		for (@SuppressWarnings("unused") NullWritable w : values) {
			/*context.write(key.getFreq(), key.getWord());*/
			multipleOutputs.write(key.getWord().toString().substring(0, 1),key.getFreq(), key.getWord());
			
		}
	}
	
	@Override
	protected void cleanup(Context context)
	throws IOException, InterruptedException {
		multipleOutputs.close();
	}
}
