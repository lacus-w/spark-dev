/*
    实时统计每10s新出现的单词数量。
*/

import org.apache.spark.streaming._
val ssc = new StreamingContext(sc, Seconds(10))
val lines = ssc.textFileStream("file:///opt/test/exp19")
// 分别得到两类统计结果，words是所有单词，d_words是去重后的所有单词：
val words = lines.flatMap(_.split(" "))
val d_words = words.transform { rdd => rdd.distinct } // 转为rdd后可以使用distinct去重
words.count.foreachRDD(rdd => println("10s新出现的单词数量是:" + rdd.first))
d_words.count.foreachRDD(rdd => println("10s新出现的单词去重后数量是:" + rdd.first))
ssc.start()
ssc.awaitTermination()

/*
    参考：
    1. 课本176页
    2. https://spark.apache.org/docs/latest/streaming-programming-guide.html#transformations-on-dstreams
*/