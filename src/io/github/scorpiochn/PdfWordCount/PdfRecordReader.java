package io.github.scorpiochn.PdfWordCount;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class PdfRecordReader extends RecordReader<LongWritable, Text> {
    private long start;
    private long pos;
    private long end;
    private FSDataInputStream fileIn;
    private LongWritable key;
    private Text value;
    private String txt;
    BufferedReader reader;

    @Override
    public void close() throws IOException {
    	if(reader!=null)
    	{
    		reader.close();
    	}
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        if (start == end)
        {
            return 0.0f;
        }
        else
        {
            return Math.min(1.0f, (pos - start) / (float)(end - start));
        }
    }

    @Override
    public void initialize(InputSplit input, TaskAttemptContext context)
            throws IOException, InterruptedException {
        FileSplit split = (FileSplit) input;
        Path file = split.getPath();
        FileSystem fs = file.getFileSystem(context.getConfiguration());
        
        start = split.getStart();
        end = start + split.getLength();
        
        fileIn = fs.open(file);
        BufferedInputStream bis = new BufferedInputStream(fileIn);
        PDFParser parser = new PDFParser(bis);
        parser.parse();
        PDDocument document = parser.getPDDocument();
        PDFTextStripper stripper = new PDFTextStripper();
        txt = stripper.getText(document);
        document.close();
        bis.close();
        fileIn.close();
        reader = new BufferedReader(new StringReader(txt));
        
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (key == null)
        {
            key = new LongWritable();
        }
        key.set(pos);
        if (value == null)
        {
            value = new Text();
        }
        if (pos < end) 
        {
            String t=reader.readLine();
            if(t!=null) 
            {
                value.set(t);
                pos+=value.getLength();
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

}
