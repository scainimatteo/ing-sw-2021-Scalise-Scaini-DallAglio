package it.polimi.ingsw.view;

import java.util.ArrayList;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.EndGameMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.servermessage.ViewUpdate;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.util.observer.SetupGameObservable;
import it.polimi.ingsw.util.observer.ViewUpdateObservable;
import it.polimi.ingsw.util.observer.GameStartObservable;
import it.polimi.ingsw.util.observer.ViewUpdateObserver;
import it.polimi.ingsw.util.observer.SetupGameObserver;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

public abstract class View extends GameStartObservable implements ViewUpdateObservable, SetupGameObservable {
	protected SimpleGame simple_game;
	protected ArrayList<SimplePlayer> simple_players;
	protected Turn turn;

	protected boolean initialized = false;
	protected boolean setup_done = false;
	protected String nickname;

	private ArrayList<ViewUpdateObserver> view_update_observers = new ArrayList<ViewUpdateObserver>();
	private ArrayList<SetupGameObserver> setup_game_observers = new ArrayList<SetupGameObserver>();

	public boolean isInitialized() {
		return this.initialized;
	}

	public boolean hasDoneSetup() {
		return this.setup_done;
	}

	public void setUninitialized() {
		this.initialized = false;
	}

	public String getNickname() {
		return this.nickname;
	}

	public abstract void startView(Client client);

	/**
	 * Print the error message, exit if the Client is still in the initialization phase
	 *
	 * @param error_message the ErrorMessage received from the Server
	 */
	public abstract void handleError(ErrorMessage error_message);

	/**
	 * Answer the messages during the initalization phase
	 *
	 * @param initializing_message the InitializingServerMessage received from the Server
	 */
	public abstract void handleInitializing(InitializingServerMessage initializing_message);

	/**
	 * Print the ranking of the Players
	 *
	 * @param end_game_message the EndGameMessage received from the Server
	 */
	public abstract void handleEndGame(EndGameMessage end_game_message);

	/**
	 * SIMPLEMODEL
	 */

	/**
	 * Update the simple model after a ViewUpdate
	 */
	public void updateView(ViewUpdate view_update) {
		if (view_update.simple_game != null) {
			this.simple_game = view_update.simple_game;
		} else if (view_update.simple_player != null) {
			updateSimplePlayer(view_update.simple_player);
		} else if (view_update.turn != null) {
			// if the turn has done the setup, start the SetupScene
			if (!this.setup_done && view_update.turn.hasDoneSetup()){
				this.setup_done = true;
				notifySetupDone();
			// if the turn was null, this is the first Turn and the game is started
			} else if (this.turn == null) {
				notifyGameStarted();
			}

			this.turn = view_update.turn;
		}

		// send the update to the SceneControllers listening on the view_update_observers
		notifyViewUpdate();
	}

	/**
	 * Change the specific SimplePlayer in the simple_players array
	 *
	 * @param simple_player the SimplePlayer to update
	 */
	private void updateSimplePlayer(SimplePlayer simple_player) {
		String nickname = simple_player.getNickname();
		for (int i = 0; i < this.simple_players.size(); i++) {
			if (this.simple_players.get(i).getNickname().equals(nickname)) {
				this.simple_players.set(i, simple_player);
				return;
			}
		}

		this.simple_players.add(simple_player);
	}

	/**
	 * Get the SimplePlayer of the Client
	 *
	 * @return the SimplePlayer of the Client
	 */
	public SimplePlayer getMyPlayer() {
		if (this.simple_players == null) {
			return null;
		}

		for (SimplePlayer s: this.simple_players) {
			if (s.getNickname().equals(this.nickname)) {
				return s;
			}
		}
		// unreachable
		return null;
	}

	/**
	 * OBSERVERS
	 */

	/**
	 * Add a ViewUpdateObserver to the view_update_observers array
	 *
	 * @param observer the ViewUpdateObserver to add
	 */
	@Override
	public void addViewUpdateObserver(ViewUpdateObserver observer) {
		this.view_update_observers.add(observer);
	}

	/**
	 * Remove a ViewUpdateObserver from the view_update_observers array
	 *
	 * @param observer the ViewUpdateObserver to remove
	 */
	@Override
	public void removeViewUpdateObserver(ViewUpdateObserver observer) {
		this.view_update_observers.remove(observer);
	}

	/**
	 * Notify to the ViewUpdateObservers listening that the SimpleModel has been updated
	 */
	@Override
	public void notifyViewUpdate() {
		for (ViewUpdateObserver v: this.view_update_observers) {
			v.updateView();
		}
	}

	/**
	 * Add a SetupGameObserver to the setup_game_observers array
	 *
	 * @param observer the SetupGameObserver to add
	 */
	@Override
	public void addSetupGameObserver(SetupGameObserver observer){
		this.setup_game_observers.add(observer);
	}

	/**
	 * Notify to the SetupGameObserver listening that the game has finished the setup phase
	 */
	@Override
	public void notifySetupDone(){
		for (SetupGameObserver s: this.setup_game_observers){
			s.gameHasDoneSetup();
		}
	}
}
