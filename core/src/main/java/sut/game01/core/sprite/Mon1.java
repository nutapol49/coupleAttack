package sut.game01.core.sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.GameplayScreen;

public class Mon1 {
    private Sprite sprite;
    private  int spriteIndex = 0;
    private boolean hasLoaded = false;


    public enum State{
        RUN
    };

    private State state = State.RUN;
    private  int e = 0;
    private  int offset = 0;
    public Body body; // return ก็ต้องประกาศตัวแปรรับ







    public Mon1(final World world,final float x_px, final float y_px){



        sprite = SpriteLoader.getSprite("images/mon1.json");
        sprite.addCallback(new Callback<Sprite>() {


            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, //เพื่อให้ตำแหน่งอยู่กลางภาพ
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x_px, y_px);

                hasLoaded = true;
            }



            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error Loading image!",cause);
            }});

    }

    public Layer layer(){  // เพื่อ return ค่า ไปใช้ ใน TestScreen
        return  sprite.layer();
    }

    public void update(int delta) {

        if(hasLoaded == false) return;

        e = e + delta;
        if(e > 150){  //check state // update เปลี่ยนท่า
            switch (state) {
                case RUN:

                    offset = 0;
                    break;

            }
            spriteIndex = offset + ((spriteIndex + 1) % 4);
            sprite.setSprite(spriteIndex);
            e=0;
            System.out.println(spriteIndex);
        }
    }

    public void paint(Clock clock){

        if(!hasLoaded) return;

        //sprite.layer().setTranslation(
        //       (body.getPosition().x / GameplayScreen.M_PER_PIXEL),  //เซต ให้ ตัว sprite อยู่ในกรอบของ 2dbox
         //       (body.getPosition().y/ GameplayScreen.M_PER_PIXEL));

        // sprite.layer().setRotation(body.getAngle());
    }


}
