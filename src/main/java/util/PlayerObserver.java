package it.polimi.ingsw.util;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.controller.util.FaithController;

public interface PlayerObserver {
	public void update(FaithController faith_controller);

	public void update(VaticanReports vatican_rep);
}
