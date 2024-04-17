
package com.exemplo;

import com.exemplo.models.Bill;
import com.exemplo.models.Client;
import com.exemplo.models.Currency;
import com.exemplo.models.Manager;
import com.exemplo.utils.TopicNames;
import com.exemplo.utils.Utils;
import com.exemplo.models.Transaction;
import com.google.gson.Gson;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;

public class KakfaStreams {
    
    private static final Map<Integer, Client> CLIENTS = new HashMap<>(); 
    private static final Map<Integer, Manager> MANAGERS = new HashMap<>();
    private static final Map<Integer, Currency> CURRENCIES = new HashMap<>();
    
    /* ---- Tempo para calcular BILL (Tumbling Window - segundos) ----*/
    private static final Integer BILL_TIME = 180;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        
        createTopicCredit("credit", Serdes.String(), Serdes.ByteArray());
        createTopicPayment("payment", Serdes.String(), Serdes.ByteArray());
        createTopicDatabase("db", Serdes.String(), Serdes.String());
        
        /* --- Credito 1 mes --- */
        createTopicCreditTumbling("creditTumbling", Serdes.String(), Serdes.ByteArray());
        /* --- Pagamento 2 meses --- */
        //createTopicPayment("paymentTumbling", Serdes.String(), Serdes.ByteArray());
        
