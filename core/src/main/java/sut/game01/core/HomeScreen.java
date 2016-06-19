package sut.game01.core;


import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import static playn.core.PlayN.*;

public class HomeScreen extends UIScreen{
    private ScreenStack ss;
    private Image bgImage;
    private ImageLayer bg;
    private Image b0Image;
    private ImageLayer b0;
    private Image howImage;
    private ImageLayer howIcon;

    public HomeScreen(final ScreenStack ss){
        this.ss = ss;

         bgImage = assets().getImage("images/bgcouple.png");
         bg = graphics().createImageLayer(bgImage);

        b0Image = assets().getImage("images/StartButton.png");
        b0 = graphics().createImageLayer(b0Image);
        b0.setTranslation(450,250);

        howImage = assets().getImage("images/howButton.png");
        howIcon = graphics().createImageLayer(howImage);
        howIcon.setTranslation(445,350);

        howIcon.addListener(new Mouse.LayerAdapter(){
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
                ss.push(new Howtoplay(ss));
            }
        });

        b0.addListener(new Mouse.LayerAdapter(){
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
                ss.push(new GameplayScreen(ss));
            }
        });




    }


@Override
    public void wasShown(){
    super.wasShown();
    this.layer.add(bg);
    this.layer.add(b0);
    this.layer.add(howIcon);
}

}
