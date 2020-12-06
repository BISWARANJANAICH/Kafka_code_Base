# Kafka_code_Base
1) To check kafka installed correctly (start in separate prompt)
  goto >  D:\KAFKA\kafka_2.13-2.6.0\bin\windows
  run > kafka-topics.bat
  
2) To start zookeeper (start in separate prompt)
   goto inside config location
  D:\KAFKA\kafka_2.13-2.6.0\config>zookeeper-server-start.bat zookeeper.properties 

3) to start kafka server-start (new cmd prompt)
  D:\KAFKA\kafka_2.13-2.6.0\config>kafka-server-start server.properties  
  
4) use kafka in a new command prompt  
 create a topic
 D:\KAFKA\kafka_2.13-2.6.0>kafka-topics --zookeeper localhost:2181 --topic first_topic --create --partitions 3 --replication-factor 1
 
5) To see lict of topics created
  D:\KAFKA\kafka_2.13-2.6.0>kafka-topics --zookeeper localhost:2181 --list

6) Describe a topic.
  D:\KAFKA\kafka_2.13-2.6.0>kafka-topics --zookeeper localhost:2181 --topic first_topic --describe

7) To create a console producer
  D:\KAFKA\kafka_2.13-2.6.0>kafka-console-producer --broker-list localhost:9092 --topic first_topic  
  
8_) Consume message at consumer side
    D:\KAFKA\kafka_2.13-2.6.0>kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic  