        System.out.println("[Kafka Streams] Estou pronto...");
    }
    
    /* -------------- CRIAM os Topicos -------------- */
    public static <K,V> void createTopicCredit(String appId, Serde<K> keyType, Serde<byte[]> valueType) {
        
        Properties props = Utils.getProperties(appId, keyType, valueType);
        String topicIn = TopicNames.CREDIT_TOPIC;
        
        StreamsBuilder builder = new StreamsBuilder(); 
        KStream<String, byte[]> lines = builder.stream(topicIn);
        
        // Enviar DB (positivo)
        lines.mapValues(v -> addCredit(v));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        System.out.println("[Kafka Streams] Reading topic " + topicIn);
    }
    public static <K,V> void createTopicCreditTumbling(String appId, Serde<K> keyType, Serde<byte[]> valueType) {
        
        Properties props = Utils.getProperties(appId, keyType, valueType);
        String topicIn = TopicNames.CREDIT_TOPIC;
        
        /* ------ Duracao 1 mes = 3 minutos ------- */
        Duration windowSize = Duration.ofSeconds(BILL_TIME);
        TimeWindows tumblingWindow = TimeWindows.of(windowSize);
        
        StreamsBuilder builder = new StreamsBuilder(); 
        KStream<String, byte[]> lines = builder.stream(topicIn);
        KTable<Windowed<String>, byte[]> creditPerClient = 
                lines.mapValues((k,v) -> addBill(k,v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.ByteArray()))
                .windowedBy(tumblingWindow)
                .reduce((v1,v2)->sumAllBills(v1,v2));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        System.out.println("[Kafka Streams] Reading topic " + topicIn);
    }
    public static <K,V> void createTopicPaymentTumbling(String appId, Serde<K> keyType, Serde<byte[]> valueType) {
        
        Properties props = Utils.getProperties(appId, keyType, valueType);
        String topicIn = TopicNames.PAYMENT_TOPIC;
        
        /* ------ Duracao 2 meses = 6 minutos ------- */
        // LEFT-JOIN
        Duration windowSize = Duration.ofSeconds(BILL_TIME * 2);
        TimeWindows tumblingWindow = TimeWindows.of(windowSize);
        
        /*StreamsBuilder builder = new StreamsBuilder(); 
        KStream<String, byte[]> lines = builder.stream(topicIn);
        KTable<Windowed<String>, byte[]> paymentPerClient = 
                lines.mapValues((k,v) -> addBill2(k,v))
                .leftJoin(paymentPerClient, v)
                .windowedBy(tumblingWindow)
                .reduce((v1,v2)->sumAllBills2(v1,v2));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();*/
        
        System.out.println("[Kafka Streams] Reading topic " + topicIn);
    }
    public static <K,V> void createTopicPayment(String appId, Serde<K> keyType, Serde<byte[]> valueType) {
        
        Properties props = Utils.getProperties(appId, keyType, valueType);
        String topicIn = TopicNames.PAYMENT_TOPIC;
        
        StreamsBuilder builder = new StreamsBuilder(); 
        KStream<String, byte[]> lines = builder.stream(topicIn);
        
        // Enviar DB (positivo)
        lines.mapValues(v -> addPayment(v));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        System.out.println("[Kafka Streams] Reading topic " + topicIn);
    }
    public static <K,V> void createTopicDatabase(String appId, Serde<K> keyType, Serde<String> valueType) {
        
        Properties props = Utils.getProperties(appId, keyType, valueType);
        String topicIn = TopicNames.DB_TOPIC;
        
        StreamsBuilder builder = new StreamsBuilder(); 
        KStream<String, String> lines = builder.stream(topicIn);
        
        lines.mapValues((k,v) -> registerInfo(k,v));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        System.out.println("[Kafka Streams] Reading topic " + topicIn);
    }

    /* -------------- LOGICA dos Topicos -------------- */
    private synchronized static byte[] addCredit(byte[] transaction) {
        
        System.out.println("\n[Credito]");
        Transaction msg = (Transaction) SerializationUtils.deserialize(transaction);        
        
        int clientId = msg.getClientId();
        int currencyId = msg.getCurrencyId();
        double amount = msg.getAmount();

        // logica db
        if (CLIENTS.containsKey(clientId)) {
            if (CURRENCIES.containsKey(currencyId)) {
                
                Client client = CLIENTS.get(clientId);
                Currency currency = CURRENCIES.get(currencyId);
                
                double oldCredits = client.getCredits();
                double newCredits = oldCredits + (amount * currency.getExchange_rate());
                
                double oldBalance = client.getBalance();
                double newBalance = oldBalance - (amount * currency.getExchange_rate());
                
                client.setBalance(newBalance);
                client.setCredits(newCredits);
                try {
                    updateClientDB(client);
                    System.out.println("[Credito] Cliente actualizado");
                } catch (IOException ex) {
                    client.setBalance(oldBalance);
                    client.setCredits(oldCredits);
                    System.out.println(ex);
                }
            } else {
                System.out.println("Moeda nao existe");
            }
        } 
        else {
            System.out.println("Cliente nao existe");
        }
        
        return transaction;
    }
    private synchronized static byte[] addPayment(byte[] transaction) {
        
        System.out.println("\n[Pagamento]");
        Transaction msg = (Transaction) SerializationUtils.deserialize(transaction);
        
        int clientId = msg.getClientId();
        int currencyId = msg.getCurrencyId();
        double amount = msg.getAmount();

        // logica db
        if (CLIENTS.containsKey(clientId)) {
            if (CURRENCIES.containsKey(currencyId)) {
                
                Client client = CLIENTS.get(clientId);
                Currency currency = CURRENCIES.get(currencyId);
                
                double oldPayments = client.getPayments();
                double newPayments = oldPayments + (amount * currency.getExchange_rate());
                
                double oldBalance = client.getBalance();
                double newBalance = oldBalance + (amount * currency.getExchange_rate());
                
                client.setBalance(newBalance);
                client.setPayments(newPayments);
                
                try {
                    updateClientDB(client);
                    System.out.println("[Pagamento] Cliente actualizado");
                } catch (IOException ex) {
                    client.setBalance(oldBalance);
                    client.setPayments(oldPayments);
                    System.out.println(ex);
                }
            } else {
                System.out.println("Moeda nao existe");
            }
        }
        else {
            System.out.println("Cliente nao existe");
        }
        
        return transaction;
    }
    private synchronized static byte[] registerInfo(String key, String value) {
        
        String tableId = key;
        
        switch (tableId) {
            case "\"0\"":
                Client newClient = new Gson().fromJson(value, Client.class);
                CLIENTS.put(newClient.getId(), newClient);                
                break;
            case "\"1\"":
                Manager newManager = new Gson().fromJson(value, Manager.class);
                MANAGERS.put(newManager.getId(), newManager);               
                break;
            case "\"2\"":
                Currency newCurrency = new Gson().fromJson(value, Currency.class);
                CURRENCIES.put(newCurrency.getId(), newCurrency);
                break;
            default:
                break;
        }
        
        String data = new StringBuilder()
                .append("\n[DB Info] Dados:\n")
                .append(CLIENTS).append("\n")
                .append(MANAGERS).append("\n")
                .append(CURRENCIES)
                .toString();
        System.out.println(data);
        
        return "".getBytes();
    }
    
    /* -------------- CALCULA Bills Creditos 1 mes -------------- */
    private synchronized static byte[] addBill(String key, byte[] obj) {
        
        System.out.println("\n[Credits - Tumbling Window]");
        Transaction msg = (Transaction) SerializationUtils.deserialize(obj);        
        
        int clientId = msg.getClientId();
        int currencyId = msg.getCurrencyId();
        double amount = msg.getAmount();

        // logica db
        if (CLIENTS.containsKey(clientId) && CURRENCIES.containsKey(currencyId)) {
                
                Client client = CLIENTS.get(clientId);
                Currency currency = CURRENCIES.get(currencyId);
                
                double billAmount = amount * currency.getExchange_rate();
                
                Bill bill = new Bill(clientId, billAmount);
                try {
                    client.setCredits_month(billAmount);
                    updateClientDB(client);
                    
                    System.out.println("[Credits - Tumbling Window] - added new bill: (client,credit) [" 
                            + clientId + "," + billAmount + "]");
                    return SerializationUtils.serialize(bill);
                } catch (IOException ex) {
                    System.err.println("[Credits - Tumbling Window] - Problema ao gravar na BD");
                    return SerializationUtils.serialize(new Bill(-1,0));
                }
        } 
        else {
            System.err.println("[Credits - Tumbling Window] - Cliente ou Moeda nao existem");
            return SerializationUtils.serialize(new Bill(-1,0));
        }
    }
    private static byte[] sumAllBills(byte[] v1, byte[] v2) {
        
        System.out.println("\n[Credits - Tumbling Window] - SAVING BILL TO DATABASE");
        
        Bill b1 = SerializationUtils.deserialize(v1);
        Bill b2 = SerializationUtils.deserialize(v2);
        
        int clientId = b1.getClientId();
        double bill = b1.getBillAmount() + b2.getBillAmount();
        
        if (CLIENTS.containsKey(clientId)) {
            Client client = CLIENTS.get(clientId);

            client.setCredits_month(bill);
            try {
                updateClientDB(client);
                System.out.println("[Credits - Tumbling Window] - saved DB (clientId,billMonth) [" + clientId + "," + bill + "]");
            } catch (IOException ex) {
                System.err.println("[Credits - Tumbling Window] Problema ao gravar para BD");
            }
            return SerializationUtils.serialize(new Bill(clientId, bill));
        }
        else {
            System.err.println("[sumBills] Cliente nao existe");
            return SerializationUtils.serialize(new Bill(-1, 0));
        }
    }
    
        /* -------------- CALCULA Pagamentos 2 meses -------------- */
    private synchronized static byte[] addBill2(String key, byte[] obj) {
        
        System.out.println("\n[Payments - Tumbling Window]");
        Transaction msg = (Transaction) SerializationUtils.deserialize(obj);        
        
        int clientId = msg.getClientId();
        int currencyId = msg.getCurrencyId();
        double amount = msg.getAmount();

        // logica db
        if (CLIENTS.containsKey(clientId) && CURRENCIES.containsKey(currencyId)) {
                
                Client client = CLIENTS.get(clientId);
                Currency currency = CURRENCIES.get(currencyId);
                
                double billAmount = amount * currency.getExchange_rate();
                
                Bill bill = new Bill(clientId, billAmount);
                try {
                    client.setCredits_month(billAmount);
                    updateClientDB(client);
                    
                    System.out.println("[Payments - Tumbling Window] - added new bill: (client,credit) [" 
                            + clientId + "," + billAmount + "]");
                    return SerializationUtils.serialize(bill);
                } catch (IOException ex) {
                    System.err.println("[Payments - Tumbling Window] - Problema ao gravar na BD");
                    return SerializationUtils.serialize(new Bill(-1,0));
                }
        } 
        else {
            System.err.println("[Payments - Tumbling Window] - Cliente ou Moeda nao existem");
            return SerializationUtils.serialize(new Bill(-1,0));
        }
    }
    private static byte[] sumAllBills2(byte[] v1, byte[] v2) {
        
        System.out.println("\n[Payments - Tumbling Window] - SAVING BILL TO DATABASE");
        
        Bill b1 = SerializationUtils.deserialize(v1);
        Bill b2 = SerializationUtils.deserialize(v2);
        
        int clientId = b1.getClientId();
        double bill = b1.getBillAmount() + b2.getBillAmount();
        
        if (CLIENTS.containsKey(clientId)) {
            Client client = CLIENTS.get(clientId);

            client.setCredits_month(bill);
            try {
                updateClientDB(client);
                System.out.println("[Payments - Tumbling Window] - saved DB (clientId,billMonth) [" + clientId + "," + bill + "]");
            } catch (IOException ex) {
                System.err.println("[Payments - Tumbling Window] Problema ao gravar para BD");
            }
            return SerializationUtils.serialize(new Bill(clientId, bill));
        }
        else {
            System.err.println("[Payments] Cliente nao existe");
            return SerializationUtils.serialize(new Bill(-1, 0));
        }
    }
    
    /* -------------- ACTUALIZA Clientes -------------- */
    private synchronized static void updateClientDB(Client cli) throws IOException {
        
        String topic = TopicNames.RESULT_TOPIC;
        
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        String value = Utils.convertToJsonAvro(cli);
        System.out.println("[updateClientDB] Json avro = " + value);
        
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            String key = String.valueOf(cli.getId());
            producer.send(new ProducerRecord<>(topic, key, value));
        }
    }
}
