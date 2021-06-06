package it.polimi.ingsw.model.player.track;

public class SoloFaithTrack extends FaithTrack {
	private Cell black_marker;

	public SoloFaithTrack(Cell[] track, Tile[] vatican_report_tiles){
		super(track, vatican_report_tiles);
		this.black_marker = track[0];
	}

	public VaticanReports moveForwardBlackMarker(int number_of_times) {
		int new_position = number_of_times + this.black_marker.getPosition();
		this.black_marker = super.track[new_position];

		for (int i = new_position; i > (new_position - number_of_times); i--){
			if (super.track[i].isPopeSpace()){
				if (super.checkCell(super.track[i].whichVaticanReport().getIndex())){
					super.vatican_report_tiles[this.track[i].whichVaticanReport().getIndex()].activateVaticanReport();
					super.activateVaticanReport(super.track[i].whichVaticanReport());
					return super.track[i].whichVaticanReport();
				}
			}
		}

		return null;
	}

	public int getBlackMarkerPosition(){
		return this.black_marker.getPosition();
	}
}
