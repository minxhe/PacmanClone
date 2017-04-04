package Pacman;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameData {
	public int score;
	public CopyOnWriteArrayList<Position> pills;
	public CopyOnWriteArrayList<Position> powerPills;
	
	public Ghost [] ghosts = new Ghost [4];
	
	Maze maze;
	public MoverInfo pacman;
	public boolean dead = false;
	public int numLives;
	
	public GameData(){
		maze = new Maze();
		numLives = 3;
		setMaze();
	}
	
	private void setMaze() {
		System.out.println("column: "+maze.pacmanPos.column + "\n"+"row: " +maze.pacmanPos.row);
		pacman = new MoverInfo(maze.pacmanPos);
		for(int i = 0 ;i < 4;i++){
			ghosts[i] = new Ghost(maze.ghostPos);
		}
		
		//CopyOnWriteArrayList is synchronous, needed for this case since we dont want multiple thread excecuting at the same time
		pills = new CopyOnWriteArrayList ((List<Position>)(maze.pills.clone()));
		powerPills = new CopyOnWriteArrayList ((List<Position>)(maze.powerPills.clone()));
	}
	
	public void movePacman(int reqDir) {
		if(move(reqDir, pacman)){
			pacman.curDir = reqDir;
		}else{
			move(pacman.curDir, pacman);
		}
		
	}
	
	
	private int wrap(int curPos, int incre, int max){
		return (curPos+max+incre)%max;
	}
	
	private boolean move(int reqDir, MoverInfo info){
		//current position of pacman is (row, column)
		int row = info.pos.row;
		int column = info.pos.column;
		int columns = maze.columns;
		int rows = maze.rows;
		int nrow = wrap(row, MoverInfo.DROW[reqDir], rows);
		int ncol = wrap(column, MoverInfo.DCOL[reqDir], columns);
		if(maze.charAt(nrow, ncol) != '0'){
			info.pos.row = nrow;
			info.pos.column = ncol;
			return true;
		}
		return false;
	}
	
	public void update(){
		if(pills.contains(pacman.pos)){
			pills.remove(pacman.pos);
			score += 10;
		}else if(powerPills.contains(pacman.pos)){
			powerPills.remove(pacman.pos);
			score += 50;
			for(int i = 0; i < ghosts.length; i++){
				if(!ghosts[i].resetting){
					ghosts[i].edibleTimer = 300;
				}	
			}
			
		}
		
		
		if(pills.isEmpty() && powerPills.isEmpty()){
			//game over
			dead = true;
		}
		
		for(Ghost ghost: ghosts){
			if(ghost.edibleTimer > 0){
				if(isTouching(pacman.pos, ghost.pos) && !ghost.resetting){
					score += 100;
					ghost.edibleTimer = 0;
					
					resetGhost(ghost, maze.ghostPos.column, maze.ghostPos.row);
				}
				if(ghost.edibleTimer != 0){
					ghost.edibleTimer--;
				}
			}
			else if(ghost.resetting){
				resetGhost(ghost, maze.ghostPos.column, maze.ghostPos.row);
			}
			
			//IN PROGRESS I DONT KNOW WHY THE FK IT DOESNT WORK
			else if(ghost.edibleTimer == 0 && isTouching(pacman.pos, ghost.pos) && !ghost.resetting){
				numLives--;
				dead = true;
//				if(numLives == 0){
//					dead = true;
//				}
				
				//pacman.pos = maze.pacmanPos;
				//ghost.pos = maze.ghostPos;
				//ghost.curDir = MoverInfo.UP;
			}
		}
	}
	
	//recursion attempt xd (failed)
	public void resetGhost(Ghost g, int col, int row){
		g.resetting = true;
		List<Integer> dirs = getPossibleDirs(g.pos);
		if(g.pos.column == col && g.pos.row == row){
			g.resetting = false;
			g.curDir = 1;
		}
		else if(g.pos.column == maze.ghostExit.column && dirs.contains(3)){
			moveGhost(g, 3);
		}else if(g.pos.row == maze.ghostExit.row){
			if(g.pos.column < col){
				moveGhost(g, 2);
			}else{
				moveGhost(g, 0);
			}
		}
		else if(dirs.contains(0) && dirs.contains(2) && (g.curDir == 0 || g.curDir == 2)){
			if(!dirs.contains(1) && !dirs.contains(3)){
				moveGhost(g, g.curDir);
			}else if(g.pos.row < row && dirs.contains(3)){
				moveGhost(g, 3);
			}else if(g.pos.row > row && dirs.contains(1)){
				moveGhost(g, 1);
			}else{
				moveGhost(g, g.curDir);
			}
			
		}else if(dirs.contains(1) && dirs.contains(3) && (g.curDir == 1 || g.curDir == 3)){
			if(!dirs.contains(0) && !dirs.contains(2)){
				moveGhost(g, g.curDir);
			}else if(g.pos.column < col && dirs.contains(2)){
				moveGhost(g, 2);
			}else if(g.pos.column > col && dirs.contains(0)){
				moveGhost(g, 0);
			}else{
				moveGhost(g, g.curDir);
			}
			
		}else{
			//LEFT 0, UP 1,  RIGHT 2, DOWN 3;
			if(g.pos.row < row && dirs.contains(3) ){
				moveGhost(g, 3);
			}else if(g.pos.row > row && dirs.contains(1) ){
				moveGhost(g, 1);
			}else if(g.pos.column < col && dirs.contains(2)){
				moveGhost(g, 2);
			}else if(g.pos.column > col && dirs.contains(0)){
				moveGhost(g, 0);
			}else{
				if(!move(g.curDir, g)){
					if(g.pos.row == row){
						if(dirs.contains(1)){
							moveGhost(g, 1);
						}else{
							moveGhost(g, 3);
						}
					}else if(g.pos.column == col){
						if(dirs.contains(0)){
							moveGhost(g, 0);
						}else{
							moveGhost(g, 2);
						}
					}else{
						if(dirs.contains(0)){
							moveGhost(g, 0);
						}else{
							moveGhost(g, 2);
						}
					}
				}else{
					moveGhost(g, g.curDir);
				}	
			}
		}
	}
	
	private boolean isTouching(Position a, Position b) {
		return Math.abs(a.column - b.column)+ Math.abs(a.row - b.row) < 7;
	}

	//for ghost movement
	public Position getNextPosition(Position pos, int i){
		int newRow = wrap(pos.row, MoverInfo.DROW[i], maze.rows);
		int newCol = wrap(pos.column, MoverInfo.DCOL[i], maze.columns);
		return new Position(newRow, newCol);
	}
	
	public List<Integer> getPossibleDirs(Position pos){
		List <Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < 4; i++){
			Position newPos = getNextPosition(pos, i);
			if(maze.charAt(newPos.row, newPos.column) != '0'){
				list.add(i);
			}
		}
		return list;
	}
	
	public void moveGhost(Ghost ghost, int dir){
		ghost.reqDir = dir;
		//prevents the ghost from going back to the starting position on its path
		if((maze.charAt(ghost.pos.row, ghost.pos.column) == '6') && (ghost.curDir == 0 || ghost.curDir == 2) && !ghost.resetting){
			move(ghost.curDir, ghost);
		}
		else if(move(ghost.reqDir, ghost)){
			ghost.curDir = dir;
		}else{

			move(ghost.curDir, ghost);
		}
	}
	
	public int getWidth() {
		return maze.width;
	}
	public int getHeight() {
		return maze.height;
	}
}
