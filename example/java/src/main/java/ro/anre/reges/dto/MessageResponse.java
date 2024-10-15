
package ro.anre.reges.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;



@Data
@JacksonXmlRootElement(localName = "MessageResponse")
public class MessageResponse {

    @JacksonXmlProperty(localName = "Header")
    protected Header Header;

    @JacksonXmlProperty(localName = "ResponseId")
    protected String responseId;


}

