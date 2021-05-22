package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;

public interface Controller {
	public void handleMessage(Message message);
	public void handleDiscardResources(Player player);
	public void handlePay(Player player, ArrayList<Resource> warehouse_top, ArrayList<Resource> warehouse_mid, ArrayList<Resource> warehouse_bot, ArrayList<Resource> strongbox, ArrayList<Resource> extraspace);
	public void handleStore(Player player, ArrayList<Resource> warehouse_top, ArrayList<Resource> warehouse_mid, ArrayList<Resource> warehouse_bot, ArrayList<Resource> strongbox, ArrayList<Resource> extraspace, int shelf);
	public void handleBuyCard(Player player, int row, int column, int slot);
	public void handleMarket(Player player, int row, int column, boolean row_or_column, ArrayList<Resource> white_marbles);
	public void handleProduction(Player player, ProductionInterface production);
	public void handleActivateLeader(Player player, LeaderCard leader_card);
	public void handleRearrange(Player player, int swap1, int swap2);
	public void handleEndTurn(Player player);
	public void handleDiscardLeader(Player player, LeaderCard leader_card);
}
