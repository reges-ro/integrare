
package ro.anre.reges.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlRootElement(name = "MessageResult")
@Data
public class MessageResult {

    @JacksonXmlProperty(localName = "Header")
    protected Header Header;

    @JacksonXmlProperty(localName = "ResponseId")
    protected String responseId;

    @JacksonXmlProperty(localName = "Result")
    protected Result result;


}
