import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.StringTokenizer;
import java.net.*;

public class GameManager implements KeyListener {
	
public int playerInventory[];
	
// ITEMS //
public int itemMax;
public String itemNames[];
public String itemDescriptions[];
public int maxCarriedItem[]; // maximum amont of a specific item player can carry
	
// FONTS //
public Font font_plain = new Font("Verdana", Font.PLAIN, 10);
public Font font_bold = new Font("Verdana", Font.BOLD, 10);
//
public Thread animation = null;
private Applet applet;
private Map map;
private Menu menu;
public Player player;
public AudioClip audio[];
public int audiolgt;

// GAMME STATES //
public boolean menuOn=false;
public boolean battleOn=false;
public boolean containerAvail=false;
public boolean playerAnimOn=false;
// GAME DATA //


// NPCS //
public boolean playNPCs = false;
public NPC npcs;


public GameManager(Applet a, AudioClip[] au, String rawData[], URL p, int lastSavedMap, Thread t, Image[] tiles, Image[] atiles, Image[] plr_faces, Image[] plr_frames, Image[] npc_frames, Image[] item_pic, Image[] spell_pic)  {
applet=a;
animation = t;
map = new Map (tiles, a, p, this, atiles);
player = new Player(plr_frames, map, applet, map.loadX, map.loadY, this, plr_faces);
audio=au;
audiolgt=audio.length;
map.loadMap(lastSavedMap);
menu = new Menu(a, this, item_pic, spell_pic);
npcs = new NPC(npc_frames, map, applet, this);
		
// LOAD VARIABLES ! //
String rawItem[] = SDetokenize(rawData[0], "&");
itemMax = rawItem.length;
playerInventory=new int[itemMax];
itemNames = new String[itemMax];
itemDescriptions = new String[itemMax];
maxCarriedItem = new int[itemMax];
	
for (int i=0; i<itemMax; i++) {
	String rawItemInfo[] = SDetokenize(rawItem[i], "*");
	itemNames[i]=rawItemInfo[0];
	itemDescriptions[i]=rawItemInfo[1];
	maxCarriedItem[i]=Integer.parseInt(rawItemInfo[2]);
	
	
	
	playerInventory[i]=3; // load inventory for tests 
}
} // END GameManager LOAD
	

	
public void update()  {
if (!menuOn && !battleOn) {
	player.update();
	map.update(player.posx,player.posy);
	if (playNPCs) { npcs.update(); } else {}
} else {
	menu.update();
}
} // END update

	
public void paint(Graphics g)  {
if (!menuOn && !battleOn) {
	map.paint(g);
	player.paint(g);
	if (playNPCs) { npcs.paint(g); } else {}
} else {
	 menu.paint(g);
}
} // END paint
	
	
public void keyTyped (KeyEvent e) {}


public void keyPressed (KeyEvent e) {

// FOR TESTS ONLY
if (e.getKeyCode() == KeyEvent.VK_A)  {

	npcs.addNPC((48*(int)(2+Math.random() * 8)),(48*(int)(2+Math.random() * 8)),(int)(Math.random() * 4),(int)(1+Math.random() * 3),(int)(Math.random() * 2));

} else {}


if (e.getKeyCode() == KeyEvent.VK_B)  {
	
} else {}

	
if (e.getKeyCode() == KeyEvent.VK_UP)  {
	if(!menuOn && !battleOn) {
		if (!player.down) {
			player.up = true;
		} else {}
	} else { // we are in menu
		playSound(false, 1);
		menu.cursorAt--;
	}
} else {}

if(e.getKeyCode() == KeyEvent.VK_DOWN)  {
	if(!menuOn  && !battleOn) {
		if (!player.up) {
			player.down = true;
		} else {}
	} else { // we are in menu
		playSound(false, 1);
		menu.cursorAt++;
	}
} else {}

if(e.getKeyCode() == KeyEvent.VK_LEFT) {
	if(!menuOn  && !battleOn) {
		if (!player.right) {
			player.left = true;
		} else {}
	} else { // we are in menu
	}
} else {}

if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
	if(!menuOn  && !battleOn) {
		if (!player.left) {
			player.right = true;
		} else {}
	} else { // we are in menu
	}
} else {}
		

// LOAD MENU
if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		if(!menuOn && !battleOn) {
			playSound(false, 2);
			menu.menuSwitch=0; // PRINT MENU
			menuOn=true;
		} else {
			playSound(false, 3);
			menu.cursorAt = 0;
			menuOn=false;
		}
} else {}
//
		
