package games.stendhal.server.entity.npc.condition;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;

/**
 * Is this quest not completed?
 */
public class QuestNotCompletedCondition extends SpeakerNPC.ChatCondition {

	private String questname;

	/**
	 * Creates a new QuestNotCompletedCondition
	 *
	 * @param questname name of quest-slot
	 */
	public QuestNotCompletedCondition(String questname) {
		this.questname = questname;
	}

	@Override
	public boolean fire(Player player, String text, SpeakerNPC engine) {
		return (!player.isQuestCompleted(questname));
	}

	@Override
	public String toString() {
		return "QuestNotCompleted <" + questname + ">";
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false, QuestStartedCondition.class);
	}
}