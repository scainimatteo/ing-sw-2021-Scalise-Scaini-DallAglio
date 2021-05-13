package it.polimi.ingsw.controller.util;

import java.io.Serializable;

import it.polimi.ingsw.view.ViewType;
import it.polimi.ingsw.view.Viewable;

public class ViewMessage implements Serializable {
	private static final long serialVersionUID = 342L;
	private String nickname = null;
	private ViewType view_type;
	private Viewable reply;

	public ViewMessage(ViewType view_type) {
		this.view_type = view_type;
	}

	public ViewMessage(ViewType view_type, String nickname) {
		this.view_type = view_type;
		this.nickname = nickname;
	}

	public ViewType getViewType() {
		return this.view_type;
	}

	public Viewable getReply() {
		return this.reply;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setReply(Viewable reply) {
		this.reply = reply;
	}
}
