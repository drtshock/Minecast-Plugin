package io.minecase.minecast.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/** Gui Menu  - Double0negative */


public abstract class GUIMenu {

	private ItemStack[][] items;

	protected int size;
	protected String name;
	protected Player player;

	public GUIMenu(int size, String name, Player p){
		this.size = size;
		this.name = name;
		this.player = p;
		items = new ItemStack[9][size];
	}


	public abstract void construct();

	public void setItem(int a, int b, ItemStack item){
		items[a][b] = item;
	}

	public ItemStack getItemAt(int a, int b){
		return items[a][b];
	}

	public void setName(String name){
		this.name = name;
		//showMenu();
	}

	public void clearMenu(){
		items = new ItemStack[9][size];
	}

	public void showMenu(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(CLib.getPlugin(), new Runnable(){
			public void run(){
				onShow();
				showMenu0();
			}
		}, 1);

	}
	
	public abstract void onShow();

	private void showMenu0(){
		Inventory inv = Bukkit.createInventory(player, size * 9, name);

		int a = 0; 
		for(int b = 0; b < size; b++){
			for(int c = 0; c < 9; c++){
				inv.setItem(a, items[c][b]);
				a++;
			}
		}
		player.openInventory(inv);

		MenuHandler.getInstance().registerMenu(player, this);
	}

	public abstract void itemClicked(int x, int y, boolean right);

	public abstract void menuClosed();

	protected void menuClosed0(){
		
		MenuHandler.getInstance().unregisterMenu(player);
		player.closeInventory();
		menuClosed();


	}
}
