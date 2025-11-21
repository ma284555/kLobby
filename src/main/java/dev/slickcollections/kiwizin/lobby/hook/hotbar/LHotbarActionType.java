package dev.slickcollections.kiwizin.lobby.hook.hotbar;


import dev.slickcollections.kiwizin.lobby.menus.MenuLobbies;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.HotbarActionType;

public class LHotbarActionType extends HotbarActionType {
  
  @Override
  public void execute(Profile profile, String action) {
    if (action.equalsIgnoreCase("lobbies")) {
      new MenuLobbies(profile);
    }
  }
}
