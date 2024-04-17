
package com.exemplo;

import com.exemplo.models.Client;
import com.exemplo.models.Currency;
import com.exemplo.models.Manager;
import com.exemplo.models.Transaction;
import com.exemplo.utils.TopicNames;
import com.exemplo.utils.Utils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
/*
*
*   IMPORTANTE: Utilizar preferencialmente o KAFKA-CLIENT manual -> KafkaClientManual
*   (dado que e' mais facil a verificacao dos dados)
*
*   (ESTE KAFKA-CLIENT ESCOLHE DE FORMA ALEATORIA OS CLIENTES E O TIPO DE OPERACAO (CREDITO/PAGAMENTOS))    
*
*/
public class KafkaClientAutomatico {
    
    private static final Map<Integer, Client> CLIENTS = new HashMap<>(); 
    private static final Map<Integer, Currency> CURRENCIES = new HashMap<>();
    
    public enum OperationType {
        CREDITS, PAYMENTS
    }
    
    public static void main(String[] args) throws Exception { 
        listenTopicDatabase("dbCliente", Serdes.String(), Serdes.String());
        
        //int nTimes = 2;
        double amount = 100.0;
        
        while (true) {
            try {
                Thread.sleep(2000);
                OperationType type;
                boolean typeValue; // true -> Creditos escolhido, false - Pagamentos escolhido
                
                List<Integer> keysAsArray = new ArrayList<>(CLIENTS.keySet());
                Random r = new Random();
                Client cli = CLIENTS.get(keysAsArray.get(r.nextInt(keysAsArray.size())));

                keysAsArray = new ArrayList<>(CURRENCIES.keySet());
                r = new Random();
                Currency cur = CURRENCIES.get(keysAsArray.get(r.nextInt(keysAsArray.size())));

                typeValue = new Random().nextBoolean();
                if (typeValue) {
                    type = OperationType.CREDITS;
                } else {
                    type = OperationType.PAYMENTS;
                }
                
                String msg = new StringBuilder()
                        .append("\nEnviado para o Kafka Streams: ")
                        .append(type).append(" ")
                        .append(cli.getName()).append(" ")
                        .append(cur.getName()).toString();
                System.out.println(msg);
                
                sendMsg(OperationType.CREDITS, cli.getId(), cur.getId(), amount);
                
            } catch (IllegalArgumentException ex) {
                System.err.println("Nao ha clientes e ou moedas");
            }
        }
    }
    
    public static void sendMsg(OperationType topic, int clientId, int currencyId, double amount) 
            throws JsonMappingException, IOException {
        
        String topicName;
        
        if (topic == OperationType.CREDITS)
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
    
    public static <K,V> void listenTopicDatabase(String appId, Serde<K> keyType, Serde<String> valueType) {
        
        Properties props = Utils.getProperties(appId, keyType, valueType);
        String topicIn = TopicNames.DB_TOPIC;
        
        StreamsBuilder builder = new StreamsBuilder(); 
        KStream<String, String> lines = builder.stream(topicIn);
        
        lines.mapValues((k,v) -> registerInfo(k,v));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        System.out.println("[Kafka Streams] Reading topic " + topicIn);
    }
    private synchronized static byte[] registerInfo(String key, String value) {
        
        String tableId = key;
        
        switch (tableId) {
            case "\"0\"":
                Client newClient = new Gson().fromJson(value, Client.class);
                CLIENTS.put(newClient.getId(), newClient);                
                break;
            case "\"2\"":
                Currency newCurrency = new Gson().fromJson(value, Currency.class);
                CURRENCIES.put(newCurrency.getId(), newCurrency);
                break;
            default:
                break;
        }
        
        /* Descomentar PARA DEBUG */
        /*String data = new StringBuilder()
                .append("\n[DB Info] Dados:\n")
                .append(CLIENTS).append("\n")
                .append(CURRENCIES)
                .toString();
        System.out.println(data);*/
        
        return "".getBytes();
    }
}