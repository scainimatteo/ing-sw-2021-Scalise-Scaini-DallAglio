package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.net.URL;

import it.polimi.ingsw.controller.message.BuyCardMessage;
import it.polimi.ingsw.controller.message.MarketMessage;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.App;


public class GameScene extends SceneController implements Initializable{
	@FXML private ImageView free_marble;
	@FXML private GridPane market_grid;
	@FXML private GridPane dev_card_grid;
	@FXML private VBox row_buttons;
	@FXML private HBox column_buttons;
	
	// indexes of the card chosen by the player
	private int chosen_card_row;
	private int chosen_card_column;

	@Override
	public void initialize(URL location, ResourceBundle resources){
		Resource[][] market = App.getSimpleGame().getMarket();
		Resource free_marble = App.getSimpleGame().getFreeMarble();
		DevelopmentCard[][] development_cards_on_table = App.getSimpleGame().getDevelopmentCardsOnTable();

		setMarket(market, free_marble);
		setDevelopmentCardsOnTable(development_cards_on_table);
	}

	/**
	 * Set the ImageViews of the Market using the SimpleModel
	 *
	 * @param market a matrix representing the Market
	 * @param free_marble the free Resource in the Market
	 */
	private void setMarket(Resource[][] market, Resource free_marble) {
		this.free_marble.setImage(new Image(getClass().getResource(getPath(free_marble)).toString()));

		int i = 0;
		int j = 0;
		for (Node node: this.market_grid.getChildren()) {
			// set the path of each square of the GridPane
			((ImageView) node).setImage(new Image(getClass().getResource(getPath(market[i][j])).toString()));

			j++;
			if (j % 4 == 0) {
				j = 0;
				i++;
			}
		}
		setMarketButtons();
	}

	/**
	 * @param resource the Resource to get the path to
	 * @return the String with the path of the Resource
	 */
	private String getPath(Resource resource) {
		if (resource == null) {
			// white marble
			return Resource.getNullMarblePath();
		}

		return resource.getPath();
	}

	/**
	 * Set the methods for the Market buttons
	 */
	private void setMarketButtons() {
		int i = 0;
		for (Node node: row_buttons.getChildren()) {
			final int row = i;
			node.setOnMouseClicked(e -> chooseRow(row));
			i++;
		}

		int j = 0;
		for (Node node: column_buttons.getChildren()) {
			final int column = j;
			node.setOnMouseClicked(e -> chooseColumn(column));
			j++;
		}
	}

	/**
	 * Send a MarketMessage with the Market row chosen
	 *
	 * @param row the row chosen
	 */
	public void chooseRow(int row) {
		MarketMessage message = new MarketMessage(false, row);
		App.sendMessage(message);
	}

	/**
	 * Send a MarketMessage with the Market column chosen
	 *
	 * @param column the column chosen
	 */
	public void chooseColumn(int column) {
		MarketMessage message = new MarketMessage(true, column);
		App.sendMessage(message);
	}

	/**
	 * Set the ImageViews and the onMouseClicked methods of the DevelopmentCardsOnTable using the SimpleModel
	 *
	 * @param development_cards_on_table a matrix representing the DevelopmentCardsOnTable
	 */
	private void setDevelopmentCardsOnTable(DevelopmentCard[][] development_cards_on_table) {
		int i = 0;
		int j = 0;
		for (Node node: this.dev_card_grid.getChildren()) {
			final int row = i;
			final int column = j;
			// set the path and the methods of each square of the GridPane
			ImageView imageview = (ImageView)((StackPane) node).getChildren().get(0);
			imageview.setOnMouseClicked(x -> chooseCard(x, row, column));
			imageview.setImage(new Image(getClass().getResource(development_cards_on_table[i][j].getPath()).toString()));

			j++;
			if (j % 4 == 0) {
				j = 0;
				i++;
			}
		}
	}

	/**
	 * Method called when a DevelopmentCard is clicked on
	 *
	 * @param e the MouseEvent that triggered this method
	 * @param row the row on the DevelopmentCardsOnTable matrix
	 * @param column the column on the DevelopmentCardsOnTable matrix
	 */
	public void chooseCard(MouseEvent e, int row, int column) {
		this.chosen_card_row = row;
		this.chosen_card_column = column;
		// show the buttons for the slot
		StackPane pane = (StackPane)((ImageView) e.getSource()).getParent();
		pane.getChildren().get(1).setVisible(true);
		pane.getChildren().get(1).setDisable(false);
	}

	/**
	 * Put the DevelopmentCard bought in the first slot
	 */
	@FXML
	public void selectSlot1(MouseEvent e) {
		BuyCardMessage message = new BuyCardMessage(chosen_card_row, chosen_card_column, 1);
		App.sendMessage(message);
		// hide the slot buttons
		HBox hbox = (HBox)((Node) e.getSource()).getParent();
		hbox.setVisible(false);
		hbox.setDisable(true);
	}

	/**
	 * Put the DevelopmentCard bought in the second slot
	 */
	@FXML
	public void selectSlot2(MouseEvent e) {
		BuyCardMessage message = new BuyCardMessage(chosen_card_row, chosen_card_column, 2);
		App.sendMessage(message);
		// hide the slot buttons
		HBox hbox = (HBox)((Node) e.getSource()).getParent();
		hbox.setVisible(false);
		hbox.setDisable(true);
	}

	/**
	 * Put the DevelopmentCard bought in the third slot
	 */
	@FXML
	public void selectSlot3(MouseEvent e) {
		BuyCardMessage message = new BuyCardMessage(chosen_card_row, chosen_card_column, 3);
		App.sendMessage(message);
		// hide the slot buttons
		HBox hbox = (HBox)((Node) e.getSource()).getParent();
		hbox.setVisible(false);
		hbox.setDisable(true);
	}

	/**
	 * Show the PlayerBoardScene
	 */
	public void changeSceneToBoard() {
		return;
	}
}