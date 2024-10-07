
package ro.anre.reges.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;



@JacksonXmlRootElement(localName = "Header")
@Data
public class Header {

    @JacksonXmlProperty(localName = "MessageId")
    protected String messageId;
    @JacksonXmlProperty(localName = "ClientApplication")
    protected String clientApplication;
    @JacksonXmlProperty(localName = "Version")
    protected String version;
    @JacksonXmlProperty(localName = "Operation")
    protected String operation;
    @JacksonXmlProperty(localName = "AuthorId")
    protected String authorId;
    @JacksonXmlProperty(localName = "SessionId")
    protected String sessionId;
    @JacksonXmlProperty(localName = "User")
    protected String user;
    @JacksonXmlProperty(localName = "UserId")
    protected String userId;
    @JacksonXmlProperty(localName = "Timestamp")
    protected XMLGregorianCalendar timestamp;

}
