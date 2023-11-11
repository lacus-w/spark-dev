import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
object SparkHiveTest extends App {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkHiveTest")
      .enableHiveSupport()
      .config("spark.sql.warehouse.dir", "hdfs://master:9000/usr/hive/warehouse")
      .config("hive.metastore.uris", "thrift://master:9083")
      .getOrCreate();

    spark.conf.set("hive.exec.dynamic.partition.mode", "nonstrict")
    spark.sparkContext.setLogLevel("OFF")
    
    spark.read
      .format("jdbc")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("url", "jdbc:mysql://localhost:3306/shtd_store")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", "user_info")
      .load()
      .createTempView("data")

    spark.sql(
      """
      |select * from data
      |""".stripMargin
    ).show
/*
select a.* 
from ods as a, ods as b 
where a.id = b.id
and greatest(a.operate_time, a.create_time) > greatest(b.operate_time, b.create_time)
*/
    val query = "select a.* from ods as a, ods as b where a.id = b.id and greatest(a.operate_time, a.create_time) > greatest(b.operate_time, b.create_time)"

    val df = spark.sql(query)
    df.show

    val df2 = df.withColumn("etl_date", date_format(date_sub(current_date, 1), "yyyyMMdd"))
    df2.show

    df2.write.format("hive").mode("append")
      .partitionBy("etl_date") //分区字段
      .saveAsTable("ods.user_info")

    spark.sql("show partitions ods.user_info").show  
}