/**
 * 
 */
package org.bonitasoft.connector;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorValidationException;

/**
 * This abstract class is generated and should not be modified.
 */
public abstract class AbstractKafkaPublisherImpl extends AbstractConnector {

	protected final static String KAFKA_BROKERS_INPUT_PARAMETER = "kafka_brokers";
	protected final static String KAFKA_TOPICS_INPUT_PARAMETER = "kafka_topics";
	protected final static String RECORD_INPUT_PARAMETER = "record";

	protected final java.lang.String getKafka_brokers() {
		return (java.lang.String) getInputParameter(KAFKA_BROKERS_INPUT_PARAMETER);
	}

	protected final java.lang.String getKafka_topics() {
		return (java.lang.String) getInputParameter(KAFKA_TOPICS_INPUT_PARAMETER);
	}

	protected final java.util.List getRecord() {
		return (java.util.List) getInputParameter(RECORD_INPUT_PARAMETER);
	}

	@Override
	public void validateInputParameters() throws ConnectorValidationException {
		try {
			getKafka_brokers();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("kafka_brokers type is invalid");
		}
		try {
			getKafka_topics();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("kafka_topics type is invalid");
		}
		try {
			getRecord();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("record type is invalid: " + cce.getMessage());
		}

	}

}
