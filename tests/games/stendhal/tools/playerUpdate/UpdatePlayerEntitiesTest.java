package games.stendhal.tools.playerUpdate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.rule.EntityManager;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.tools.modifer.PlayerModifier;
import marauroa.server.db.DBTransaction;
import marauroa.server.db.TransactionPool;
import marauroa.server.game.db.DatabaseFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class UpdatePlayerEntitiesTest {

	@BeforeClass
	public static void setUp() throws Exception {
		new DatabaseFactory().initializeDatabase();
	}

	//@Ignore
	@Test
	public void testDoUpdate() throws Exception, Throwable {
		MockStendlRPWorld.get();
		DBTransaction transaction = TransactionPool.get().beginWork();
		try {
			
			PlayerModifier pm = new PlayerModifier();
			Player loaded = pm.loadPlayer(transaction, "george");
			assertNotNull("pm can only handle existing players, so if this fails first create a player in db by login", loaded);
			if (loaded.getSlot("bag").size() > 0) {
				loaded.getSlot("bag").remove(loaded.getSlot("bag").getFirst().getID());
			}
			//assertEquals(null, loaded.getSlot("bag").getFirst());
			
			EntityManager em = SingletonRepository.getEntityManager();
			Item item = (Item) em.getItem("leather armor");
			item.put("name", "leather_armor_+1");
			loaded.equipToInventoryOnly(item);
			assertTrue(loaded.getSlot("bag").has(item.getID()));
	
			assertTrue(pm.savePlayer(transaction, loaded));
			UpdatePlayerEntities updatePlayerEntities = new UpdatePlayerEntities();
			Player changing = updatePlayerEntities.createPlayerFromRPO(loaded);
			updatePlayerEntities.savePlayer(transaction, changing);
			
			
			
			
			Player secondLoaded = pm.loadPlayer(transaction, "george");
			assertNotNull(secondLoaded);
			
			assertNotNull(secondLoaded.getSlot("bag"));
			assertNotNull(secondLoaded.getSlot("bag").getFirst());
			assertThat(secondLoaded.getSlot("bag").getFirst().get("name"), not(is("leather_armor_+1")));

			TransactionPool.get().rollback(transaction);
		} catch (Throwable e) {
			TransactionPool.get().rollback(transaction);
			throw e;
		}
	}

}
