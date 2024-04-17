# Kafka Integration for Enterprise Application

## Description
This project demonstrates the integration of systems using Apache Kafka and Kafka Streams.

## Objectives
- Create simple asynchronous and message-oriented applications.
- Utilize Kafka Streams for real-time data processing.

## Technologies Used
- Apache Kafka
- Kafka Streams
- MySQL Database
- Spring Boot
- Java Persistence API (JPA)
- RESTful API

## Setup Instructions
1. **MySQL Database Setup:**
   - Create a database named 'meta3' in MySQL.

2. **Kafka Configuration:**
   - Place the Kafka Connect configuration files (sink and source) in the 'config' directory of Kafka.

3. **Start Zookeeper and Kafka Server:**
   - Ensure that Zookeeper and Kafka server are running.

4. **Topic Setup:**
   - Delete any previously created topics.
   - Create the following topics: 'topicCredit', 'topicPayment', 'topicResult', and 'topicDB'.

5. **Configure Kafka Connect:**
   - Execute the `bin/connect-standalone.sh` script to establish the connection to the database. Specify the filenames for the sink and source configurations.

6. **SpringBoot Application Setup:**
   - Run the 'ismeta3rest' project to allow SpringBoot + JPA to create the necessary tables in the database.

7. **Run Kafka Streams:**
   - Run the 'KafkaStreams.java' file located in the 'ismeta3' project.

8. **Run Kafka Client (Manual):**
   - Run the 'KafkaClientManual.java' file located in the 'ismeta3' project. This will produce a credit/payment for a client.

9. **REST API Testing:**
   - Run the 'ismeta3RestTest' project to test the REST layer and visualize the results.

## Folder Structure
- `config_sink_e_source`: Contains Kafka Connect configuration files.
- `projetos_netbeans`: Contains NetBeans projects.
