package io.github.scorpiochn.PdfWordCount;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class GroupPartitioner extends Partitioner<WordFreqWritable, NullWritable> {
	@Override
	public int getPartition(WordFreqWritable key, NullWritable value,
							int numPartitions) {
		
		return key.getFreq().get()%numPartitions;
	}
}