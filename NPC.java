import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class NPC {
	
private Applet applet;
private Image[] frames;
private Map map;
private GameManager game;
public int numNPCs = 0;

int amtNPCFrames = 8;
public int posx[] =		{96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96}; //25
public int posy[] =		{96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96};
public int npcspeed[] =	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //1=normal, 2=med, 3=fast

//
public int npcAnimPattern[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // walk towards, etc
//
// which image to use from frames[]..
public int whichanim[]=		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
public int whichanimAt[]=	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
public int whichanimFAt[]=	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
public int whichanimFAtC[]=	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
public int npcDirection[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //0=right, 1=left, 2=down, 3=up


public NPC (Image [] f, Map m, Applet a, GameManager g)  {
	frames = f;
	applet = a;
	map = m;
	game=g;
} // END NPC
	

public void addNPC(int x, int y, int fn, int s, int p) {
	posx[numNPCs]=x;
	posy[numNPCs]=y;
	whichanim[numNPCs]=fn;
	whichanimAt[numNPCs] = (whichanim[numNPCs]*amtNPCFrames)+6;
	npcspeed[numNPCs]=s;
	npcAnimPattern[numNPCs]=p;
	game.playNPCs=true;
	numNPCs++;
}


public void update() {
for (int nn=0; nn<numNPCs; nn++) {

switch (npcAnimPattern[nn]) {
case 0: // WANDER RANDOMLY IN MAP
	switch (npcDirection[nn]) {
		case 0://right
			if ( map.NPCcollide(posx[nn]+npcspeed[nn],posy[nn]) ) { rDirection(nn);
			} else { if ((int)(Math.random() * 33)>0) { posx[nn]=posx[nn]+npcspeed[nn]; } else { rDirection(nn); } }
			break;
		case 1://left
			if ( map.NPCcollide(posx[nn]-npcspeed[nn],posy[nn]) ) { rDirection(nn);
			} else { if ((int)(Math.random() * 33)>0) { posx[nn]=posx[nn]-npcspeed[nn]; } else { rDirection(nn); } }
			break;	
		case 2://down
			if ( map.NPCcollide(posx[nn],posy[nn]+npcspeed[nn]) ) { rDirection(nn);
			} else { if ((int)(Math.random() * 33)>0) { posy[nn]=posy[nn]+npcspeed[nn]; } else { rDirection(nn); } }
			break;	
		case 3://up
			if ( map.NPCcollide(posx[nn],posy[nn]-npcspeed[nn]) ) { rDirection(nn);
			} else { if ((int)(Math.random() * 33)>0) { posy[nn]=posy[nn]-npcspeed[nn]; } else { rDirection(nn); } }
			break;		
		default: break;
	}
break;

case 1: // DON'T MOVE, JUST LOOK AROUND
if ((int)(Math.random() * 33)>0) {} else { rDirection(nn); } whichanimFAtC[nn]=0;
break;

default: break;
}

// FOR MOVING FRAMES
if (whichanimFAt[nn]<5) { whichanimFAt[nn]++; } else {
switch (npcDirection[nn]) {	
	case 0: //right
	if (whichanimFAtC[nn]==0) { whichanimFAtC[nn]=1; whichanimAt[nn] = (whichanim[nn]*amtNPCFrames)+3;
	} else { whichanimFAtC[nn]=0; whichanimAt[nn] = (whichanim[nn]*amtNPCFrames)+2; }
	break;
	
	case 1: //left
	if (whichanimFAtC[nn]==0) { whichanimFAtC[nn]=1; whichanimAt[nn] = (whichanim[nn]*amtNPCFrames)+7;
	} else { whichanimFAtC[nn]=0; whichanimAt[nn] = (whichanim[nn]*amtNPCFrames)+6; }
	break;	

	case 2: //up
	if (whichanimFAtC[nn]==0) { whichanimFAtC[nn]=1; whichanimAt[nn] = (whichanim[nn]*amtNPCFrames)+5;
	} else { whichanimFAtC[nn]=0; whichanimAt[nn] = (whichanim[nn]*amtNPCFrames)+4; }
	break;	
	
	default: //down
	if (whichanimFAtC[nn]==0) { whichanimFAtC[nn]=1; whichanimAt[nn] = (whichanim[nn]*amtNPCFrames)+1;
	} else { whichanimFAtC[nn]=0; whichanimAt[nn] = (whichanim[nn]*amtNPCFrames)+0; }
	break;
}
whichanimFAt[nn]=0;
}
// END FOR MOVING FRAMES

}// END FOR
} // end update


public void paint(Graphics g)  {
for (int zz=0; zz<numNPCs; zz++) {
	g.drawImage(frames[whichanimAt[zz]],posx[zz]-map.camx,posy[zz]-map.camy,applet);
}
} // END paint



public void rDirection(int s) {
switch ((int)(Math.random() * 4)) {
	case 0: //right
		npcDirection[s]=0;
		break;
	case 1: //left
		npcDirection[s]=1;
		break;
	case 2: //up
		npcDirection[s]=3;
		break;
	default: //down
		npcDirection[s]=2;
		break;
}} // END rDirection


}


