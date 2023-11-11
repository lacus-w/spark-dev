sdk install java 8.0.392-amzn

sudo chown gitpod:gitpod /opt
mkdir /opt/module

wget "https://archive.apache.org/dist/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz"
tar -xzvf spark-3.1.1-bin-hadoop3.2.tgz -C /opt/module
export SPARK_HOME="/opt/module/spark-3.1.1-bin-hadoop3.2"
export PATH=$PATH:$SPARK_HOME/bin

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

