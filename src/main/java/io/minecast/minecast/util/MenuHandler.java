package io.minecast.minecast.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

/** Gui Menu Handler - Double0negative */

public class MenuHandler implements Listener{

	private static HashMap<Player, GUIMenu>menus = new HashMap<Player, GUIMenu>();

	private static MenuHandler instance = new MenuHandler();


	private MenuHandler(){
	}

	public static MenuHandler getInstance(){
		return instance;
	}

	public void registerMenu(Player player, GUIMenu menu){
		menus.put(player, menu);
	}

	public void unregisterMenu(Player player){
		menus.remove(player);
	}


	@EventHandler
	public void menuClick(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player)e.getWhoClicked();
			if(menus.get(p) != null && e.getCurrentItem() != null){
				if(e.getSlot() == e.getRawSlot()){
					int x = e.getRawSlot() % 9;
					int y = e.getRawSlot() / 9;
					GUIMenu menu = menus.get(p);
					menu.itemClicked(x, y, e.isRightClick());
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void menuClose(InventoryCloseEvent e){
		if(e.getPlayer() instanceof Player){
			Player p = (Player)e.getPlayer();
			if(menus.get(p) != null){
				GUIMenu menu = menus.get(p);
				menu.menuClosed0();
			}
		}
	}


}
