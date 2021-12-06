package main.java;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameStateMenu extends GameState {

    private static final String[] options = { "PLAY", "CREDITS", "INSTRUCTION", "EXIT" };

    private BufferedImage logo;
    private Font textFont;
    private Color textColor;
    private int currentOption;
    private boolean showingCredits;
    private boolean showingInstruc;

    public GameStateMenu() {
        super();
        textFont = TextFont.getFont();
        definePanelSize(WindowManager.windowWidth, WindowManager.WINDOW_HEIGHT);
        resetAllCheckpoints();
        Sound.stopAllSounds();
    }

    private void resetAllCheckpoints(){
        GameStateLevel1.resetCheckpoint();
        GameStateLevel2.resetCheckpoint();
        GameStateLevel3.resetCheckpoint();
    }

    @Override
    protected void loadImages() {
        getBackground("menu");
        logo = SpriteAssets.getLogo();
    }

    @Override
    protected void paintElements(Graphics g) {
        paintBackground(g);
        paintLogo(g);
        paintMenuOptions(g);
        if(showingCredits){
            paintCredits(g);
        }
        else if(showingInstruc){
        	paintInstructions(g);
        }

    }

    private void paintLogo(Graphics g) {
        int xLogoCoordinate = (WindowManager.windowWidth - logo.getWidth()) / 2;
        g.drawImage(logo, xLogoCoordinate, 80, GameRunner.instance);
    }

    private void paintMenuOptions(Graphics g) {
        for (int i = 0; i < options.length; i++) {
            getSelectedOption(i);
            g.setFont(textFont);
            g.setColor(textColor);
            g.drawString(options[i], WindowManager.windowWidth / 2 - 100, 470 + i * 60);
        }
    }

    private void paintCredits(Graphics g){
        g.setFont(textFont.deriveFont(Font.PLAIN, 15));
        g.setColor(Color.WHITE);
        g.drawString("THIS PROJECT WAS DONE BY", 150, 500);
        g.drawString("NHÓM 3", 180, 530);
    }
    
    private void paintInstructions(Graphics g){
        g.setFont(textFont.deriveFont(Font.PLAIN, 15));
        g.setColor(Color.WHITE);
		g.drawString("INSTRUCTIONS", 100, 75);
		g.drawLine(75, 110, 75, 140);
		g.fillPolygon(new int []{65,85,75}, new int[] {110,110,93}, 3);
		g.drawLine(76, 180, 106, 180);
		g.fillPolygon(new int []{76,76,59}, new int[] {170,190,180}, 3);
		g.drawLine(54, 240, 84, 240);
		g.fillPolygon(new int []{84,84,101}, new int[] {230,250,240}, 3);
		g.drawString("K", 65, 315);
		g.drawString("W", 65, 378);
		g.drawString("X", 65, 438);
		g.drawString("_",200,108);
		g.drawString("_",200,178);
		g.drawString("_",200,238);
		g.drawString("_",200,302);
		g.drawString("_",200,361);
		g.drawString("_",200,421);
		g.drawString("JUMP",240,122);
		g.drawString("MOVE LEFT",240,192);
		g.drawString("MOVE RIGHT",240,252);
		g.drawString("PAUSE",240,316);
		g.drawString("RESUME", 240, 378);
		g.drawString("FIRE (IN FIRE FORM)", 240, 438);
    }

    private void getSelectedOption(int iterator) {
        if (iterator == currentOption) {
            textFont = textFont.deriveFont(Font.BOLD, 30);
            textColor = Color.GREEN;
        } else {
            textFont = textFont.deriveFont(Font.PLAIN, 30);
            textColor = Color.WHITE;
        }
    }

    @Override
    protected void keyReleased(int k) {
    }

    @Override
    protected void tick() {

    }

    @Override
    protected void spawnMario() {
        // No need to spawn mario
    }
    
    @Override
    protected void keyPressed(int k) {
        switch (k) {
            case KeyEvent.VK_DOWN:
                if (currentOption++ == options.length - 1)
                    currentOption = 0;
                break;

            case KeyEvent.VK_UP:
                if (currentOption-- == 0)
                    currentOption = options.length - 1;
                break;
            case KeyEvent.VK_ENTER:
                applySelectedOption();
                break;
            default:
                break;
        }

    }

    private void applySelectedOption() {
        switch (currentOption) {
            case 0:
                requestNextLevel();
                break;
            case 1:
                showingCredits = !showingCredits;
                break;
            case 2:
            	showingInstruc = !showingInstruc;
            	break;
            case 3:
                System.exit(0);
                break;
            default:
                break;
        }
    }


    @Override
    public void requestNextLevel() {
        GameState newGameState = new GameStateLevel1();
        gameRunner.setCurrentGameState(newGameState);
     }
}
