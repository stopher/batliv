package controllers;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.StreamSupport;

import models.Boat;
import models.ChatMessage;
import models.ChatRoom;
import models.GamePlayer;
import models.Guess;
import models.History;
import play.*;
import play.mvc.*;
import utils.ISO8601DateParser;
import utils.Toolbox;
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
public class Guesser extends Controller {

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

    @play.db.ebean.Transactional
    public static Result newGuess() {
    	JsonNode json = request().body().asJson();
    	if(json == null) {
    		return badRequest("Expecting Json data");
    	} else {
    		String uuid = json.findPath("uuid").textValue();
    		
    		GamePlayer gamePlayer = GamePlayer.find.where().eq("uuid", uuid).findUnique();
    		
    		Integer selectedVal = json.findPath("selectedVal").intValue();
    		Integer val2 = json.findPath("val2").intValue();
    		Integer val3 = json.findPath("val3").intValue();
    		
    		Guess g = new Guess();
    		
    		    		
    		int winningNumber = Toolbox.winningNumber(selectedVal, val2, val3);
    		int bonusPoints = Toolbox.bonusPoints(1);
    		
    		if(gamePlayer == null) {
    			gamePlayer = new GamePlayer();
    			gamePlayer.setUuid(uuid);
    			gamePlayer.setPoints(0);
    			gamePlayer.setName("");
    			gamePlayer.save();
    		} else {    			
    			g.setGameplayer(gamePlayer);
    		}
    		
    		if(winningNumber == selectedVal.intValue()) {    			
    			g.setPoints(1000);
    			g.setBonusPoints(bonusPoints);    			
    			Integer moreBonus = gamePlayer.increaseWinsInArow();
    			g.setBonusPoints(g.getBonusPoints()+moreBonus);
    			
    		} else {
    			g.setPoints(0);
    			g.setBonusPoints(0);
    			gamePlayer.setWinsInARow(0);
    		}
    	
    		g.save();
    		
    		
    		Integer oldpoints = gamePlayer.getPoints();
    		Integer newpoints = oldpoints.intValue()+g.getSumPoints();
    		
    		Guess findEarlierGuess = Guess.findEarlierGuess(g, gamePlayer);
    		if(findEarlierGuess != null) {
    			long newTime = Calendar.getInstance().getTime().getTime();    			
    			long oldTime = findEarlierGuess.getCreated().getTime();
    			int rest = (int)(newTime-oldTime)/500; // subtracting timeleft
    			newpoints = newpoints - rest;
    		}
    		gamePlayer.setPoints(newpoints);
    		gamePlayer.save();
    		
    		ObjectNode jsonResponse = Json.newObject();
    		jsonResponse.put("uuid", gamePlayer.getUuid());
    		jsonResponse.put("points", g.getSumPoints());
    		jsonResponse.put("totalPoints", gamePlayer.getPoints());
    		jsonResponse.put("winsInARow", gamePlayer.getWinsInARow());
    		
    		return ok(jsonResponse);
    	}
    }
    
    public static Result getPlayer(String uuid) {
    	
    	if(uuid == null || uuid.length() < 1) {
    		return badRequest("Missing parameter uuid");
    	}
    	    	
    	GamePlayer findUnique = GamePlayer.find.where().eq("uuid", uuid).findUnique();
    	if(findUnique == null) {
    		return notFound("Could not find player");
    	}
    	
    	ObjectNode jsonResponse = Json.newObject();
		jsonResponse.put("uuid", findUnique.getUuid());
		jsonResponse.put("points", findUnique.getPoints());
				
    	return ok(Json.toJson(findUnique));
    }
    
    public static Result getTopScores() {
    	
    	List<GamePlayer> findList = GamePlayer.find.orderBy("points desc").setMaxRows(1000).findList();
    	
    	ArrayNode arrayNode = Json.newObject().arrayNode();
    	
    	if(findList == null) {
    		return notFound("Could not find any scores");
    	}    	
    	findList.forEach(x -> {
    		ObjectNode gpNode= Json.newObject();
    		gpNode.put("name", x.getName());
    		gpNode.put("points", x.getPoints());
    		arrayNode.add(gpNode);
    	});

    	return ok(arrayNode);    	
    }
  
}
