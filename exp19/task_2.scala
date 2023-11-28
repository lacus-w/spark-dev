/*
    实时统计最近1min内每个单词的出现次数（每10s统计一次）。
*/

import org.apache.spark.streaming._
val ssc = new StreamingContext(sc, Seconds(10))
val lines = ssc.textFileStream("file:///opt/test/exp19")
val words = lines.flatMap(_.split(" "))
/* 
    时间间隔为60秒，所以每10秒统计一次的时候，都要把前面50秒的数据也算在内
    使用到滑动窗口Window，参考课本189-190页
*/
val wordCounts = words.map(x => (x, 1)).
    reduceByKeyAndWindow((v1: Int, v2: Int) => v1 + v2, Seconds(60), Seconds(10))
wordCounts.print()
ssc.start()
ssc.awaitTermination()