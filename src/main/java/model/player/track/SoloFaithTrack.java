package it.polimi.ingsw.model.player.track;

public class SoloFaithTrack extends FaithTrack {
	private Cell black_marker;

	public SoloFaithTrack(Cell[] track, Tile[] vatican_report_tiles){
		super(track, vatican_report_tiles);
		this.black_marker = track[0];
	}

	/**
	 * Move the Black Cross marker forward
	 *
	 * @param number_of_times how many Cells to go forward
	 */
	public VaticanReports moveForwardBlackMarker(int number_of_times) {
		// the new position can be max the last position on the FaithTrack
		int new_position = (number_of_times + this.black_marker.getPosition() < getLastPosition()) ? number_of_times + this.black_marker.getPosition() : getLastPosition();
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

	/**
	 * @return the position of the Cell the Black Cross is on
	 */
	public int getBlackMarkerPosition(){
		return this.black_marker.getPosition();
	}
}
