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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

@XmlType(name = "property")
class XmlProperty {

    @XmlAttribute
    public String key;

    @XmlValue
    public String value;
}


@XmlType(name = "properties")
class XmlProperties {

    @XmlElement(name = "entry")
    public Collection<XmlProperty> entries = new ArrayList<XmlProperty>();
}

// for use with a @XmlJavaTypeAdapter(PropertiesAdapter.class) annotation
public class PropertiesAdaptor extends XmlAdapter<XmlProperties, Properties> {

    public XmlProperties marshal(Properties props) {

        XmlProperties xml = new XmlProperties();

        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            XmlProperty xmlEntry = new XmlProperty();
            xmlEntry.key = entry.getKey().toString();
            xmlEntry.value = entry.getValue().toString();
            xml.entries.add(xmlEntry);
        }

        return xml;
    }

    public Properties unmarshal(XmlProperties xml) {

        Properties props = new Properties();

        for (XmlProperty entry : xml.entries)
            props.setProperty(entry.key, entry.value);
        return props;
    }
}
