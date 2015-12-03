package io.github.scorpiochn.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

class Location {
	public int offset;
	public int size;
	public Location(int o, int s) {
		this.offset = o;
		this.size = s;
	}
}

public class StarDict {	
	/**/
	DictIndexFile index;
	DictZipFile dz;
	String dictname;
	HashMap<String, Location> words = new HashMap<String, Location>();
	public String last_error = "";
	boolean initialized = false;
	
	/**
	 * 
	 * @param dictname
	 */
	public StarDict(String dictname) {
		try {
			File dict = new File(dictname);
			if(dict.isDirectory()) {
				File ff[]=dict.listFiles();
				for(File f:ff) {
					String FileName = f.getName();
					System.out.println(FileName);
					if(FileName.indexOf('.')>=0) {
						this.dictname = dictname+File.separator+FileName.split("\\.")[0];
						this.index = new DictIndexFile(this.dictname+".idx", "r");
						this.dz = new DictZipFile(this.dictname+".dict.dz");
					}
				}
			}
		}
		catch(FileNotFoundException e) {
			last_error = e.toString();
			e.printStackTrace();
		}
		catch(Exception e) {
			last_error = e.toString();
			e.printStackTrace();
		}
		try {
			String w = this.index.readWord();
			while(w!=null) {
				int offset = this.index.readInt();
				int size = this.index.readInt();
				this.words.put(w, new Location(offset, size));
				w = this.index.readWord();
			}
		}
		catch(Exception e) {
			
		}
		
	}
	
	public boolean wordExist(String w) {
		Location l = this.words.get(w);
		if(l==null)
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @param word
	 * @return the explanation of the word
	 */
	public String getExplanation(String word) {
		Location l = this.words.get(word);
		String exp = "";
		if (l!=null) {
			//get explanation
			byte [] buffer = new byte[l.size];
			this.dz.seek(l.offset);
			try {
				this.dz.read(buffer, l.size);
			}
			catch(Exception e) {
				last_error = e.toString();
				buffer = null;
				exp = e.toString();
			}
			
			try {
				if (buffer == null) {
					exp = "Error when reading data\n"+exp;
				}
				else {
					exp = new String(buffer, "UTF8");
				}
			}
			catch(Exception e) {
				last_error = e.toString();
				e.printStackTrace();
			}
			return exp;
		}
		else {
			return "<NOTFOUND>";
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getVersion() {
		String ver = "<UNKOWN>";
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.dictname+".ifo"));
			String line = br.readLine();
			while(line != null) {
				String [] version = line.split("=");
				if (version.length == 2 && version[0].equals("version")) {
					ver = version[1];
					break;
				}
				line = br.readLine();
			}
			br.close();
		}
		catch(IOException e) {
			last_error = e.toString();
			e.printStackTrace();
		}
		return ver;
	}
	
	public int getWordNum() throws IOException {
		FileReader fr = new FileReader(this.dictname+".ifo");
		BufferedReader br = new BufferedReader(fr);
		try {
			String line = br.readLine();
			while(line != null) {
				String [] version = line.split("=");
				if (version.length == 2 && version[0].equals("wordcount")) {
					return Integer.parseInt(version[1]);
				}
				line = br.readLine();
			}
		}
		catch(IOException e) {
			last_error = e.toString();
			e.printStackTrace();
		}
		finally {
			br.close();
			fr.close();			
		}
		return 0;
	}
	
	public static void main(String[] args) {
		StarDict dict = new StarDict(args[0]);
		System.out.println(dict.getVersion());
		System.out.println(dict.getExplanation(args[1]));
	}
}