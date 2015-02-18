package models;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.*;

public class MyWebSocketActor extends UntypedActor {
	
    public static Props props(ActorRef out) {
        return Props.create(MyWebSocketActor.class, out);
    }

    private final ActorRef out;

    public MyWebSocketActor(ActorRef out) {
        this.out = out;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof JsonNode) {
        	JsonNode node = (JsonNode) message;
            out.tell("I received your message: " + node.toString(), self());          
        }
    }
}