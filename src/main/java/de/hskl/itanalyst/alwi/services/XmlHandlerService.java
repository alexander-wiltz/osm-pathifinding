package de.hskl.itanalyst.alwi.services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
import jakarta.xml.bind.Unmarshaller;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.StringReader;


@Slf4j
@Service
public class XmlHandlerService {

    /**
     * Method turns a given xml string into a defined object
     *
     * @param xml          string containing xml
     * @param genericClass target class object
     * @param <T>          object
     * @return configured object
     */
    @SuppressWarnings("unchecked")
    public <T> T getObjectFromXmlString(@NonNull String xml, Class<T> genericClass) {
        if (xml.isEmpty()) {
            log.debug("XML-String should not be empty");
            return null;
        }

        T object = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(genericClass);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            object = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException exception) {
            if (exception instanceof UnmarshalException) {
                log.error("Wrong target object given. Expected '{}'", genericClass.getSimpleName());
            }
            log.error("Error while generating the object from XML-String", exception);
        }

        if (object != null) {
            log.debug("String successfully generated.");
        }

        return object;
    }

}
