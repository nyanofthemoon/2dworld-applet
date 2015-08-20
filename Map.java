import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.StringTokenizer;
import java.io.*;
import java.net.*;

public class Map {
URL path;
	
private final int NUMTILES = 100;
public final int TILE_W = 48;
public final int TILE_H = 48;
private int MAP_W=17;
private int MAP_H=13;
private final int SCREEN_W = 800;
private final int SCREEN_H = 600;
private Applet applet;
private Image[] tiles;
private Image[] atiles;
public int camx,camy, tilex, tiley;
private int offsetx,offsety;
	
// MAP LOADING
public GameManager game;
public int loadX,loadY=96;
private int mapgemX=16;
private int mapgemY=12;
public int[][] mapTileData;
public String[][] mapTypeData;
//

// CONTAINER-RELAYTED
public int containerItemGet;
public int containerItemAmtGet;
public int containerDataType;
public int containerTileData;
public int containerSound;
public int containerType;
public int containerX;
public int containerY;

public Map(Image[] s, Applet a, URL u, GameManager p, Image[] t)  {
applet=a;
tiles=s;
atiles=t;
path=u;
game=p;
} // END Map


public void update(int x, int y)  {
camx=x-(SCREEN_W/2+24);
camy=y-(SCREEN_H/2+24);
if(camx<0) { camx=0; } else {}
if (camx>(MAP_W*TILE_W-SCREEN_W)) { camx=MAP_W*TILE_W-SCREEN_W; } else {}
if(camy<0) { camy=0; } else {}
if (camy>(MAP_H*TILE_H-SCREEN_H)) { camy=MAP_H*TILE_H-SCREEN_H; } else {}
} // END update


public void paint(Graphics g)  {
tilex=camx/TILE_W;
tiley=camy/TILE_H;
offsetx=TILE_W-camx%TILE_W;
offsety=TILE_H-camy%TILE_H;
	for(int i=0;i<12;i++) {
		for(int j=0;j<16;j++) { g.drawImage(tiles[mapTileData[i+tiley][j+tilex]],offsetx+j*TILE_W, offsety+i*TILE_H, applet); }
	}
} // END paint()


public boolean NPCcollide(int x, int y)  {
boolean cannotcont = false;
String whata=mapTypeData[(y+25)/TILE_H-1][(x+10)/TILE_W-1];
String whatb=mapTypeData[(y+25)/TILE_H-1][(x+22)/TILE_W-1];

if (whata.length() < 2 && Integer.parseInt(whata) == 1) {
		cannotcont=true;
		game.playSound(false, 0); // beeps when colliding!
} else {
	if (whatb.length() < 2 && Integer.parseInt(whatb) == 1) {
		cannotcont=true;
		game.playSound(false, 0); // beeps when colliding!
	} else {}
}

return cannotcont;
}


public boolean collide(int x, int y)  {
boolean cannotwalk = false;
int wch = 0;
String what=mapTypeData[y/TILE_H-1][x/TILE_W-1];
String whatt[]=game.SDetokenize(what, "&");
if (what.length() < 2) { wch=Integer.parseInt(what); } else { wch = Integer.parseInt(whatt[0]); }
game.containerAvail=false;

switch ( wch ) {
	case 1: // CANNOT WALK ON TILE
	cannotwalk=true;
	break;
	
	case 2: // teleport in map; one-way or two-way option
	double yy= (double)(y % TILE_H) / TILE_H;
	double xx= (double)(x % TILE_W) / TILE_W;
	if ( xx > 0.3 && xx < 0.7  &&  yy > 0.3 && yy < 0.7 ) { // IF WE ARE 33% INSIDE A TILE
		if (Integer.parseInt(whatt[5])==-1) { game.teleport(Integer.parseInt(whatt[1]),Integer.parseInt(whatt[2]),false); } else { game.teleport(Integer.parseInt(whatt[1]),Integer.parseInt(whatt[2]),true); }
		if (Integer.parseInt(whatt[3])==-1) {} else { game.changeTypeData((x/TILE_W-1),(y/TILE_H-1),whatt[3]); }
		if (Integer.parseInt(whatt[4])==-1) {} else { game.changeTileImg((x/TILE_W-1),(y/TILE_H-1),Integer.parseInt(whatt[4])); }
	} else {}
	break;

	case 3: // floor switch; walk on to change a/several tile typeData and/or tileData, use @ to separate amt of switches
	double yya= (double)(y % TILE_H) / TILE_H;
	double xxa= (double)(x % TILE_W) / TILE_W;
	if ( xxa > 0.3 && xxa < 0.7  &&  yya > 0.3 && yya < 0.7 ) { // IF WE ARE 33% INSIDE A TILE
		int amts = whatt.length;
		if (Integer.parseInt(whatt[1])==-1) {} else { game.playSound(false, Integer.parseInt(whatt[1])); }
			for (int mf=2; mf<amts; mf++) {
				int whattt[]=game.Detokenize(whatt[mf], "@");
				if (whattt[2]==-1) {} else { game.changeTypeData(whattt[0],whattt[1],""+whattt[2]); }
				if (whattt[3]==-1) {} else { game.changeTileImg(whattt[0],whattt[1],whattt[3]); }
			}
	} else {}
	break;

	case 4: // teleport to other map: loads player into a new map
	double yyb= (double)(y % TILE_H) / TILE_H;
	double xxb= (double)(x % TILE_W) / TILE_W;
	if ( xxb > 0.3 && xxb < 0.7  &&  yyb > 0.3 && yyb < 0.7 ) { // IF WE ARE 33% INSIDE A TILE
		loadMap(Integer.parseInt(whatt[1]));
	} else {}
	break;
	
	case 5: // container?
	double yyc= (double)(y % TILE_H) / TILE_H;
	double xxc= (double)(x % TILE_W) / TILE_W;
	if ( xxc > 0.1 && xxc < 0.9  &&  yyc > 0.1 && yyc < 0.9 ) { // IF WE ARE 33% INSIDE A TILE
		cannotwalk=true;
		containerX=x/TILE_W-1;
		containerY=y/TILE_H-1;
		containerDataType=Integer.parseInt(whatt[1]);
		containerTileData=Integer.parseInt(whatt[2]);
		containerSound=Integer.parseInt(whatt[3]);
		containerType=Integer.parseInt(whatt[4]);
		containerItemAmtGet= Integer.parseInt(whatt[5]);
		containerItemGet=Integer.parseInt(whatt[6]);
		game.containerAvail=true;
	} else {}
	break;
	
	
	
	
	
	default: break; // CAN WALK ON TILE - 0
}
return cannotwalk;
} // END collide



















public void loadMap(int w)  {
String rawMap[] = new String[500];
// LOAD MAP.TXT DATA //
try {
	String rawLine = "";
	int rawAt = 0;
    URLConnection connection = new URL(path + "map"+w+".txt").openConnection();
    DataInputStream byteReader = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
   		while ((rawLine = byteReader.readLine()) != null) {  
    		rawMap[rawAt] = rawLine;
    		rawAt++;
   		}   		
 } catch (Exception rscex) {}

MAP_W=Integer.parseInt(rawMap[0]);
MAP_H=Integer.parseInt(rawMap[1]);
if (MAP_W==17) { mapgemX=16; } else { mapgemX=MAP_W; }
if (MAP_H==13) { mapgemY=12; } else { mapgemY=MAP_H; }

mapTileData=new int[mapgemY][mapgemX];
mapTypeData=new String[mapgemY][mapgemX];

for(int i=0;i<mapgemY;i++) {
	int mapAt[] = game.Detokenize(rawMap[6+i],"*");
	String mapData[] = game.SDetokenize(rawMap[7+mapgemY+i],"*");
		for(int j=0;j<mapgemX;j++) {
				mapTileData[i][j]=mapAt[j];
				mapTypeData[i][j]=mapData[j];
		}}
loadX=Integer.parseInt(rawMap[2]);
loadY=Integer.parseInt(rawMap[3]);
if (Integer.parseInt(rawMap[4])==-1) {} else {
	for (int s=0; s<game.audiolgt; s++) { game.stopSound(s); }
	game.playSound(true, Integer.parseInt(rawMap[4]));
}
game.teleport(loadX, loadY, false);
} // END loadMap




} // END Map
