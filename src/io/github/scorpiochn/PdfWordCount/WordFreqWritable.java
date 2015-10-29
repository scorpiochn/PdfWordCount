package io.github.scorpiochn.PdfWordCount;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class WordFreqWritable  implements WritableComparable<WordFreqWritable>{
	
	private String word;
	private int freq;
	
	public WordFreqWritable(String w, int f)
	{
		word = w;
		freq = f;
	}
	
	public IntWritable getFreq() {
		return new IntWritable(freq);
	}
	
	public Text getWord() {
		return new Text(word);
	}
	

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(word);
		out.writeInt(freq);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		word = in.readUTF();
		freq = in.readInt();
	}

	@Override
	public int compareTo(WordFreqWritable o) {
		if(freq < o.freq) {
			return -1;
		}
		else if(freq > o.freq) {
			return 1;
		}
		else {
			return word.compareTo(o.word);
		}
	}
	
	@Override
	public String toString() {
		return word+","+freq;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof WordFreqWritable) {
			WordFreqWritable ip = (WordFreqWritable) o;
			return ip.word == word && ip.freq == freq;
	    }
	    return false;
	}
}
