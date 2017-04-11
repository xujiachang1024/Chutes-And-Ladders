package game_components;

public class Ladder {

	private Square top, bottom;

	public Ladder(Square top, Square bottom) {

		this.top = top;
		this.bottom = bottom;

	}

	public Square getBottom() {
		return this.bottom;
	}

	public Square getTop() {
		return this.top;
	}

}
