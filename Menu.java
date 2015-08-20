import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.applet.AudioClip;

public class Menu {

Image[] itemImg;
Image[] spellImg;

private Applet applet;
private GameManager game;
private Player player;
public int menuSwitch = 0;

private String invList = "";
private int invListAt; // also amount of different items held in inv
public int cursorAt;
private int menuItemSelected=0;
	

public Menu(Applet a, GameManager g, Image[] i, Image[] s)  {
applet=a;
game=g;
itemImg=i;
spellImg=s;
} // END Menu


public void update() {}
	
	
public void paint(Graphics g)  {
    if (game.menuOn) {
    	switch (menuSwitch) {
        		
       	case 1: // USE ITEM
			if (game.playerInventory[menuItemSelected]>0) {
				game.useItem(menuItemSelected);
       		} else {}
       			menuSwitch=0;
       		break;
        		
		default: // PRINT MENU AT
			invListAt=0;
		   	g.setColor(Color.red);
			g.fillRect(100,110, 650, 424);
       	   	g.setColor(Color.white);
       	    g.setFont(game.font_bold);
    		g.drawString("[ RPGEngine Menu 1.0 ] ", 145, 150);
					
			for (int i=0; i<game.itemMax; i++) {
			
       	    	if (game.playerInventory[i]>0) {
       	    		invListAt++;
						
       	    			if (cursorAt!=invListAt) {
       	    				g.setColor(Color.white);
       	    				g.setFont(game.font_plain);
       	    			} else {
       	    				menuItemSelected=i;
       	    				g.setColor(Color.black);
       	    				g.setFont(game.font_bold);
       	    			}
       	    			
       	    		invList="["+game.playerInventory[i]+"] "+game.itemNames[i]+": "+game.itemDescriptions[i];
       	    		g.drawString(invList, 155, 150+(invListAt*20));
				} else {}
				
			}
			if (invListAt>0) { // is not empty
				if (cursorAt>invListAt) { cursorAt=0;
				} else if (cursorAt<0) { cursorAt=invListAt;
				} else {}
			} else { // is empty
				g.setFont(game.font_plain);
				g.drawString("Inventory is empty.", 155, 170);
			}
			break;
    	}
    } else {} // ELS EMENU IS OT ON
} // END paint



} // END Menu
