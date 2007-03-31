package games.stendhal.server.script;

import games.stendhal.server.Jail;
import games.stendhal.server.StendhalRPRuleProcessor;
import games.stendhal.server.actions.AdministrationAction;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.scripting.ScriptImpl;

import java.util.HashSet;
import java.util.List;

import marauroa.common.game.RPObject;
import marauroa.common.game.RPSlot;

import org.apache.log4j.Logger;

/**
 * Deep inspects a player and all his/her items
 *
 * @author hendrik
 */
public class BugInspect extends ScriptImpl {
	private static Logger logger = Logger.getLogger(BugInspect.class);
	private HashSet<String> seen = new HashSet<String>();

	@Override
	public void execute(Player admin, List<String> args) {
		super.execute(admin, args);
		if (args.size() == 0) {
			admin.sendPrivateText("Need player name as parameter.");
			return;
		}
		Player player = StendhalRPRuleProcessor.get().getPlayer(args.get(0));
		seen.add(player.getName());
		StringBuffer sb = new StringBuffer();
		sb.append("Inspecting " + player.getName() + "\n");
		boolean caught = false;
		boolean warn = false;

		// inspect slots
		for (RPSlot slot : player.slots()) {
			if (slot.getName().equals("!buddy") 
				|| slot.getName().equals("!ignore")
				|| slot.getName().equals("!kills")
				|| slot.getName().equals("!quests")) {
				continue;
			}
			sb.append("\nSlot " + slot.getName() + ": \n");

			// list objects
			for (RPObject object : slot) {
				if (object instanceof StackableItem) {
					StackableItem item = (StackableItem) object;
					if (!item.getName().equals("money") && item.getQuantity() > 10000) {
						caught = true;
					}
					if (!item.getName().equals("money") && item.getQuantity() > 1000) {
						warn = true;
					}
				}
				sb.append("   " + object + "\n");
			}
		}
		
		String message = player.getName() + " has a large amount of items";
		if (caught) {
			
			StendhalRPRuleProcessor.get().addGameEvent("bug inspect", "jail", player.getName(),
					        Integer.toString(-1), "bug abuse");
			Jail.get().imprison(player.getName(), player, -1, "bug abuse");
			player.sendPrivateText("Please use /support to talk to an admin about your large amount if items which may have been the result of a bug.");
			player.notifyWorldAboutChanges();
			
			message = "auto jailed " + player.getName() + " because of a large number of items";
		}

		if (warn || caught) {
			
			StendhalRPRuleProcessor.get().addGameEvent("bug inspect", "support", message);
			for (Player p : StendhalRPRuleProcessor.get().getPlayers()) {
				if (p.getAdminLevel() >= AdministrationAction.REQUIRED_ADMIN_LEVEL_FOR_SUPPORT) {
					p.sendPrivateText(message);
					p.notifyWorldAboutChanges();
				}
			}
			logger.warn("User with large amout of items: " + message + "\r\n" + sb.toString());
		}

		player.sendPrivateText(sb.toString());
	}
}
