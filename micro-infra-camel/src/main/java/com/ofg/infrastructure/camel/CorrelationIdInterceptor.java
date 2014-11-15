package com.ofg.infrastructure.camel;

import com.ofg.infrastructure.correlationid.CorrelationIdUpdater;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import static com.ofg.infrastructure.correlationid.CorrelationIdHolder.CORRELATION_ID_HEADER;

/**
 * Interceptor class that ensures the correlationId header is present in {@Exchange}.
 */
public class CorrelationIdInterceptor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Ensures correlationId header is set in incoming message (if is missing a new correlationId is created and set).
     *
     * @param exchange Camel's container holding received message
     * @throws Exception if an internal processing error has occurred
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        String correlationIdHeader = getCorrelationId(exchange);
        CorrelationIdUpdater.updateCorrelationId(correlationIdHeader);
        setCorrelationIdHeaderIfMissing(exchange, correlationIdHeader);
    }

    private String getCorrelationId(Exchange exchange) {
        String correlationIdHeader = (String) exchange.getIn().getHeader(CORRELATION_ID_HEADER);
        if (StringUtils.isBlank(correlationIdHeader)) {
            log.debug("No correlationId has been set in request inbound message. Creating new one.");
            correlationIdHeader = UUID.randomUUID().toString();
        }

        return correlationIdHeader;
    }

    private void setCorrelationIdHeaderIfMissing(Exchange exchange, String correlationIdHeader) {
        Message inboundMessage = exchange.getIn();
        if (!inboundMessage.getHeaders().containsKey(CORRELATION_ID_HEADER)) {
            log.debug("Setting correlationId [" + correlationIdHeader + "] in header of inbound message");
            inboundMessage.setHeader(CORRELATION_ID_HEADER, correlationIdHeader);
        }

    }

}
