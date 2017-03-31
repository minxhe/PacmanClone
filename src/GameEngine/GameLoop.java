package GameEngine;

public class GameLoop extends Thread {
	
	private final Game game;
	private final GameCanvas canvas;

	public GameLoop(Game game, GameCanvas canvas){
		this.game = game;
		this.canvas = canvas;
	}
	
	@Override
	public void run(){
		
		game.init();
		
		while(!game.isOver()){
			game.update();
			canvas.repaint();
			try {
				Thread.sleep(game.getDelay());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
