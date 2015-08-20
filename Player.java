import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Player {
	
private Applet applet;
private Image [] frames;
private Image [] faces;
private Map map;
private GameManager game;	
public boolean left, right, up, down, walking;
public int posx, posy, frameindex, facing, framecount, dy, dx;

public int moveSpeed=5;

public int playerAnim=0;
public int playerAnimAt=0;

public Player (Image [] f, Map m, Applet a, int x, int y, GameManager g, Image [] ff)  {
frames = f;
faces=ff;
applet = a;
map = m;
game=g;
posx=x; posy=y;		// Player starting position
dx=dy=0;
facing=4;			// this the index into the frames Image[]
frameindex=facing;
framecount=3;		// the delay between animation frame
walking = true;
} // END Player
	

public void update()  {

if (right && !walking)  {
	walking = true;
	dx = moveSpeed;
	frameindex = 5;
	facing = 4;

} else if (left && !walking) {
	walking = true;
	dx = -moveSpeed;
	frameindex = 9;
	facing = 10;

} else if (up && !walking) {
	walking=true;
	dy = -moveSpeed;
	frameindex = 0;
	facing = 1;

} else if (down && !walking) {
	walking = true;
	dy = moveSpeed;
	frameindex = 6;
	facing = 7;
	
} else if (!left && !right && !up && !down)  {		//no input don't move
	walking = false;
	dx=dy=0;
	frameindex = facing;

} else if ( game.playerAnimOn || map.collide(posx+dx+10,posy+dy+25) || map.collide(posx+dx+22,posy+dy+25))  {	// collides with vertical/horizontal borders
	walking = false;
	dx=dy=0;
	
} else {
	posx += dx;
	posy += dy;
	framecount--;
	
	up = false;
	down = false;
	right = false;
	left = false;
	
}
	
if (framecount < 0) { updateanimation(); } else {}
} // END update
	

public void paint(Graphics g)  {
switch (playerAnim) {
	case 1: // PLAYER TWIRLS ON ITSELF
		switch(playerAnimAt) {
			case 0: frameindex=6; playerAnimAt++; break;
			case 1: frameindex=5; playerAnimAt++; break;
			case 2: frameindex=0; playerAnimAt++; break;
			case 3: frameindex=9; playerAnimAt++; break;
			default: frameindex=6; playerAnim=0; game.playerAnimOn=false; break;
		}
	break;
	


	default: break;
}
g.drawImage(frames[frameindex],posx-map.camx,posy-map.camy,applet);
} // END paint


private void updateanimation()  {
if(dy<0) {

	frameindex ++;
	if(frameindex > 2)  
	frameindex = 0;

} else if (dy>0) {
	
	frameindex ++;
	if(frameindex > 8)  
	frameindex = 6;

} else if (dx>0) {
	
	frameindex ++;
	if(frameindex > 5) 
	frameindex = 3;

} else if(dx<0) {
	
	frameindex ++;
	if(frameindex > 11)  
	frameindex = 9;

}	
	framecount=3;
} // END updateanimation
	
	
	
	
	
	
	
	
	
	
	
	
	
}