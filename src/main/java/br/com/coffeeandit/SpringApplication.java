package br.com.coffeeandit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("br.com.coffeeandit")
@EnableKafka
@EnableCircuitBreaker
public class SpringApplication {

    public static void main(String[] args) {
        final long currentTimeMillis = System.currentTimeMillis();
        System.setProperty("spring.kafka.producer.client-id", "sale-client" + currentTimeMillis);
        System.setProperty("spring.kafka.producer.producer-id", "sale-producer" + currentTimeMillis);
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }

    @Bean
    public KafkaTransactionManager kafkaTransactionManager(ProducerFactory producerFactory) {
        KafkaTransactionManager ktm = new KafkaTransactionManager(producerFactory);
        ktm.setTransactionSynchronization(AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION);
        return ktm;
    }


    @Bean
    @Primary
    public JpaTransactionManager transactionManager(EntityManagerFactory em) {
        return new JpaTransactionManager(em);
    }

    @Bean(name = "chainedTransactionManager")
    public ChainedTransactionManager chainedTransactionManager(JpaTransactionManager jpaTransactionManager,
                                                               KafkaTransactionManager kafkaTransactionManager) {
        return new ChainedTransactionManager(kafkaTransactionManager, jpaTransactionManager);
    }


}
