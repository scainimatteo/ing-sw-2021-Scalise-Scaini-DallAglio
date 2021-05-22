package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

public class PayMessage extends ResourceMessage {
	public PayMessage(){
		this.warehouse_top = new ArrayList<Resource>();
		this.warehouse_mid = new ArrayList<Resource>();
		this.warehouse_bot = new ArrayList<Resource>();
		this.strongbox = new ArrayList<Resource>();
		this.extraspace = new ArrayList<Resource>();
	}

	public void useMessage(Controller controller){
		controller.handlePay(this.player, this.warehouse_top, this.warehouse_mid, this.warehouse_bot, this.strongbox, this.extraspace);
	}
}
