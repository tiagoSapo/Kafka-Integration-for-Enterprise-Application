
package com.exemplo;

import com.exemplo.models.Transaction;
import com.exemplo.utils.TopicNames;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/*
*
*   IMPORTANTE: Utilizar este KAFKA-CLIENT para produzir as mensagens de creditos e/ou pagamento
*
*/
public class KafkaClientManual {
    
    /* OperationType = topico de destino, Credito ou Pagamento */
    public enum OperationType {
        ADD_CREDITS, ADD_PAYMENTS
    }
    
    public static void main(String args[]) throws Exception { 
        
        /* ID do cliente na BD */
        int clientId = 1;
        
        /* ID da moeda na BD */
        int currencyId = 3;
        
        /* ID da quantia */
        double amount = 100.0;
        
        /* Enviar KafkaStreams, para o topico 'OperationType' */
        sendMsg(OperationType.ADD_CREDITS, clientId, currencyId, amount);
    }
    
    public static void sendMsg(OperationType type, int clientId, int currencyId, double amount) 
            throws JsonMappingException, IOException {
        
        String topicName;
        
        if (type == OperationType.ADD_CREDITS)
            topicName = TopicNames.CREDIT_TOPIC;
        else
            topicName = TopicNames.PAYMENT_TOPIC;
        
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        
        Transaction trans = new Transaction();
        trans.setClientId(clientId);
        trans.setAmount(amount);
        trans.setCurrencyId(currencyId);
        
        byte value[] = SerializationUtils.serialize(trans);  
        System.out.println(trans);
        
        try (Producer<String, byte[]> producer = new KafkaProducer<>(props)) {
            producer.send(new ProducerRecord<>(topicName, String.valueOf(clientId), value));
        }
        
    }
}