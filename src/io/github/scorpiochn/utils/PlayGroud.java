package io.github.scorpiochn.utils;

import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;

public class PlayGroud {

	public static void main(String[] args) throws MalformedURIException {
		
		URI uri = new URI("file:///home");
		System.out.println(uri.getPath());
	}

}
