package Pacman;

public class MoverInfo {
	
	public MoverInfo(Position pos) {
		this.pos = new Position(pos.row, pos.column);
	}
	int curDir, reqDir;
	Position pos;
	
	static final int LEFT=0, UP=1, RIGHT=2, DOWN=3;
	static final int[] DROW = {0, -1, 0, 1};
	static final int[] DCOL = {-1, 0, 1, 0};
	static final int[] REV = {RIGHT, DOWN, LEFT, UP};
}
