
package com.exemplo.utils;

public interface TopicNames {
    /*
        CLIENT -> KAFKA STREAMS
    */
    public static final String CREDIT_TOPIC = "topicCredit";
    public static  final String PAYMENT_TOPIC = "topicPayment";
    
    /*
        KAFKA STREAMS -> DATABASE
     */
    public static final String RESULT_TOPIC = "topicResult";
    
    /*
        DATABASE-> KAFKA STREAMS AND CLIENT
     */
    public static final String DB_TOPIC = "topicDB";
    
}
