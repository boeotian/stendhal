package games.stendhal.server.maps.fado.deathmatch;

import games.stendhal.server.StendhalRPZone;
import games.stendhal.server.maps.ZoneConfigurator;
import games.stendhal.server.maps.quests.FadoDeathmatch;
import games.stendhal.server.util.Area;

import java.awt.geom.Rectangle2D;
import java.util.Map;

/**
 * This is just a Deathmatch for Fado (the battle_arena)
 * 
 * @author timothyb89
 * 
 */
public class Deathmatch implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildDeathmatch(zone); System.out.println("DEBUG: Loading Fado Deathmatch.");
	}

	/**
	 * Makes the Deathmatch/battle arena
	 *
	 * @param zone StendhalRPZone
	 */

	/**
	 * Creatures the Deathmatch referee.
	 *
	 * @param zone StendhalRPZone
	 */
	private void buildDeathmatch(StendhalRPZone zone) {
		String zoneName = zone.getID().getID();
		Rectangle2D shape = new Rectangle2D.Double();
		shape.setRect(5, 4, 38, 52);
		Area arena = new Area(zone, shape);
		FadoDeathmatch deathmatch = new FadoDeathmatch(zoneName, zone, arena);
		deathmatch.createLegs(27, 40);
		deathmatch.createNPC("Thonatun", 29, 16);
		
		
	}

}
