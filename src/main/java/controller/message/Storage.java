package it.polimi.ingsw.controller.message;

import java.util.ArrayList;

import it.polimi.ingsw.model.resources.Resource;

public class Storage {
	public ArrayList<Resource> warehouse_top;
	public ArrayList<Resource> warehouse_mid;
	public ArrayList<Resource> warehouse_bot;
	public ArrayList<Resource> strongbox;
	public ArrayList<Resource> extraspace;

	public Storage() {
		this.warehouse_top = new ArrayList<Resource>();
		this.warehouse_mid = new ArrayList<Resource>();
		this.warehouse_bot = new ArrayList<Resource>();
		this.strongbox = new ArrayList<Resource>();
		this.extraspace = new ArrayList<Resource>();
	}

	public ArrayList<Resource> getWarehouseTop() {
		return this.warehouse_top;
	}

	public ArrayList<Resource> getWarehouseMid() {
		return this.warehouse_mid;
	}

	public ArrayList<Resource> getWarehouseBot() {
		return this.warehouse_bot;
	}

	public ArrayList<Resource> getStrongbox() {
		return this.strongbox;
	}

	public ArrayList<Resource> getExtraspace() {
		return this.extraspace;
	}

	public void addToWarehouseTop(ArrayList<Resource> res) {
		this.warehouse_top.addAll(res);
	}

	public void addToWarehouseMid(ArrayList<Resource> res) {
		this.warehouse_mid.addAll(res);
	}

	public void addToWarehouseBot(ArrayList<Resource> res) {
		this.warehouse_bot.addAll(res);
	}

	public void addToStrongbox(ArrayList<Resource> res) {
		this.strongbox.addAll(res);
	}

	public void addToExtraspace(ArrayList<Resource> res) {
		this.extraspace.addAll(res);
	}
}
