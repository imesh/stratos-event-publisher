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
