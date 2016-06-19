package nut.eventproject.core;

import nut.eventproject.core.Character.Zealot;

import nut.eventproject.core.sprite.Sprite;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.Body;
import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.*;

public class GameplayScreen extends UIScreen{
    public static float M_PER_PIXEL = 1/26.666667f; //size of world
    private static int width = 24; //640px in physic unit (meter)
    private static int height = 18;//480px in physic unit (meter)

    private final ScreenStack ss;
    private Zealot z;
    private Image bgImage;
    private ImageLayer bg;

    private World world;
    private boolean showDebugDraw = true; //show กรอบ
    private DebugDrawBox2D debugDraw;
    private ArrayList<Zealot> character = new ArrayList<Zealot>();


    public GameplayScreen(final ScreenStack ss) {



        this.ss = ss;
        bgImage = assets().getImage("images/bggameplay.png");
        this.bg = graphics().createImageLayer(bgImage);




        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        //พื้นโลก
        Body bottomground = world.createBody(new BodyDef());
        EdgeShape groundbottomShape = new EdgeShape();
        groundbottomShape.set(new Vec2(0,((height/2)+(float)5.5)),new Vec2(width,((height/2)+(float)5.5)));
        bottomground.createFixture(groundbottomShape,0.0f);

        Body topground = world.createBody(new BodyDef());
        EdgeShape groundtopShape = new EdgeShape();
        groundtopShape.set(new Vec2(0,((height/2)-5)),new Vec2(width,((height/2)-5)));
        topground.createFixture(groundtopShape,0.0f);

        Body rightwall = world.createBody(new BodyDef());
        EdgeShape wallrightShape = new EdgeShape();
        wallrightShape.set(new Vec2(width,0),new Vec2(width,height));
        rightwall.createFixture(wallrightShape,0.0f);

        Body leftwall = world.createBody(new BodyDef());
        EdgeShape wallleftShape = new EdgeShape();
        wallleftShape.set(new Vec2(3.4f,0),new Vec2(3.4f,height));
        leftwall.createFixture(wallleftShape,0.0f);

        keyboard().setListener(new Keyboard.Adapter(){
            public void onKeyDown(Keyboard.Event event){
                switch (event.key()){
                    case BACKSLASH:
                        ss.remove(ss.top());
                       // ss.push(new HomeScreen(ss));
                        break;
                }
            }
        });

        //นิยามตัว body



        //สร้าง shape ให้ body


    }

    public void wasShown(){
        super.wasShown();
        this.layer.add(bg);


        mouse().setListener(new Mouse.Adapter(){
            public void onMouseUp(Mouse.ButtonEvent event){
             /*  BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyType.DYNAMIC;
                bodyDef.position = new Vec2(
                        event.x() * M_PER_PIXEL,
                        event.y() * M_PER_PIXEL);

                Body body = world.createBody(bodyDef);
                CircleShape shape = new CircleShape();
                shape.setRadius(0.4f);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.1f;
                fixtureDef.restitution = 1f;

                body.createFixture(fixtureDef);
                body.setLinearDamping(0.2f); */
                z = new Zealot(world, event.x(), event.y()); //event เก็บค่าตำแหน่ง x y ของเมาส์ที่เราคลิก
                layer.add(z.layer());
                character.add(z);

            }});



        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/GameplayScreen.M_PER_PIXEL),
                    (int)(height/GameplayScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit  |
                    DebugDraw.e_jointBit  |
                    DebugDraw.e_aabbBit);
            debugDraw.setCamera(0,0,1f/GameplayScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);

        }
        // z = new Zealot(world,200f,280f);
        //this.layer.add(z.layer());

    }

    public void update(int delta) {
      //  super.update(delta);
        for(int i = 0; i < character.size(); i++){ //ต้องวน update เพราะเราเก็บ List ขออง zealot ไว้ ก็ต้อง update ทุกตัวใน List
            character.get(i).update(delta);
        }
        world.step(0.033f,10,10);
    }


    @Override
    public void paint(Clock clock){
        super.paint(clock);
        for(int i = 0; i < character.size(); i++){
            character.get(i).paint(clock);
        }
        if (showDebugDraw) {
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
