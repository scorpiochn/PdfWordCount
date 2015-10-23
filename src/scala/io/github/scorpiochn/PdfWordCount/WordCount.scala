package io.github.scorpiochn.PdfWordCount

//import io.github.scorpiochn.PdfWordCount.PdfInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat

object WordCount {
  
    def wordPattern = """[0-9]+""".r
    def patternL = """^[^a-z]*""".r
    def patternR = """[^a-z]*$""".r
  
    def isWord(w:String):Boolean = {
        if(wordPattern.findFirstIn(w).isEmpty)
            true
        else
            false
    } 
    
    def WordTrim(w:String):String = {
         val wL = patternL.replaceAllIn(w, "")
         val wR = patternR.replaceAllIn(wL, "")
         wR
    }
    
    def main (args: Array[String]) {
        val conf = new SparkConf().setAppName("Word Count")
        val sc = new SparkContext(conf)
        val fileRDD = sc.newAPIHadoopFile(args(0), classOf[PdfInputFormat],
            classOf[LongWritable], classOf[Text])
        //fileRDD.map(x=>x.split(" ").map((_,1))).flatMap(x=>x).reduceByKey(_+_).saveAsTextFile(args(1))
        //val rawWords = fileRDD.flatMap({case (x,y) => y.toString.split(" ")})
        val fileLine = fileRDD.map(x=>x._2.toString).reduce((x,y)=>x+y)
        val rawWords = fileLine.split(" ")
        val _words = rawWords.filter { w => isWord(w) }.map { x => WordTrim(x) }
        val words =sc.parallelize(_words)
        val output = words.map((_,1)).reduceByKey(_+_).map({case (x,y) => (y,x)}).sortByKey(false).map(x=>(x._2,x._1))
        output.saveAsNewAPIHadoopFile(args(1), classOf[IntWritable], classOf[Text], classOf[TextOutputFormat[IntWritable,Text]])
        sc.stop()
    }
}
