package io.github.scorpiochn.PdfWordCount;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class  GroupComparator extends WritableComparator {
	protected GroupComparator() {
		super(WordFreqWritable.class, true);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public int compare(WritableComparable  w1, 
					   WritableComparable  w2) {
		WordFreqWritable ip1 = (WordFreqWritable) w1;
		WordFreqWritable ip2 = (WordFreqWritable) w2;
        return ip1.getFreq().get()-ip2.getFreq().get();
      }
    }

