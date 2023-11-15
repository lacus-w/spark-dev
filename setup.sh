sdk install java 8.0.392-amzn

sudo chown gitpod:gitpod /opt
mkdir /opt/module

wget "https://archive.apache.org/dist/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz"
tar -xzvf spark-3.1.1-bin-hadoop3.2.tgz -C /opt/module
export SPARK_HOME="/opt/module/spark-3.1.1-bin-hadoop3.2"
export PATH=$PATH:$SPARK_HOME/bin
# test spark-submit
spark-submit --class org.apache.spark.examples.SparkPi  $SPARK_HOME/examples/jars/spark-examples_2.12-3.1.1.jar


# setup kafka
wget "http://archive.apache.org/dist/kafka/2.4.1/kafka_2.12-2.4.1.tgz"
tar -xzvf kafka_2.12-2.4.1.tgz -C /opt/module
export PATH=$PATH:/opt/module/kafka_2.12-2.4.1/bin

cd /opt/module/spark-3.1.1-bin-hadoop3.2/jars
wget "https://repo1.maven.org/maven2/org/apache/spark/spark-streaming-kafka-0-10_2.12/3.1.1/spark-streaming-kafka-0-10_2.12-3.1.1.jar" 
wget "https://repo1.maven.org/maven2/org/apache/spark/spark-token-provider-kafka-0-10_2.12/3.1.1/spark-token-provider-kafka-0-10_2.12-3.1.1.jar"
cp /opt/module/kafka_2.12-2.4.1/libs/kafka-clients-2.4.1.jar .
ls | grep "kafka"
# ssc.checkpoint("file:///opt/exp18/kafka/checkpoint")
mkdir -p /opt/exp18/kafka/checkpoint
# producer
cd /workspace/spark-dev/kafka-app/producer
mvn clean dependency:copy-dependencies package
spark-submit --class com.esse.ll.KafkaWordProducer ./target/kproducer-1.0-SNAPSHOT.jar localhost:63342  wordsender  3  5
# consumer
cd /workspace/spark-dev/kafka-app/app
mvn clean dependency:copy-dependencies package
spark-submit --class com.esse.ll.KafkaWordCount ./target/kapp-1.0-SNAPSHOT.jar


wget "http://archive.apache.org/dist/hadoop/common/hadoop-3.1.3/hadoop-3.1.3.tar.gz"
tar -xzvf hadoop-3.1.3.tar.gz -C /opt/module
export HADOOP_HOME="/opt/module/hadoop-3.1.3"
export PATH=$PATH:$HADOOP_HOME/bin
vi $HADOOP_HOME/etc/hadoop/core-site.xml
    #  <property>
    #      <name>fs.defaultFS</name>
    #      <value>hdfs://localhost:9000</value>
    #  </property>

wget "http://archive.apache.org/dist/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz"
tar -xzvf apache-hive-3.1.2-bin.tar.gz -C /opt/module
export HIVE_HOME="/opt/module/apache-hive-3.1.2-bin"
export PATH=$PATH:$HIVE_HOME/bin

