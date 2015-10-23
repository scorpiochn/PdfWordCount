# PdfWordCount
WordCount for PDF files


# Project Structure


    .
    ├── lib
    │   ├── fontbox-1.8.10.jar
    │   ├── jempbox-1.8.10.jar
    │   ├── pdfbox-1.8.10.jar
    │   ├── preflight-1.8.10.jar
    │   ├── stemmer.jar        
    │   └── xmpbox-1.8.10.jar
    ├── README.md
    └── src
        ├── io
        │   └── github
        │       └── scorpiochn
        │           ├── PdfWordCount
        │           │   ├── PdfInputFormat.java
        │           │   ├── PdfRecordReader.java
        │           │   ├── WordCountMain.java
        │           │   ├── WordCountMapper.java
        │           │   └── WordCountReducer.java
        │           └── utils
        │               └── Pdf2txt.java
        └── scala
            └── io
                └── github
                    └── scorpiochn
                        └── PdfWordCount
                            └── WordCount.scala


# Dependecies
   * [PDFBox](http://pdfbox.apache.org/)
   * [Snowball stemmer](http://snowball.tartarus.org/)
   * [stardict](http://abloz.com/huzheng/stardict-dic/zh_CN/)


# Run

   * Hadoop
    
    hadoop jar __$JAVAWCJAR__  io.github.scorpiochn.PdfWordCount.WordCountMain _INPUTPATH_  _OUTPUTPATH_

   * Spark 
   
    spark-submit --jars __$JAVAWCJAR__,$PDFBOXPATH/fontbox-1.8.10.jar,$PDFBOXPATH/preflight-1.8.10.jar,$PDFBOXPATH/jempbox-1.8.10.jar,$PDFBOXPATH/xmpbox-1.8.10.jar,$PDFBOXPATH/pdfbox-1.8.10.jar --class io.github.scorpiochn.PdfWordCount.WordCount __$SCALAWCJAR__ _INPUTPATH_  _OUTPUTPATH_



