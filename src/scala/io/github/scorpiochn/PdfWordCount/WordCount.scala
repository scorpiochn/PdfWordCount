package io.github.scorpiochn.PdfWordCount

import io.github.scorpiochn.PdfWordCount.PdfInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

object WordCount {
    def main (args: Array[String]) {
        val conf = new SparkConf().setAppName("Word Count")
        val sc = new SparkContext(conf)
        val fileRDD = sc.newAPIHadoopFile(args(0), classOf[PdfInputFormat],
            classOf[LongWritable], classOf[Text])
        //fileRDD.map(x=>x.split(" ").map((_,1))).flatMap(x=>x).reduceByKey(_+_).saveAsTextFile(args(1))
        fileRDD.flatMap({case (x,y)=>y.toString.split(" ")}).map((_,1)).reduceByKey(_+_).saveAsTextFile(args(1))
        sc.stop()

    }
}
