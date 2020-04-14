This app allows capture net traffic from hardware devices, filtered or entire.
Filter is applied to system properties, so that means parameter of type "spark.ip=<some ip>" during boot will initialise filtering.

Once traffic value for 5-minute term goes out of set up limits, alert message is sent to Kafka topic "alerts"
Once in 20-minute-term limits are being updated from DB, also they are updated after every insert in DB (only made by app).
New limits can be set by REST, sending parametrized POST-request to port.

To set up everything work you'll need to do several things:

1. Download Zookeeper, Kafka and Spark and unzip them;

2. Set up system variables with path to directory of programm:
ZOOKEEPER_HOME, 
KAFKA_HOME, 
SPARK_HOME 
and add "%VARIABLE%/bin" to System path.

3. Run Zookeeper server by commands:
%ZOOKEEPER_HOME%\bin\zkServer.sh conf\zoo.cfg and 
%ZOOKEEPER_HOME%\bin\zkServer.sh start

Then run Kafka server by command:
%KAFKA_HOME%\bin\windows\kafka-server-start.bat %KAFKA_HOME%\config\server.properties

4. You may need to create topics for kafka:
%KAFKA_HOME%\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic <TOPIC_NAME>
*Note that each topic should be created once.

5. Sometimes it's needed to run producer and consumer for kafka messages. Commands are:
%KAFKA_HOME%\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic <TOPIC_NAME>
%KAFKA_HOME%\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic <TOPIC_NAME>

6. After building project jar-file should be run by Spark:
%SPARK_HOME%\bin\spark-submit --class <MAIN_CLASS_REL_ADDRESS> <--conf spark.ip=...> <JAR_ABS_ADDRESS>


