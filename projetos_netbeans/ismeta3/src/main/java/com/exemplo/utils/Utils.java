
package com.exemplo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import kafka.utils.Json;
import kafka.utils.json.JsonValue;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class Utils {
    
    public static <K,V> Properties getProperties(String applicationId, Serde<K> key, Serde<V> value) {
        
        Properties props = new Properties();
        
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, key.getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, value.getClass());
        
        return props;
    }
    
    // Obtem a informacao da bd sem o schema
    public static JsonValue getPayloadKafka(String str) {
        return Json.parseFull(str).getOrElse(null);
    }
    
    public static <T> GenericRecord pojoToRecord(T model) throws IOException {
        Schema schema = ReflectData.get().getSchema(model.getClass());
 
        ReflectDatumWriter<T> datumWriter = new ReflectDatumWriter<>(schema);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        datumWriter.write(model, encoder);
        encoder.flush();
 
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(outputStream.toByteArray(), null);
 
        return datumReader.read(null, decoder);
    }
    
    public static <T> String convertToJsonAvro(T object) throws JsonProcessingException, IOException {
        
        Schema schema = ReflectData.get().getSchema(object.getClass());
        String json = new Gson().toJson(object);
        
        String avro = new StringBuilder()
                .append("{\"schema\": ")
                .append(schema.toString().replace("\"name\":", "\"field\":"))
                .append(", \"payload\": ")
                .append(new ObjectMapper().readTree(json))
                .append("}").toString();
        avro = avro.replace("record", "struct");
        avro = avro.replace("int", "int32");
        
        return avro;
    }
}
