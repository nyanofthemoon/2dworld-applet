import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.applet.AudioClip;
import java.io.*;

public class RPGEngine extends java.applet.Applet implements Runnable {

// GLOBAL VARIABLES //
URL path;
int tileSize = 48;
int charSize = 32;
int itemSize = 16; // for items and spells
int faceSize = 64;
int amtTiles = 150; // amount of tiles per row
int amtATiles = 20; // amount of tiles per row
int amtATilesFrames = 6; // amount of frames per different animated tile
int amtDiffPChars = 10; // amount of players per row, almost amount of faces
int amtDiffPFaces = 5; // amount of faces per different player
int amtDiffPFrames = 12; // amount of frames per different player
int amtDiffNPC = 30; // amount of NPC per row
int amtNPCFrames = 8; // amount of frames per different NPC
int amtItemsPic = 100;
int amtSpellsPic = 100;

int gameWidth = 800;//tilesX*tileSize;
int gameHeight = 600;//tilesY*tileSize;

// SAVED VARS //
public int wmap;
public String wgame;

// // // // // // // //
Thread animation = null;
GameManager game;
long nextTime;
long loopInterval=50;
Graphics backbuffergc;
Image backbuffer;
Image tiles[] = new Image [amtTiles];
Image atiles[] = new Image [amtATiles*amtATilesFrames];
Image plr_faces[] = new Image [amtDiffPChars*amtDiffPFaces];
Image plr_frames[] = new Image [amtDiffPChars*amtDiffPFrames];
Image npc_frames[] = new Image [amtDiffNPC*amtNPCFrames];
Image item_pic[] = new Image [amtItemsPic];
Image spell_pic[] = new Image [amtSpellsPic];

// FOR SOUNDS
public AudioClip audio[] = new AudioClip[25];

// FOR .TXTLOADING VARIABLES //
private String rawData[] = new String[100];


public void init() {
	System.gc(); // RUN GARBAGE COLLECTION NOW!
	wmap = Integer.parseInt(getParameter("wmap")); // lastSavedMap
	wgame = getParameter("id"); // which game to load
	
	loadResources();
	game = new GameManager(this, audio, rawData, path, wmap, animation, tiles, atiles, plr_faces, plr_frames, npc_frames, item_pic, spell_pic);
	this.addKeyListener(game);
	backbuffer = createImage(gameWidth, gameHeight);
	backbuffergc = backbuffer.getGraphics();
}


public void start () {
	animation = new Thread(this);
	if (animation != null) {
		animation.start();
		game.animation=animation;
	} else {
		nextTime=System.currentTimeMillis()+loopInterval;
	}
}


public void stop () {
	if (animation != null) {
		animation.stop();
		animation=null;
	} else {		
	}
}


public void run () {
while (true) {
	try {
		animation.sleep(Math.max(25,nextTime-System.currentTimeMillis()));
	} catch (InterruptedException e) {}
	nextTime=System.currentTimeMillis()+loopInterval;	
	
	game.update();
	repaint();
	Thread.currentThread().yield();
}
}



public void paint(Graphics g) {
backbuffergc.setColor(Color.black);
backbuffergc.fillRect(0,0, gameWidth, gameHeight);
game.paint(backbuffergc);
g.drawImage(backbuffer,0,0,gameWidth,gameHeight,tileSize,tileSize,gameWidth,gameHeight,this);
}


public void update(Graphics g) {
	paint(g);
}



// LOADS STUFF //

public void loadResources () {
try { path = new URL(getCodeBase()+wgame+"/"); } catch (Exception malurlex) {}
MediaTracker t = new MediaTracker (this);
Image imagedl0=getImage(path,"tiles.gif");
Image imagedl1=getImage(path,"atiles.gif");
Image imagedl2=getImage(path,"plr.gif");
Image imagedl3=getImage(path,"plrf.gif");
Image imagedl4=getImage(path,"npc.gif");
Image imagedl5=getImage(path,"items.gif");
Image imagedl6=getImage(path,"spells.gif");
t.addImage(imagedl0,0);
t.addImage(imagedl1,1);
t.addImage(imagedl2,2);
t.addImage(imagedl3,3);
t.addImage(imagedl4,4);
t.addImage(imagedl5,5);
t.addImage(imagedl6,6);
try { t.waitForAll(); } catch (InterruptedException e) {}

// LOAD MAP NON-ANIMATED GRAPHICS //
ImageProducer imgsrc = imagedl0.getSource();
for (int i=0;i<amtTiles;i++) {
		ImageFilter getsubimage=new CropImageFilter(i*tileSize,0,tileSize,tileSize);
		ImageProducer producer=new FilteredImageSource(imgsrc,getsubimage);
		tiles[i]=createImage(producer);
}
// END LOAD MAP NON-ANIMATED GRAPHICS //

// LOAD MAP ANIMATED GRAPHICS //
imgsrc = imagedl1.getSource();
for (int i=0;i<amtATiles;i++) {
		ImageFilter getsubimage=new CropImageFilter(i*tileSize,0,tileSize,tileSize);
		ImageProducer producer=new FilteredImageSource(imgsrc,getsubimage);
		atiles[i]=createImage(producer);
}
// END LOAD MAP ANIMATED GRAPHICS //

// LOAD PLAYER FRAMES GRAPHICS //
imgsrc = imagedl2.getSource();
for (int i=0;i<(amtDiffPChars*amtDiffPFrames);i++) {
		ImageFilter getsubimage=new CropImageFilter(i*charSize,0,charSize,charSize);
		ImageProducer producer=new FilteredImageSource(imgsrc,getsubimage);
		plr_frames[i]=createImage(producer);
}
// END LOAD PLAYER FRAMES GRAPHICS //

// LOAD PLAYER FACES GRAPHICS //
imgsrc = imagedl3.getSource();
for (int i=0;i<(amtDiffPChars*amtDiffPFaces);i++) {
		ImageFilter getsubimage=new CropImageFilter(i*faceSize,0,faceSize,faceSize);
		ImageProducer producer=new FilteredImageSource(imgsrc,getsubimage);
		plr_faces[i]=createImage(producer);
}
// END LOAD PLAYER FACES GRAPHICS //

// LOAD NPC GRAPHICS //
imgsrc = imagedl4.getSource();
for (int i=0;i<(amtDiffNPC*amtNPCFrames);i++) {
		ImageFilter getsubimage=new CropImageFilter(i*charSize,0,charSize,charSize);
		ImageProducer producer=new FilteredImageSource(imgsrc,getsubimage);
		npc_frames[i]=createImage(producer);
}
// END LOAD NPC GRAPHICS //

// LOAD ITEMS GRAPHICS //
imgsrc = imagedl5.getSource();
for (int i=0;i<amtItemsPic;i++) {
		ImageFilter getsubimage=new CropImageFilter(i*itemSize,0,itemSize,itemSize);
		ImageProducer producer=new FilteredImageSource(imgsrc,getsubimage);
		item_pic[i]=createImage(producer);
}
// END LOAD ITEMS GRAPHICS //

// LOAD SPELLS GRAPHICS //
imgsrc = imagedl6.getSource();
for (int i=0;i<amtSpellsPic;i++) {
		ImageFilter getsubimage=new CropImageFilter(i*itemSize,0,itemSize,itemSize);
		ImageProducer producer=new FilteredImageSource(imgsrc,getsubimage);
		spell_pic[i]=createImage(producer);
}
// END LOAD SPELLS GRAPHICS //
	
// LOAD AUDIO //
audio[0] = getAudioClip(path, "menuUseItem.mid"); // playSound(false, 0);
audio[1] = getAudioClip(path, "menuMove.mid"); // playSound(false, 1);
audio[2] = getAudioClip(path, "menuOpen.mid"); // playSound(false, 2);
audio[3] = getAudioClip(path, "menuClose.mid"); // playSound(false, 3);
audio[4] = getAudioClip(path, "mapTeleport.mid"); // playSound(false, 4);
audio[5] = getAudioClip(path, "switchOn.mid"); // playSound(false, 5);
// END LOAD AUDIO //
	
// LOAD .TXT DATA into rawData[] //
try {
	String rawLine = "";
	int rawAt = 0;
    URLConnection connection = new URL(path + "src.txt").openConnection();
    DataInputStream byteReader = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
   		while ((rawLine = byteReader.readLine()) != null) {  
    		rawData[rawAt] = rawLine;
    		rawAt++;
   		}
   		
} catch (Exception rscex) {}   
// END LOAD .TXT DATA // 
	
} // END loadResources


} // END RPGEngine













