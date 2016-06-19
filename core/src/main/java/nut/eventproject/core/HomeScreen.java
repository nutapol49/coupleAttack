package nut.eventproject.core;


import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class HomeScreen extends UIScreen {
    private final ScreenStack ss;
    private final GameplayScreen gameplayScreen;

    private Image bgImage;
    private ImageLayer bgLayer;

    private Image startButton;
    private ImageLayer startButtonLayer;

    private Image startButtonHolder;
    private ImageLayer startButtonHolderLayer;

    public HomeScreen(final ScreenStack ss) {
        this.ss = ss;
        this.gameplayScreen = new GameplayScreen(ss);

        bgImage = assets().getImage("images/bgcouple.png");
        this.bgLayer = graphics().createImageLayer(bgImage);


        startButton = assets().getImage("images/startButton.png");
        this.startButtonLayer = graphics().createImageLayer(startButton);

        startButtonHolder = assets().getImage("images/startButtonHolder.png");
        this.startButtonHolderLayer = graphics().createImageLayer(startButtonHolder);


       startButtonLayer.addListener(new Mouse.LayerAdapter(){

           @Override
           public void onMouseOut(Mouse.MotionEvent event) {

               startButtonHolderLayer.setVisible(false);
               startButtonLayer.setVisible(true);
               super.onMouseOut(event);
           }
            @Override
            public void onMouseOver(Mouse.MotionEvent event) {
                startButtonHolderLayer.setVisible(true);
               // startButtonLayer.setVisible(false);
                super.onMouseOver(event);
            }
           @Override
           public void onMouseUp(Mouse.ButtonEvent event){
               ss.push(gameplayScreen);
           }});
    }




    @Override
    public void wasShown(){
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.addAt(startButtonLayer,425f,240f);
        this.layer.addAt(startButtonHolderLayer,425f,240f);


    }
}
