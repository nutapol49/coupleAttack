package sut.game01.core;

import javafx.scene.media.AudioClip;
import org.jbox2d.callbacks.ContactImpulse;
import sut.game01.core.RandomArrow;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.character.*;
import sut.game01.core.sprite.Mon1;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.util.Colors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static playn.core.PlayN.*;

public class GameplayScreen extends Screen{

    private Clock.Source stoptime = new Clock.Source(0);
    public static Boolean pause = false;
    public static  float M_PER_PIXEL = 1 / 26.666667f;
    private static int width = 24;
    private static int height = 18;
    private World world;
    private ScreenStack ss;
    private Image bgImage;
    private ImageLayer bg;
    private Image pauseImage;
    private ImageLayer pauseIcon;
    private Zealot zealot;
    private tube tor1;
    private tube tor2;
    private tube tor3;
    private tube tor4;
    private tube tor5;
    private toptube ttor1;
    private toptube ttor2;
    private toptube ttor3;
    private toptube ttor4;
    private toptube ttor5;
    private DebugDrawBox2D debugDraw;
    private Boolean showDebugDraw = true;
    private static HashMap<Body, String> bodies = new HashMap<Body,String>();
    private Random random = new Random();
    private String debugSring = "";
    public static float x = 0.0f;
    public static float y = 0.0f;
    public static float xsea = 0.0f;
    private Mon1 sea;
    private int checkt = 0;
    private Boolean checkover= false;
    private Layer score;
    private Layer gameover;
    private ToolsG toolg = new ToolsG();
    private float alphaTest = 0f;
    private float alphaEnd = 1f;
    private int scoret1 = 0;
    private float scoret2;
    private int timedeley=0;
    private Boolean pauseCheck = false;
    private int countPause = 0;
    public static HashMap<String,ArrowKey> key = new HashMap<String,ArrowKey>();


    Sound soundover= assets().getMusic("sound/over");