// MAKE SELECTION 
if(e.getKeyCode() == KeyEvent.VK_SPACE) {
	if(menuOn  && !battleOn) { // menu on
		menu.menuSwitch=1; // SELECTED AN ITEM
	} else { // menu not on map action open container if collide?
		
		if (!containerAvail) {} else {
			switch ( map.containerType ) {
				case 0: // ADD AMT ITEM IN INVENTORY
				containerAvail=false;
				if (map.containerTileData==-1) {} else { changeTileImg( map.containerX, map.containerY, map.containerTileData ); }
				if (map.containerDataType==-1) {} else { changeTypeData( map.containerX , map.containerY, ""+map.containerDataType ); }
				if (map.containerSound==-1) {} else { playSound(false, map.containerSound); }
				addItemInventory(map.containerItemGet, map.containerItemAmtGet);
				break;
		
			case 1: // SOMETHING ELSE
			break;
				
			default: break;
		
		}}
	
	
	}
} else {}
//	

// CANCEL ACTION
if (e.getKeyCode() == KeyEvent.VK_SHIFT) {

} else {}
//	
} // END keyPressed


public void keyReleased (KeyEvent e) {
// DO THIS OTHERWISE YOU GET A CRAPPY KEY CHANGING DIRECTION (WON'T WORK IF 2 KEYS ARE PRESSED TOGETHER...
if(!menuOn && !battleOn) {
	if(e.getKeyCode() == KeyEvent.VK_UP) { player.up = false; player.walking = true;
	} else if(e.getKeyCode() == KeyEvent.VK_DOWN) { player.down = false; player.walking = true;
	} else if(e.getKeyCode() == KeyEvent.VK_LEFT) { player.left = false; player.walking = true;
	} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) { player.right = false; player.walking = true;
	} else {}
} else {}
// /// // / // 
} // END keyReleased
    


// USABLE IN GAME SUBS //

public void loadNewMap(int w) {
	map.loadMap(w);
}


public void teleport(int x, int y, boolean soundOn) {
	if (soundOn) { playSound(false, 4); } else {}
	map.loadX=x*map.TILE_W;
	map.loadY=y*map.TILE_H;
	player.posx=x*map.TILE_W;
	player.posy=y*map.TILE_H;
} // END teleport


public void playSound(boolean loop, int i) {
	if (loop) { audio[i].loop();
    } else { audio[i].play(); }
} // END playSound


public void stopSound(int i) {
	audio[i].stop();
} // END stopSound


public void useItem(int i) {
if (playerInventory[i]>0) {
	removeItemInventory(i, 1);
    playSound(false, 0);
} else {}
}


public void removeItemInventory(int w, int a) { // removeItemInventory(X, -1) removes all of X
	if (a>-1 && (playerInventory[w]-a) > 0) {
		playerInventory[w]=playerInventory[w]-a;
	} else {
		playerInventory[w]=0;
	}
}


public void addItemInventory(int w, int a) {
	if (a>-1 && (playerInventory[w]+a) < maxCarriedItem[w]) { // addItemInventory(X, -1) adds max of X
		playerInventory[w]=playerInventory[w]+a;
	} else {
		playerInventory[w]=maxCarriedItem[w];
	}
}


public void changeMoveSpeed(int s) { player.moveSpeed=s; }

public void doIdle(int l) { try { animation.sleep(l*100); } catch (java.lang.InterruptedException threx){} }

public void playerAnimEffect(int a) { player.playerAnim=a; player.playerAnimAt=0; }


// END USABLE IN GAME SUBS //

public void changeTileImg(int x, int y, int i) { map.mapTileData[y][x]=i; }
public void changeTypeData(int x, int y, String i) { map.mapTypeData[y][x]=i; }


// OTHER LOADING SUBS HERE //


public final String[] SDetokenize(String rawtk, String w) {
StringTokenizer dtk = new StringTokenizer(rawtk,w,false);
String[] da = new String[dtk.countTokens()];
int s = 0;
while (dtk.hasMoreTokens()) {
da[s] = dtk.nextToken();
s = s + 1; }
return (da); }


public final int[] Detokenize(String rawts, String w) {
StringTokenizer dd = new StringTokenizer(rawts,w,false);
int[] df = new int[dd.countTokens()];
int d = 0;
while (dd.hasMoreTokens()) {
df[d] = Integer.parseInt(dd.nextToken());
d = d + 1; }
return (df); }


public final String ReplaceString(String str, String pattern, String replace) {
int slen = str.length();
int plen = pattern.length();
int s = 0, e = 0;
StringBuffer result = new StringBuffer(slen);
char[] chars = new char[slen];

while ((e = str.indexOf(pattern, s)) >= 0) {
  str.getChars(s, e, chars, 0);
  result.append(chars, 0, e - s).append(replace);
  s = e + plen;
}
str.getChars(s, slen, chars, 0);
result.append(chars, 0, slen - s);
return result.toString();
}

} // END GameManager

