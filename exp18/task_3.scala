val updateFunc = (values: Seq[Int], state: Option[Int]) => {
    val currentCount = values.foldLeft(0)(_ + _)
    val previousCount = state.getOrElse(0)
    Some(currentCount + previousCount)
} //定义状态更新函数
sc.setLogLevel("ERROR")
import org.apache.spark.streaming._
val ssc = new StreamingContext(sc, Seconds(5))
ssc.checkpoint("file:///opt/test/exp18/stateful/")//设置检查点，检查点具有容错机制
val lines = ssc.socketTextStream("localhost", 9999)
val words = lines.flatMap(_.split(" "))
val wordDstream = words.map(x => (x, 1))
val stateDstream = wordDstream.updateStateByKey[Int](updateFunc)
stateDstream.print()
ssc.start()
ssc.awaitTermination()