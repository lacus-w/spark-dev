/*
    实时统计每个单词累计出现的次数，并将结果保存到本地文件（每10s统计1次）。
*/
val updateFunc = (values: Seq[Int], state: Option[Int]) => {
    val currentCount = values.foldLeft(0)(_ + _)
    val previousCount = state.getOrElse(0)
    Some(currentCount + previousCount)
} //定义状态更新函数
sc.setLogLevel("ERROR")
import org.apache.spark.streaming._
val ssc = new StreamingContext(sc, Seconds(10))
ssc.checkpoint("file:///opt/save/stateful/")//设置检查点，检查点具有容错机制
val lines = ssc.textFileStream("file:///opt/test/exp19")
val words = lines.flatMap(_.split(" "))
val wordDstream = words.map(x => (x, 1))
val stateDstream = wordDstream.updateStateByKey[Int](updateFunc)
stateDstream.print()
// 输出到本地文件：
stateDstream.saveAsTextFiles("file:///opt/save/dstreamoutput/output")
ssc.start()
ssc.awaitTermination()