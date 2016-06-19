package nut.eventproject.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import nut.eventproject.core.Mygame;

public class MygameActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new Mygame());
  }
}
