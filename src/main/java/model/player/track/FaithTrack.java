package it.polimi.ingsw.model.player.track;

public class FaithTrack {
	protected Cell[] track;
	protected Tile[] vatican_report_tiles;
	protected Cell faith_marker;

	public FaithTrack(Cell[] track, Tile[] vatican_report_tiles){
		this.track = track;
		this.vatican_report_tiles = vatican_report_tiles;
		this.faith_marker = this.track[0];
	}

	public VaticanReports moveForward(int number_of_times) {
		int new_position = number_of_times + faith_marker.getPosition();
		this.faith_marker = track[new_position];

		for (int i = new_position; i > (new_position - number_of_times); i --){
			if (track[i].isPopeSpace()){
				if (checkCell(i)){
					this.activateVaticanReport(this.track[i].whichVaticanReport());
					return this.track[i].whichVaticanReport();
				}
			}
		}

		return null;
	}

	protected boolean checkCell(int i){
		return this.vatican_report_tiles[this.track[i].whichVaticanReport().getIndex()] != null && !(this.vatican_report_tiles[this.track[i].whichVaticanReport().getIndex()].isActive());
	}

	protected void activateVaticanReport(VaticanReports vr_param){
		this.vatican_report_tiles[vr_param.getIndex()].activateVaticanReport();
	}

	public int getMarkerPosition(){
		return this.faith_marker.getPosition();
	}

	public boolean checkIfTileIsActive(int index){
		return this.vatican_report_tiles[index].isActive();
	}
}
