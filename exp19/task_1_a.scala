import java.io.PrintWriter
val r = scala.util.Random

def random_line(n: Int): String = {
  var a_line = ""
  for (i <- 1 to n) {
    val x_length = r.nextInt(10) + 1 // 单词的长度随机，范围是1到10
    val a_word = r.alphanumeric.filter(_.isLower).take(x_length).mkString
    a_line += a_word + " "
  }
  a_line
}

def random_file(path: String, i: Int): Unit = {
  val a_file = new PrintWriter(path + "/" + i)
  val n = r.nextInt(10) + 1
  val a_line = random_line(n)
  a_file.print(a_line)
  a_file.close
}

var x = r.nextInt(10)
val x_path = "/opt/test/exp19"
var i = 1 //文件的编号
while(true) { // 持续生成文件，直到CTRL-C终止程序
    random_file(x_path, i)
    i += 1
    print("生成的文件是:" + x_path + '/' + i)
    println(", 时间间隔是:" + x)
    Thread.sleep( x*1000 ) // 每次生成文件后，程序暂停x秒
    x = r.nextInt(10) 
}
