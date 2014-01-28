package io.minecast.minecast.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExampleMenu extends GUIMenu{

	public ExampleMenu(String name, Player p) {
		super(1, name, p); // create a single lined menu
	}

	/**
	 * Add three items to this menu
	 */
	@Override
	public void construct() {
		setItem(0, 0, new ItemStack(Material.WOOL, 1));
		setItem(0, 1, new ItemStack(Material.DIAMOND, 1));
		setItem(0, 2, new ItemStack(Material.REDSTONE_BLOCK, 1));
	}

	/**
	 * Is called when the menu is shown to the player. call menu.showMenu() to show the menu to the player
	 */
	@Override
	public void onShow() {
		System.out.println("Showing menu to " + player.getName());

	}

	/**
	 * Is called when a item is clicked
	 */
	@Override
	public void itemClicked(int x, int y, boolean right) {
		System.out.println("The item at "+x+" "+y+" was clicked. It was" + ((right)? "": "not") +" a right click");
		ItemStack item = getItemAt(x, y);
		System.out.println((item == null)? "There is no item here" : "The item here is "+item);
	}

	/**
	 * Is called when the menu is closed
	 */
	@Override
	public void menuClosed() {
		System.out.println(player.getName() + "Closed a menu ");
	}

}
