package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.GameplayScreen;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;
import tripleplay.game.UIScreen;

import java.util.Random;

/**
 * Created by Nutapol on 5/25/2016.
 */
public class toptube{

    private Sprite sprite;
    private  int spriteIndex = 0;
    private boolean hasLoaded = false;


    float bx = 24 ,by = 18;

    public enum State{
        RUN,IDLE
    };

    private State state = State.IDLE;
    private  int e = 0;
    private  int offset = 0;
    public  Body body; // return ก็ต้องประกาศตัวแปรรับ



    public toptube(final World world,final float x, final float y){

        sprite = SpriteLoader.getSprite("images/toptube.json");
        sprite.addCallback(new Callback<Sprite>() {


            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, //เพื่อให้ตำแหน่งอยู่กลางภาพ
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x, y);
                body = initPhysicsBody(world,
                        GameplayScreen.M_PER_PIXEL * x,
                        GameplayScreen.M_PER_PIXEL * y);
                hasLoaded = true;
            }



            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error Loading image!",cause);
            }});

    }

    public  Layer layer(){  // เพื่อ return ค่า ไปใช้ ใน TestScreen
        return  sprite.layer();
    }

    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.layer().width()) * GameplayScreen.M_PER_PIXEL / 2, //set size of box2D
                (sprite.layer().height())* GameplayScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }

    public void update(int delta) {
        if(hasLoaded == false) return;
        e = e + delta;
        sprite.setSprite(0);

        if(body.getLinearVelocity().x>-3) {
            body.applyForce(new Vec2(-120, -100), body.getPosition());
        }
        //System.out.println(body.getLinearVelocity().x);

    }

    public void paint(Clock clock){
        if(!hasLoaded) return;

        sprite.layer().setTranslation(
                (body.getPosition().x / GameplayScreen.M_PER_PIXEL),
                (body.getPosition().y/ GameplayScreen.M_PER_PIXEL));

    }
}



