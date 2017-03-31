package Pacman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze {
	ArrayList<String> lines;
	ArrayList<Position> pills;
	ArrayList<Position> powerPills;
	
	public Position pacmanPos;
	public Position ghostPos;
	public Position ghostExit;

	public final int STEP = 2;
	int rows, columns;
	int width, height;
	
	public Maze(){
		lines = new ArrayList<String>();
		pills = new ArrayList<Position>();
		powerPills = new ArrayList<Position>();
		
		try {
			int r = 0;
			Scanner sc = new Scanner(new File("maze.txt"));
			
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				lines.add(line);
				if(line.contains("5")){
					pacmanPos= new Position(r, line.indexOf('5'));
				}
				if(line.contains("4")){
					ghostPos = new Position(r, line.indexOf('4'));
				}
				if(line.contains("6")){
					ghostExit = new Position(r, line.indexOf('6'));
				}
				
				for(int i = 0; i < line.length() ;i++){
					if(line.charAt(i) == '2'){
						pills.add(new Position(r, i));
					}else if(line.charAt(i) == '3'){
						powerPills.add(new Position(r, i));
					}
				}
				
				
				r++;
			}
			
			sc.close();
			
			rows = lines.size();
			columns = lines.get(0).length();
			
			width = columns*STEP + 15;
			height = rows*STEP;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public char charAt(int r, int c) {
		return lines.get(r).charAt(c);
	}
}