    public GameplayScreen(final ScreenStack ss){


        scoret2=0;
        Vec2 gravity = new Vec2(0.0f,0.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        Body bottomground = world.createBody(new BodyDef());
        EdgeShape groundbottomShape = new EdgeShape();
        groundbottomShape.set(new Vec2(0, ((height / 2) + (float) 5.5)), new Vec2(100000, ((height / 2) + (float) 5.5)));
        bottomground.createFixture(groundbottomShape, 0.0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.shape = groundbottomShape;
        bottomground.createFixture(fixtureDef);

        Body seabody = world.createBody(new BodyDef());
        EdgeShape seashape = new EdgeShape();
        seashape.set(new Vec2(2,0), new Vec2(2,height));
        seabody.createFixture(seashape, 0.0f);
        FixtureDef fixtureDef3 = new FixtureDef();
        fixtureDef3.friction = 1f;
        fixtureDef3.shape = seashape;
        seabody.createFixture(fixtureDef3);
        bodies.put(seabody,"seabody");

        Body topground = world.createBody(new BodyDef());
        EdgeShape groundtopShape = new EdgeShape();
        groundtopShape.set(new Vec2(-100000, ((height / 2) - 5)), new Vec2(100000, ((height / 2) - 5)));
        topground.createFixture(groundtopShape, 0.0f);
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.friction = 1f;
        fixtureDef1.shape = groundtopShape;
        topground.createFixture(fixtureDef1);

        this.ss=ss;
        bgImage = assets().getImage("images/bggameplay.png");
        bg = graphics().createImageLayer(bgImage);



        pauseImage = assets().getImage("images/pauseIcon.png");
        pauseIcon = graphics().createImageLayer(pauseImage);
        pauseIcon.setTranslation(0,0);
        pauseIcon.addListener(new Mouse.LayerAdapter(){
            public void onMouseUp(Mouse.ButtonEvent event){
                countPause++;
                pauseCheck = countPause%2 !=0;
            }
        });

        zealot = new Zealot(world,600,200,ss);
        sea = new Mon1(world,0,245);

        tor1 = new tube(world,1000+random.nextInt(400),300);
        tor2 = new tube(world,1500+random.nextInt(400),300);
        tor3 = new tube(world,2000+random.nextInt(400),300);
        tor4 = new tube(world,2500+random.nextInt(400),300);
        tor5 = new tube(world,3000+random.nextInt(400),300);

        ttor1 = new toptube(world,1000+random.nextInt(400),150);
        ttor2 = new toptube(world,1500+random.nextInt(400),150);
        ttor3 = new toptube(world,2000+random.nextInt(400),150);
        ttor4 = new toptube(world,2500+random.nextInt(400),150);
        ttor5 = new toptube(world,3000+random.nextInt(400),150);

        score = toolg.genText("Score : "+scoret1, 30, Colors.MAGENTA, 450, 30);

        gameover = toolg.genText("Game Over", 60, Colors.MAGENTA, 200, 200);
        gameover.setVisible(false);




        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                if(bodies.get(a)=="seabody" && b== zealot.body || bodies.get(b)=="seabody" && a== zealot.body){
                   checkover=true;
                }

                if(bodies.get(a)=="seabody" && b== tor1.body || bodies.get(b)=="seabody" && a== tor1.body){
                    checkt=1;
                }
                if(bodies.get(a)=="seabody" && b== tor2.body || bodies.get(b)=="seabody" && a== tor2.body){
                    checkt=2;
                }
                if(bodies.get(a)=="seabody" && b== tor3.body || bodies.get(b)=="seabody" && a== tor3.body){
                    checkt=3;
                }
                if(bodies.get(a)=="seabody" && b== tor4.body || bodies.get(b)=="seabody" && a== tor4.body){
                    checkt=4;
                }
                if(bodies.get(a)=="seabody" && b== tor5.body || bodies.get(b)=="seabody" && a== tor5.body){
                    checkt=5;
                }

                if(bodies.get(a)=="seabody" && b== ttor1.body || bodies.get(b)=="seabody" && a== ttor1.body){
                    checkt=6;
                }
                if(bodies.get(a)=="seabody" && b== ttor2.body || bodies.get(b)=="seabody" && a== ttor2.body){
                    checkt=7;
                }
                if(bodies.get(a)=="seabody" && b== ttor3.body || bodies.get(b)=="seabody" && a== ttor3.body){
                    checkt=8;
                }
                if(bodies.get(a)=="seabody" && b== ttor4.body || bodies.get(b)=="seabody" && a== ttor4.body){
                    checkt=9;
                }
                if(bodies.get(a)=="seabody" && b== ttor5.body || bodies.get(b)=="seabody" && a== ttor5.body){
                    checkt=10;
                }

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });

    }

