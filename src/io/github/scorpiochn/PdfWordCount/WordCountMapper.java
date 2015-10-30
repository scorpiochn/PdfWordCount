package io.github.scorpiochn.PdfWordCount;

import java.io.IOException;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import io.github.scorpiochn.utils.StarDict;


public class WordCountMapper extends Mapper<LongWritable,Text, Text, IntWritable> {
	
	static Pattern pattern = Pattern.compile("[0-9]+");
	static Pattern patternL = Pattern.compile("^[^a-z]*");
	static Pattern patternR = Pattern.compile("[^a-z]*$");
	
	String dictPath ; //stardict dictpath+dictname
	StarDict dict; 
	
	SnowballStemmer stemmer = new englishStemmer();
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException{
		super.setup(context);
		
		Configuration conf = context.getConfiguration();
		dictPath = conf.get("dict.name");
		if(dictPath!=null) {
			dict = new StarDict(dictPath);
			
		}
		else {
			dict = null;
		}
	}
	
	private boolean isWord(String w) {
		Matcher matcher = pattern.matcher(w);
		if(matcher.find()) {
			return false;
		}
		return true;
	}
	
	private String WordTrimL(String w) {
		Matcher matcher = patternL.matcher(w);
		return matcher.replaceAll("");
	}
	
	private String WordTrimR(String w) {
		Matcher matcher = patternR.matcher(w);
		return matcher.replaceAll("");
	}
	
	private String WordTrim(String w) {
		return WordTrimR(WordTrimL(w));
	}
	
    @Override
    public void map(LongWritable key, Text value, Context context)
    throws IOException, InterruptedException {
        String line = value.toString();
        String [] words = line.split(" ");
        for (String w:words) 
        {
        	context.getCounter(WordCountMain.WordStats.TOKENS).increment(1);
        	String word = w.toLowerCase();
        	if(this.isWord(word)) {
        		word = this.WordTrim(word);
        		stemmer.setCurrent(word);
        		stemmer.stem();
        		word = stemmer.getCurrent();
        		if(dict!=null) {
        			System.out.println(dict.getExplanation(word));
        			if(dict.wordExist(word)) {
        				context.getCounter(WordCountMain.WordStats.WORDS).increment(1);
        				context.write(new Text(word), new IntWritable(1));
        			}
        		}
        		else {
        			context.getCounter(WordCountMain.WordStats.WORDS).increment(1);
        			context.write(new Text(word), new IntWritable(1));
        		}
        	}
        }
    }    
}
