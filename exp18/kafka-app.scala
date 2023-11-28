import org.apache.spark._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

object KafkaWordCount{
  def main(args:Array[String]) : Unit = {
    val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("ERROR")
    val ssc = new StreamingContext(sc,Seconds(10))
    ssc.checkpoint("file:///home/hadoop/exp18/kafka/checkpoint") //设置检查点，如果存放在HDFS上面，则写成类似ssc.checkpoint("/user/hadoop/checkpoint")这种形式，但是，要启动Hadoop
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )
    val topics = Array("wordsender")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    stream.foreachRDD(rdd => {
      val offsetRange = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      val maped: RDD[(String, String)] = rdd.map(record => (record.key,record.value))
      val lines = maped.map(_._2)
      val words = lines.flatMap(_.split(" "))
      val pair = words.map(x => (x,1))
      val wordCounts = pair.reduceByKey(_+_)
      wordCounts.foreach(println)
    })
    ssc.start
    ssc.awaitTermination
  }
}
