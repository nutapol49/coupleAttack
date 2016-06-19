package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.Body;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.GameplayScreen;
import sut.game01.core.HomeScreen;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;
import tripleplay.game.ScreenStack;
import static playn.core.PlayN.*;

public class Zealot {

    private Sprite sprite;
    private  int spriteIndex = 0;
    private boolean hasLoaded = false;
    private ScreenStack ss;


    public enum State{
        RUN,JUMP,RUNON,RUNBACK
    };

   public  State state = State.RUN;
    private  int e = 0;
    private  int offset = 0;
    public Body body; // return ก็ต้องประกาศตัวแปรรับ
    Sound jumpsound = assets().getSound("sound/jump");


    public Zealot(final World world,final float x_px, final float y_px,final ScreenStack ss){
        this.ss=ss;
        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) { //ดักสวิซ์ จาก keyboard ที่เรากด
                switch (event.key()) {
                    case SPACE:
                        if (state == State.RUN) {
                            state = State.RUNON;
                            jumpsound.play();

                        } else{
                            state = State.RUN;
                            jumpsound.play();
                        }
                        break;
                    case RIGHT:
                        body.applyForce(new Vec2(200,0),body.getPosition());
                        break;
                    case ESCAPE:
                        ss.remove(ss.top());
                        ss.push(new HomeScreen(ss));
                        break;

                }
            }
        });

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

    private Body initPhysicsBody(World world,float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.layer().width()) * GameplayScreen.M_PER_PIXEL / 2, //set size of box2D
                (sprite.layer().height())* GameplayScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = -50;
        body.createFixture(fixtureDef);

        body.setTransform(new Vec2(x,y), 0f);
        body.setFixedRotation(true);
        return body;
    }

    public Layer layer(){  // เพื่อ return ค่า ไปใช้ ใน TestScreen
        return  sprite.layer();
    }

    public void update(int delta) {
        if(hasLoaded == false) return;
        e = e + delta;
        if(e > 150){
            switch (state) {
                case RUN:
                    offset = 0;
                    break;

                case RUNON:
                    offset = 4;
                    break;

            }
            spriteIndex = offset + ((spriteIndex + 1) %  4);
            sprite.setSprite(spriteIndex);
            e=0;
        }

        switch (state){
            case RUN:
                    body.applyForce(new Vec2(0,10),body.getPosition());
                System.out.println("RUN");
                    break;
            case RUNON:
                    body.applyForce(new Vec2(0,-10),body.getPosition());
                System.out.println("RUNONN");
                break;
        }

    }

    public void paint(Clock clock){

        if(!hasLoaded) return;

        sprite.layer().setTranslation(
                (body.getPosition().x / GameplayScreen.M_PER_PIXEL),  //เซต ให้ ตัว sprite อยู่ในกรอบของ 2dbox
                (body.getPosition().y/ GameplayScreen.M_PER_PIXEL));

       // sprite.layer().setRotation(body.getAngle());
    }



}

