package org.samples.apache.stratos.event.event;

import org.apache.stratos.messaging.event.Event;

/**
 * Created by akila on 7/29/14.
 */
public class MemberFaultEventMessage extends Event{
    private org.apache.stratos.messaging.event.health.stat.MemberFaultEvent  message;

    public MemberFaultEventMessage(String clusterId, String memberId, String partitionId, float value){
        message = new org.apache.stratos.messaging.event.health.stat.MemberFaultEvent(clusterId, memberId, partitionId, value);
    }

    public org.apache.stratos.messaging.event.health.stat.MemberFaultEvent getMemberFaultEvent(){
        return message;
    }

}
