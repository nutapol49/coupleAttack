package nut.eventproject.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import tripleplay.game.ScreenSpace;
import tripleplay.game.ScreenStack;

public class Mygame extends Game.Default {

    public static final int UPDATE_RATE = 10;
    private ScreenStack ss = new ScreenStack();
    protected final Clock.Source clock = new Clock.Source(UPDATE_RATE);


    public Mygame() {
    super(UPDATE_RATE); // call update every 33ms (30 times per second)
  }

    @Override
  public void init() {
    // create and add background image layer
    ss.push(new HomeScreen(ss));
  }

  @Override
  public void update(int delta) {
      ss.update(delta);
  }

  @Override
  public void paint(float alpha) {
    // the background automatically paints itself, so no need to do anything here!
      clock.paint(alpha);
      ss.paint(clock);
  }
}
