package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

public interface ResourceMessage extends Message {
	public ArrayList<Resource> warehouse = new ArrayList<Resource>();
	public ArrayList<Resource> strongbox = new ArrayList<Resource>();
	public ArrayList<Resource> extraspace = new ArrayList<Resource>();
}
