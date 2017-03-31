package Pacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import GameEngine.Game;
import GameEngine.GameApplicaiton;
import Utility.*;


public class Pacman extends Game{
	
	public static void main(String [] args){
		GameApplicaiton.start(new Pacman());
	}
	
	int reqDir, frame;
	GameData data;
	SpriteSheet drawer;
	
	public Pacman(){
		data = new GameData();
		drawer = new SpriteSheet("images/packman_sheet.png", "images/packman_sheet.info");
	
		title = "Pacman";
		width = data.getWidth();
		height = data.getHeight()+50;
		delay = 15;
	}

	@Override
	public void init() {}
	
	@Override
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if(37 <= key && key <= 40){
			reqDir = key-37;
		}
		
		//restart game
		if(data.dead == true && key == KeyEvent.VK_SPACE){
			data.dead = false;
			data.score = 0;
			data = new GameData();
			update();
		}
	}

	@Override
	public void update() {
		if(!data.dead){
			frame++;
			data.movePacman(reqDir);
			
			//move ghost
			for(int i = 0; i < 4; i++){
				if(!data.ghosts[i].resetting){
					data.moveGhost(data.ghosts[i],data.ghosts[i].decide(data));
				}
			}
			
			data.update();
		}
	}

	@Override
	public void draw(Graphics g) {
		//clean the canvas
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		
		//draw maze
		drawer.draw(g, "mazes", 0, 0, 0, false);
		
		//draw pill
		for(Position pill : data.pills){
			drawer.draw(g, "pill", 0, pill.column*2, pill.row*2, true);
		}
						
		//draw powerpills
		for(Position powerPill : data.powerPills){
			drawer.draw(g, "powerpills", 0, powerPill.column*2, powerPill.row*2, true);
		}
		
		//draw ghost
		for(int i = 0; i < 4; i++){
			if(data.ghosts[i].edibleTimer == 0){
				drawer.draw(g, "ghosts", i, data.ghosts[i].curDir+frame%2 ,data.ghosts[i].pos.column*2, data.ghosts[i].pos.row*2, true);
			}else{
				drawer.draw(g, "edibleghosts", frame%2,  data.ghosts[i].pos.column*2,  data.ghosts[i].pos.row*2, true);
			}
		}
		
		
		//draw pacman
		MoverInfo pacman = data.pacman;
		//System.out.println("column: "+pacman.pos.column + "\n"+"row: " +pacman.pos.row);
		drawer.draw(g, "packmans", pacman.curDir, frame%3, pacman.pos.column*2, pacman.pos.row*2, true);
		
		
		//draw score
		drawer.draw(g, "score", 0, 10, 510, false);
		String score = ""+data.score;
		for(int i = 0; i < score.length(); i++){
			char c = score.charAt(score.length()-1-i);
			drawer.draw(g, "digits", c-'0', width-i*20-40, 510, false);
		}
		
		if(data.dead){
			g.setColor(new Color(100,100,100,200));
			g.fillRect(0, 0, width, height);
			drawer.draw(g, "over", 0, width/2, height/2-50, true);
		}
		
	}

}
