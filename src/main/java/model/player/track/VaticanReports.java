package it.polimi.ingsw.model.player.track;

public enum VaticanReports {
	REPORT1(0),
	REPORT2(1),
	REPORT3(2);

	private int report_index;

	private VaticanReports(int report_index){
		this.report_index = report_index;
	}

	public int getIndex(){
		return this.report_index;
	}
}
