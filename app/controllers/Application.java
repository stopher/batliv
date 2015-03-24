package controllers;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.StreamSupport;

import models.Boat;
import models.ChatMessage;
import models.ChatRoom;
import models.History;
import play.*;
import play.mvc.*;
import utils.ISO8601DateParser;
import views.html.*;
import play.mvc.WebSocket;
import play.mvc.BodyParser;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.AllowXDomain;
import akka.actor.*;
import play.libs.F.*;
import play.mvc.WebSocket;
import models.MyWebSocketActor;

@With({ AllowXDomain.class})
public class Application extends Controller {

	// serves CORS OPTIONS request
	public static Result options(String url) {
		return ok("");
	}
	
	public static Result getSupport() {		
		return ok(support.render(""));
	}
	
	// serves crossdomain.xml
	public static Result crossdomain() {
		response().setContentType("application/xml");
		return ok("<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" /></cross-domain-policy>");
	}
	
    public static Result index() {
    	Play.isDev();
        return ok(index.render("Your new application is ready."));
    }
    
    /*
    public static WebSocket<JsonNode> pingWs() {    
    	
    	return WebSocket.withActor(MyWebSocketActor::props);
    }
    */
    
    public static WebSocket<JsonNode> socket() {
    	
        return WebSocket.whenReady((in, out) -> {
        	
        	ChatRoom.connections.add(out);
        	
            // For each event received on the socket,
            //in.onMessage(System.out::println);
            in.onMessage((json) -> {
            	Logger.debug(json.toString());
            	ChatRoom.connections.forEach(conn -> {                        	
            		conn.write(json);
            	});
            });
                        
            // When the socket is closed.
            in.onClose(() -> {
            	Logger.debug("Disconnected websocket client");
            	ChatRoom.connections.remove(out);
            });

            // Send a single 'Hello!' message
            ObjectNode joinMessage = Json.newObject();
            joinMessage.put("type", "join");
            joinMessage.put("created", ISO8601DateParser.toString(Calendar.getInstance().getTime()));
            
        	ChatRoom.connections.forEach(conn -> {            		
        		conn.write(joinMessage);
        	});
        });
    }

    @play.db.ebean.Transactional
    public static Result updatePosition() {
    	JsonNode json = request().body().asJson();
    	if(json == null) {
    		return badRequest("Expecting Json data");
    	} else {
    		Long id = json.findPath("id").longValue();
    		Double lat = json.findPath("latitude").doubleValue();
    		Double lng = json.findPath("longitude").doubleValue();
    		Boat boat = Boat.find.byId(id);
    		
    		History h = new History();
    		h.setLatitude(lat);
    		h.setLongitude(lng);
    		
    		boat.setLatitude(lat);
    		boat.setLongitude(lng);
    		boat.save();
    		
    		h.setBoat(boat);
    		h.save();
    		
    		return ok(Json.toJson(boat).toString());
    	}
    }
    
    @play.db.ebean.Transactional
    public static Result newBoat() {

    	JsonNode json = request().body().asJson();
    	
    	if(json == null) {
    		return badRequest("Expecting Json data");
    	} else {
    		    		
    		String name = json.findPath("name").textValue();
    		String desc = json.findPath("description").textValue();
    		String tel = json.findPath("telephone").textValue();
    		String type = json.findPath("type").textValue();
    		Double lat = json.findPath("latitude").doubleValue();
    		Double lng = json.findPath("longitude").doubleValue();
    		Long id = json.findPath("id").longValue();
    			
    		Boat b = null;
    		if(id == null) {
    			b = new Boat();
    		} else {
    			b = Boat.find.byId(id);
    			if(b == null) {
    				b = new Boat();
    			}
    		}
    		    		
    		b.setName(name);
    		b.setDescription(desc);
    		b.setLatitude(lat);
    		b.setLongitude(lng);
    		b.setType(type);
    		b.setTelephone(tel);    		
    		b.save();

    		if(name == null
    				||lat == null
    				|| lng == null
    				|| tel == null) {
    			return badRequest("Missing parameter [name], [latitude], [longitude], [telephone]");
    		} else {
    			Logger.debug("Returning json");
    			return ok(Json.toJson(b).toString());
    		}
    	}
    }
    
    public static Result getChat() {
    	    	
    	ArrayNode arrayNode = Json.newObject().arrayNode();    	
    	List<ChatMessage> chatMessages = ChatMessage.find.orderBy("created desc").setMaxRows(100).findList();
    	
    	for(ChatMessage h : chatMessages) {
        	ObjectNode msg= Json.newObject();        	
        	msg.put("user", h.getUser());
        	msg.put("message", h.getMessage());
        	msg.put("created", ISO8601DateParser.toString(h.getCreated()));
        	arrayNode.add(msg);    		
    	}

    	return ok(arrayNode);
    }
    
    public static Result getHistory(Long id) {
    	
    	if(id == null || id < 1) {
    		return badRequest("Missing parameter id or id was 0");
    	}
    	
    	ArrayNode arrayNode = Json.newObject().arrayNode();
    	
    	List<History> history = History.findByBoat(Boat.find.ref(id), 10);
    	
    	for(History h : history) {
        	ObjectNode hNode= Json.newObject();        	
        	hNode.put("latitude", h.getLatitude());
        	hNode.put("longitude", h.getLongitude());
        	hNode.put("created", ISO8601DateParser.toString(h.getCreated()));
        	arrayNode.add(hNode);    		
    	}

    	return ok(arrayNode);
    }
    
    public static Result getBoats() {
    	    	
    	ArrayNode arrayNode = Json.newObject().arrayNode();
    	
    	List<Boat> all = Boat.find.all();
    	
    	for(Boat b : all) {
        	ObjectNode boat = Json.newObject();
        	boat.put("latitude", b.getLatitude());
        	boat.put("longitude", b.getLongitude());
        	boat.put("name", b.getName());
        	boat.put("telephone", b.getTelephone());
        	boat.put("description", b.getDescription());
        	boat.put("id", b.getId());        	
        	arrayNode.add(boat);    		
    	}
    	
    	return ok(arrayNode);
    }
    
    public static Result simulateBoats() {
    	
    	ArrayNode arrayNode = Json.newObject().arrayNode();
    	
    	   	
    	for(int x = 0; x < 50; x++) {
    		
        	ObjectNode boat = Json.newObject();
        	boat.put("latitude", 58.069+(x/100.0));
        	boat.put("longitude", 7.993+(x/100.0));
        	boat.put("name", ""+x);
        	boat.put("telephone", "tel"+x);
        	boat.put("description", "desc"+x);
        	boat.put("id", x);        	
        	arrayNode.add(boat);    		
    	}

    	return ok(arrayNode);
    }
    
    public static Result getBoatsInArea() {
    	
    	JsonNode json = request().body().asJson();
    	
    	Double northwestLat = json.findPath("northwestLat").doubleValue();
		Double northwestLng = json.findPath("northwestLng").doubleValue();
    	Double southeastLat = json.findPath("southeastLat").doubleValue();
		Double southeastLng = json.findPath("southeastLng").doubleValue();
			
		if(northwestLat == null
				||northwestLng == null
				|| southeastLat == null
				|| southeastLng == null) {
			return badRequest("Missing parameter [northwestLat], [northwestLng], [southeastLat], [southeastLng]");
		}
		
		List<Boat> boatsInArea = Boat.findPositionsInBounds(northwestLat, northwestLng, southeastLat, southeastLng, 1000);
		
    	
    	ArrayNode arrayNode = Json.newObject().arrayNode();
    	    	
    	for(Boat b : boatsInArea) {
        	ObjectNode boat = Json.newObject();
        	boat.put("latitude", b.getLatitude());
        	boat.put("longitude", b.getLongitude());
        	boat.put("name", b.getName());
        	boat.put("telephone", b.getTelephone());
        	boat.put("description", b.getDescription());
        	boat.put("type", b.getType());
        	boat.put("id", b.getId());        	
        	arrayNode.add(boat);    		
    	}

    	return ok(arrayNode);
    }
}
