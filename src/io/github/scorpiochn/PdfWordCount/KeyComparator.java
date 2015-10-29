package io.github.scorpiochn.PdfWordCount;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;


public class KeyComparator extends WritableComparator {
	protected KeyComparator() {
		super(WordFreqWritable.class, true);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public int compare(WritableComparable w1, WritableComparable w2) {
		WordFreqWritable ip1 = (WordFreqWritable) w1;
		WordFreqWritable ip2 = (WordFreqWritable) w2;
		return ip1.compareTo(ip2);
    }
}