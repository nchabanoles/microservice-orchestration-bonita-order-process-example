/**
 * 
 */
package org.bonitasoft.connector;

import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bonitasoft.engine.connector.ConnectorException;

/**
 *The connector execution will follow the steps
 * 1 - setInputParameters() --> the connector receives input parameters values
 * 2 - validateInputParameters() --> the connector can validate input parameters values
 * 3 - connect() --> the connector can establish a connection to a remote server (if necessary)
 * 4 - executeBusinessLogic() --> execute the connector
 * 5 - getOutputParameters() --> output are retrieved from connector
 * 6 - disconnect() --> the connector can close connection to remote server (if any)
 */
public class KafkaPublisherImpl extends AbstractKafkaPublisherImpl {

	private Producer<Long, String> producer;

	@Override
	protected void executeBusinessLogic() throws ConnectorException{
		@SuppressWarnings("unchecked")
		ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(getKafka_topics(),
				buildRecordContent(getRecord()));
		try {
			producer.send(record);
		}	
		catch (Exception e) {
			throw new ConnectorException("Unable to publish record to kafka.", e);
		}


	}

	private String buildRecordContent(List<List<String>> record) {
		final StringBuffer contentAsJSONString = new StringBuffer("{");
		record.forEach(new Consumer<List<String>>() {
			public void accept(List<String> pair) {
				if(contentAsJSONString.length()>1) {
					contentAsJSONString.append(",");
				}
				contentAsJSONString.append("\"").append(pair.get(0)).append("\"").append(":").append("\"").append(pair.get(1)).append("\"");
			};
		});
		contentAsJSONString.append("}");
		return contentAsJSONString.toString();
	}

	@Override
	public void connect() throws ConnectorException{
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getKafka_brokers());
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "bonita-kafka-publisher-connector");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producer = new KafkaProducer<>(props);

	}

	@Override
	public void disconnect() throws ConnectorException{
		producer.flush();
		producer.close();
	}

}
