package com.app.Booking.Service.config;

import com.app.commons.constant.EventsConstant;
import com.app.commons.createdEvents.PaymentCreatedEvent;
import com.app.commons.createdEvents.SeatReservationCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.mapping.DefaultJacksonJavaTypeMapper;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic createNewTopic() {
        return new NewTopic(EventsConstant.BOOKING_EVENT_TOPIC,3,(short)1);
    }

    @Bean
    public Map<String,Object> producerConfig() {
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JacksonJsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String,Object> producerFactory() {
        DefaultJacksonJavaTypeMapper mapper = new DefaultJacksonJavaTypeMapper();

        Map<String, Class<?>> idMapper = new HashMap<>();
        idMapper.put("seat_event", SeatReservationCreatedEvent.class);
        idMapper.put("payment_event", PaymentCreatedEvent.class);

        mapper.setIdClassMapping(idMapper);

        StringSerializer serializer = new StringSerializer();


        JacksonJsonSerializer<Object> jsonSerializer = new JacksonJsonSerializer<>();
        jsonSerializer.setTypeMapper(mapper);

        return new DefaultKafkaProducerFactory<>(producerConfig(), serializer, jsonSerializer);
    }

    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, EventsConstant.booking_grp);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        //props.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        //props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, "com.app.commons.createdEvents.BookingCreatedEvent");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        StringDeserializer key_deserializer = new StringDeserializer();

        JacksonJsonDeserializer<Object> value_deserializer = new JacksonJsonDeserializer<>();

        value_deserializer.addTrustedPackages("*");

        DefaultJacksonJavaTypeMapper mapper = new DefaultJacksonJavaTypeMapper();
        Map<String,Class<?>> idMapper = new HashMap<>();
        idMapper.put("seat_event", SeatReservationCreatedEvent.class);
        idMapper.put("payment_event", PaymentCreatedEvent.class);

        mapper.setIdClassMapping(idMapper);
        mapper.setTypePrecedence(DefaultJacksonJavaTypeMapper.TypePrecedence.TYPE_ID);
        value_deserializer.setTypeMapper(mapper);

        return new DefaultKafkaConsumerFactory<>(consumerConfig(),key_deserializer,value_deserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
