package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.message.Storage;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

import java.io.Serializable;

public abstract class ResourceMessage extends Message implements Serializable {
	private static final long serialVersionUID = 99734L;
	Storage storage;
}
