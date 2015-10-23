package io.github.scorpiochn.utils;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

public class WordStemmer {

	public static void main(String[] args) {
		SnowballStemmer stemmer = new englishStemmer();
		
		stemmer.setCurrent("swimming");
		stemmer.stem();
		System.out.println(stemmer.getCurrent());
		stemmer.setCurrent("singing");
		stemmer.stem();
		System.out.println(stemmer.getCurrent());
	}

}
