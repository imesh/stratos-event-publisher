/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.samples.apache.stratos.event.util;

import org.apache.stratos.messaging.event.Event;
import org.samples.apache.stratos.event.model.Wrapper;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.util.List;

/**
 * Read user given sample events
 */
public class EventFileReader {

    private static EventFileReader eventFileReader = null;
    private static JAXBContext jc = null;



    private EventFileReader() {
    }

    public static EventFileReader getInstance() throws JAXBException {
        if (eventFileReader == null) {
            eventFileReader = new EventFileReader();
        }
        if (jc == null) {
            jc = JAXBContext.newInstance(SampleConstants.eventClassArray);
        }
        return eventFileReader;
    }


    public List<Object> readSampleEvents() throws JAXBException {
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return unmarshal(unmarshaller, Object.class, System.getProperty(SampleConstants.EVENT_USER_DATA_PATH));
    }

    /**
     * Unmarshal XML to Wrapper and return List value.
     */
    private static <T> List<T> unmarshal(Unmarshaller unmarshaller, Class<T> clazz, String xmlLocation) throws JAXBException {
        StreamSource xml = new StreamSource(xmlLocation);
        Wrapper<T> wrapper = (Wrapper<T>) unmarshaller.unmarshal(xml, Wrapper.class).getValue();
        return wrapper.getItems();
    }

    /**
     * Wrap List in Wrapper, then leverage JAXBElement to supply root element
     * information.
     */
    private static void marshal(Marshaller marshaller, List<?> list, String name)
            throws JAXBException {
        QName qName = new QName(name);
        Wrapper wrapper = new Wrapper(list);
        JAXBElement<Wrapper> jaxbElement = new JAXBElement<Wrapper>(qName, Wrapper.class, wrapper);
        marshaller.marshal(jaxbElement, System.out);
    }
}
