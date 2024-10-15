
package ro.anre.reges.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;



@XmlRootElement(name = "Result")
@Data
public class Result {

    @JacksonXmlProperty(localName = "Code")
    protected String code;
    @JacksonXmlProperty(localName = "Description")
    protected String description;
    @JacksonXmlProperty(localName = "Ref")
    protected String ref;

}
