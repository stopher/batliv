package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

public class ChatRoom {
	public static List<WebSocket.Out<JsonNode>> connections = new ArrayList<>();
	public static HashMap<WebSocket.In<JsonNode>, WebSocket.Out<JsonNode>> connMap = new HashMap<>();
}
