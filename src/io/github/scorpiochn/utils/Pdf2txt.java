package io.github.scorpiochn.utils;
import java.io.BufferedInputStream; 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class Pdf2txt
{
    public static void main(String[] args) throws IOException
    {
       File f = new File(args[1]);
       FileInputStream fis = new FileInputStream(f);
       BufferedInputStream bis = new BufferedInputStream(fis);
       PDFParser parser = new PDFParser(bis);
       parser.parse();
       PDDocument document = parser.getPDDocument();
       PDFTextStripper stripper = new PDFTextStripper();
       String s = stripper.getText(document);
       document.close();
       bis.close();
       File ff = new File(args[2]);
       ff.createNewFile();
       if (ff.exists())
       {
           ff.createNewFile();
       }
       FileWriter fw = new FileWriter(ff);
       BufferedWriter bw = new BufferedWriter(fw);
       bw.write(s);
       bw.close();
    }
}
