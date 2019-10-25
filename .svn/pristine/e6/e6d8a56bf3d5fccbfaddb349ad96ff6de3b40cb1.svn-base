package com.vortex.cloud.ums.util.utils.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vortex.cloud.common.kafka.IProducer;
import com.vortex.cloud.common.kafka.msg.KafkaMsg;
import com.vortex.cloud.common.kafka.producer.SimpleProcuder;
import com.vortex.cloud.common.kafka.producer.SimpleProducerConfig;

@Component
public class KafkaProducer {
	private static IProducer producer = null;
	private static KafkaProducer instance = null;
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

	private KafkaProducer() {

	}

	public static String kafkaAddress;

	@Value("${server.kafka.address}")
	public void setName(String kafkaAddress) {
		KafkaProducer.kafkaAddress = kafkaAddress;
	}

	public static synchronized KafkaProducer getInstance() {
		if (instance == null) {
			instance = new KafkaProducer();
			if (producer == null) {
				producer = new SimpleProcuder(new SimpleProducerConfig(kafkaAddress, "com.vortex.cloud.oms.mq.produce.KafkaProducer"));
				try {
					producer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return instance;
	}

	public void produce(String topic, Object value) throws Exception {
		KafkaMsg msg = KafkaMsg.buildMsg(topic, value);
		producer.send(msg);
		logger.error("发送消息成功：" + msg.toString());
	}

	public void produce(String topic, String key, Object value) throws Exception {
		KafkaMsg msg = KafkaMsg.buildMsg(topic, key, value);
		producer.send(msg);
		logger.error("发送消息成功：" + msg.toString());
	}

	public static void stop() {
		if (producer != null) {
			try {
				producer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
