package games.stendhal.server.entity.npc.action;

import games.stendhal.common.MathHelper;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.parser.Sentence;
import games.stendhal.server.entity.player.Player;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Increments by some value the current state or substate of a quest.
 */
public class IncrementQuestAction implements ChatAction {

	private final String questname;
	private final int index;
	private final int increment;

	/**
	 * Creates a new SetQuestAction.
	 * 
	 * @param questname
	 *            name of quest-slot to change
	 * @param state
	 *            new value
	 */
	public IncrementQuestAction(final String questname, final int increment) {
		this.questname = questname;
		this.index = -1;
		this.increment = increment;
	}

	/**
	 * Creates a new SetQuestAction.
	 * 
	 * @param questname
	 *            name of quest-slot to change
	 * @param index
	 *            index of sub state
	 * @param state
	 *            new value
	 */
	public IncrementQuestAction(final String questname, final int index, final int increment) {
		this.questname = questname;
		this.index = index;
		this.increment = increment;
	}

	public void fire(final Player player, final Sentence sentence, final SpeakerNPC engine) {
		String substate = player.getQuest(questname,index);
		int questcount;
		if (substate.equals("null")) {
			questcount = increment;
		} else {
			questcount = MathHelper.parseIntDefault(substate,0) + increment;
		}
		if (index > -1) {
			player.setQuest(questname, index, "" + questcount);
		} else {
			player.setQuest(questname, "" + questcount);
		}
	}

	@Override
	public String toString() {
		return "IncrementQuest<" + questname + "[" + index + "] + " + increment + ">";
	}


	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false,
				IncrementQuestAction.class);
	}
}
