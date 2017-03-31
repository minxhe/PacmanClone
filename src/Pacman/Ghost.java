package Pacman;

import java.util.List;
import java.util.Random;

public class Ghost extends MoverInfo{
	 public int edibleTimer;
	 public boolean resetting = false;
	 
	 Random random = new Random();
	 
	 public int decide(GameData data) {
			// TODO Auto-generated method stub
			int dirs;

			List<Integer> list = data.getPossibleDirs(this.pos);
		
			//Remove the reverse direction so the ghost move in straight line
			list.remove(new Integer(MoverInfo.REV[this.curDir]));
			dirs = list.get(random.nextInt(list.size()));

			return dirs;
		}
	 
	 
	 public Ghost(Position pos){
		 super(pos);
	 }
}
