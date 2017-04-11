package game_components;

public class Snake {

	private Square head, tail;

	public Snake(Square head, Square tail) {

		this.head = head;
		this.tail = tail;

	}

	public Square getHead() {
		return this.head;
	}

	public Square getTail() {
		return this.tail;
	}

}
