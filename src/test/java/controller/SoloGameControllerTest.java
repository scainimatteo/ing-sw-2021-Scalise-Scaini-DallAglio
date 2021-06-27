package it.polimi.ingsw.controller;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.ChooseResourcesMessage;
import it.polimi.ingsw.controller.message.DiscardLeaderMessage;
import it.polimi.ingsw.controller.message.ActivateLeaderMessage;
import it.polimi.ingsw.controller.message.MarketMessage;
import it.polimi.ingsw.controller.message.BuyCardMessage;
import it.polimi.ingsw.controller.message.ProductionMessage;
import it.polimi.ingsw.controller.message.DiscardResourcesMessage;
import it.polimi.ingsw.controller.message.PayMessage;
import it.polimi.ingsw.controller.message.StoreMessage;
import it.polimi.ingsw.controller.message.RearrangeMessage;
import it.polimi.ingsw.controller.message.EndTurnMessage;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Initializer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.StubGameController;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.LeaderCardLevelCost;
import it.polimi.ingsw.model.card.LeaderCardResourcesCost;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.CardLevel;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.server.ClientHandler;

public class SoloGameControllerTest{

}
