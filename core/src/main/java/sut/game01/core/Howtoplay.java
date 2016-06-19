package sut.game01.core;


import org.jbox2d.common.Vec2;
import playn.core.*;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import static playn.core.PlayN.*;

public class Howtoplay extends UIScreen{
    private ScreenStack ss;
    private Image bgImage;
    private ImageLayer bg;


    public Howtoplay(final ScreenStack ss){
        this.ss = ss;

        bgImage = assets().getImage("images/how.png");
        bg = graphics().createImageLayer(bgImage);


        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) { //ดักสวิซ์ จาก keyboard ที่เรากด
                switch (event.key()) {
                    case ESCAPE:
                        ss.remove(ss.top());
                        ss.push(new HomeScreen(ss));
                        break;

                }
            }
        });




    }


    @Override
    public void wasShown(){
        super.wasShown();
        this.layer.add(bg);

    }

}
