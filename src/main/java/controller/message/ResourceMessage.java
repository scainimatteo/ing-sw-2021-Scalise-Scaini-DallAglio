package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

public abstract class ResourceMessage extends Message {
	public ArrayList<Resource> warehouse_top;
	public ArrayList<Resource> warehouse_mid;
	public ArrayList<Resource> warehouse_bot;
	public ArrayList<Resource> strongbox;
	public ArrayList<Resource> extraspace;
}
