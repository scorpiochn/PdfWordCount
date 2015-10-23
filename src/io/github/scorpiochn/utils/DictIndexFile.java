package io.github.scorpiochn.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DictIndexFile extends RandomAccessFile {
	
	public DictIndexFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);

	}
	
	public String readWord() throws IOException {
        StringBuffer input = new StringBuffer();
        int c = -1;
        boolean eol = false;

        while (!eol) {
            switch (c = read()) {
            case -1:
            case '\0':
                eol = true;
                break;
            default:
                input.append((char)c);
                break;
            }
        }

        if ((c == -1) && (input.length() == 0)) {
            return null;
        }
        return input.toString();
	}
}
