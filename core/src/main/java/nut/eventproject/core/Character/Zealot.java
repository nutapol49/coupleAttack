package nut.eventproject.core.Character;
import nut.eventproject.core.GameplayScreen;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.Body;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import nut.eventproject.core.sprite.Sprite;
import nut.eventproject.core.sprite.SpriteLoader;
import playn.core.util.Clock;

public class Zealot {

    private Sprite sprite;
    private  int spriteIndex = 0;
    private boolean hasLoaded = false;


    public enum State{
        IDLE, RUN, ATTK,JUMP,RUNON
    };

    private State state = State.RUN;
    private  int e = 0;
    private  int offset = 0;
    private Body body; // return ก็ต้องประกาศตัวแปรรับ

    private Body initPhysicsBody(World world,float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        //EdgeShape shape = new EdgeShape();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((sprite.layer().width()/2)-10) * GameplayScreen.M_PER_PIXEL / 2, //set size of box2D
                ((sprite.layer().height()/2)+13)* GameplayScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }



    public Zealot(final World world,final float x_px, final float y_px){

        // Constructor
        //x y คือ รับว่าจะให้ตัวละครแสดงอยู่ตรงไหน
        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyUp(Keyboard.Event event) { //ดักสวิซ์ จาก keyboard ที่เรากด
                if(event.key() == Key.SPACE) {
                    switch (state) {
                        case RUN:
                            state = State.JUMP;
                            break;
                        case JUMP:
                            state = State.RUNON;
                            break;
                        case RUNON:
                            state = State.RUN;
                            break;
                    } }
            }        });

        sprite = SpriteLoader.getSprite("images/zealot.json");
        sprite.addCallback(new Callback<Sprite>() {


            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, //เพื่อให้ตำแหน่งอยู่กลางภาพ
                                        sprite.height() / 2f);
                sprite.layer().setTranslation(x_px, y_px);
                //สร้าง Body ตอนที่ Sprite โหลดเสร็จแล้ว
                 body = initPhysicsBody(world,
                        GameplayScreen.M_PER_PIXEL * x_px,
                        GameplayScreen.M_PER_PIXEL * y_px);
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
                case RUN: offset = 0;
                    break;
                case JUMP:
                    offset = 4;
                   // if(spriteIndex == 7){
                        state = State.RUNON;
                   // }
                    break;
                case RUNON:
                    offset = 8;
                    break;
               // case ATTK:
                 //   offset=8;
                  /*  if(spriteIndex == 10) {
                        state = State.IDLE;
                    } */
                   // break;
            }
            spriteIndex = offset + ((spriteIndex + 1) % 4);
            sprite.setSprite(spriteIndex);
            e=0;
        }
    }

    public void paint(Clock clock){

        if(!hasLoaded) return;

        sprite.layer().setTranslation(
                (body.getPosition().x / GameplayScreen.M_PER_PIXEL)+44,  //เซต ให้ ตัว sprite อยู่ในกรอบของ 2dbox
                (body.getPosition().y/ GameplayScreen.M_PER_PIXEL)-38);
    }



}