    @Override
    public void wasShown(){
        super.wasShown();
        //soundover.play();
        this.layer.add(bg);
        this.layer.add(pauseIcon);
        this.layer.add(zealot.layer());
        this.layer.add(tor1.layer());
        this.layer.add(tor2.layer());
        this.layer.add(tor3.layer());
        this.layer.add(tor4.layer());
        this.layer.add(tor5.layer());
        this.layer.add(ttor1.layer());
        this.layer.add(ttor2.layer());
        this.layer.add(ttor3.layer());
        this.layer.add(ttor4.layer());
        this.layer.add(ttor5.layer());
        this.layer.add(sea.layer());
        this.layer.add(score);
        this.layer.add(gameover);

        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int) (width / GameplayScreen.M_PER_PIXEL),
                    (int) (height / GameplayScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit |
                    DebugDraw.e_jointBit |
                    DebugDraw.e_aabbBit);
            debugDraw.setCamera(0,0,1f / GameplayScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);

        }



    }

    @Override
    public void update(int delta) {
        if(!pauseCheck) {

            super.update(delta);
            world.step(0.033f, 10, 10);
            zealot.update(delta);
            sea.update(delta);
            tor1.update(delta);
            tor2.update(delta);
            tor3.update(delta);
            tor4.update(delta);
            tor5.update(delta);
            ttor1.update(delta);
            ttor2.update(delta);
            ttor3.update(delta);
            ttor4.update(delta);
            ttor5.update(delta);
            if (checkover == false) {
                scoret2 += 0.2f;
                scoret1 = (int) scoret2;
            }
            controlbg();
            scorecount();

            if (checkover == true) {
                //soundover.play();
                world.destroyBody(zealot.body);
                zealot.layer().destroy();
                gameover.setVisible(true);
                gameover.setAlpha(alphaTest);
                if (timedeley >= 50) {
                    gameover.setAlpha(alphaEnd);
                    System.out.println(alphaEnd);
                }
                timedeley++;
            }
        }


    }

    public void scorecount(){
        layer.remove(score);
        score = toolg.genText("Score : "+scoret1, 30, Colors.MAGENTA, 450, 30);
        layer.add(score);

    }

    public void controlbg(){
        x -= 2;
        if (x <= -bg.width() + 640) {
            x = -20;
        }
        bg.setTranslation(x, y);


        if(checkt==1){
            world.destroyBody(tor1.body);
            layer.remove(tor1.layer());
            tor1 = new tube(world,800+random.nextInt(500),300);
            layer.add(tor1.layer());
            checkt=0;
        }
        if(checkt==2){
            world.destroyBody(tor2.body);
            layer.remove(tor2.layer());
            tor2 = new tube(world,900+random.nextInt(500),300);
            layer.add(tor2.layer());
            checkt=0;
        }
        if(checkt==3){
            world.destroyBody(tor3.body);
            tor3.layer().destroy();
            tor3 = new tube(world,1000+random.nextInt(500),300);
            layer.add(tor3.layer());
            checkt=0;
        }
        if(checkt==4){
            world.destroyBody(tor4.body);
            tor4.layer().destroy();
            tor4 = new tube(world,1100+random.nextInt(500),300);
            layer.add(tor4.layer());
            checkt=0;
        }
        if(checkt==5){
            world.destroyBody(tor5.body);
            tor5.layer().destroy();
            tor5 = new tube(world,1200+random.nextInt(500),300);
            layer.add(tor5.layer());
            checkt=0;
        }
        if(checkt==6){
            world.destroyBody(ttor1.body);
            layer.remove(ttor1.layer());
            ttor1 = new toptube(world,800+random.nextInt(500),150);
            layer.add(ttor1.layer());
            checkt=0;
        }
        if(checkt==7){
            world.destroyBody(ttor2.body);
            layer.remove(ttor2.layer());
            ttor2 = new toptube(world,900+random.nextInt(500),150);
            layer.add(ttor2.layer());
            checkt=0;
        }
        if(checkt==8){
            world.destroyBody(ttor3.body);
            ttor3.layer().destroy();
            ttor3 = new toptube(world,1000+random.nextInt(500),150);
            layer.add(ttor3.layer());
            checkt=0;
        }
        if(checkt==9){
            world.destroyBody(ttor4.body);
            ttor4.layer().destroy();
            ttor4 = new toptube(world,1100+random.nextInt(500),150);
            layer.add(ttor4.layer());
            checkt=0;
        }
        if(checkt==10){
            world.destroyBody(ttor5.body);
            ttor5.layer().destroy();
            ttor5 = new toptube(world,1200+random.nextInt(500),150);
            layer.add(ttor5.layer());
            checkt=0;
        }

    }

    @Override
    public void paint(Clock clock) {
        if (!pauseCheck) {
            if (checkover == true) {
                alphaTest = toolg.fade(alphaTest);
            }
            if (timedeley >= 50) {
                alphaEnd = toolg.fade1(alphaEnd);
            }
            super.paint(clock);
            zealot.paint(clock);
            tor1.paint(clock);
            tor2.paint(clock);
            tor3.paint(clock);
            tor4.paint(clock);
            tor5.paint(clock);
            ttor1.paint(clock);
            ttor2.paint(clock);
            ttor3.paint(clock);
            ttor4.paint(clock);
            ttor5.paint(clock);

            if (showDebugDraw) {
                debugDraw.getCanvas().clear();
                debugDraw.getCanvas().drawText(debugSring, 50, 50);
                world.drawDebugData();
            }
        }
    }
    public void newLoad(int level) {
        RandomArrow arEvent = new RandomArrow(layer,level);
        key = arEvent.getHashMap();
        for(int i = 0;i<key.size();i++){
            layer.add(key.get("key_" + i).layer());
        }
        System.out.println("new key : " + RandomArrow.keyOut + " ");
    }
}
