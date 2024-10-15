package ro.anre.reges.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperConfiguration {

    @Bean
    public XmlMapper createXmlMapper() {
        final XmlMapper mapper = new XmlMapper();

        mapper.registerModule(new JavaTimeModule());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return mapper;
    }


}

