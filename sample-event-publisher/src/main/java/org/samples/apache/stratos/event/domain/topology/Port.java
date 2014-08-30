package org.samples.apache.stratos.event.domain.topology;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by akila on 8/17/14.
 */

@XmlType
public class Port {

    private String protocol;
    private int value;
    private int proxy;

    public Port() {

    }

    public Port(String protocol, int value, int proxy) {
        setProtocol(protocol);
        setValue(value);
        setProxy(proxy);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getProxy() {
        return proxy;
    }

    public void setProxy(int proxy) {
        this.proxy = proxy;
    }

    @Override
    public String toString() {
        return "Port [protocol=" + protocol + ", value=" + value + ", proxy=" + proxy + "]";
    }
}
