/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DevItem.CrystalLing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MIME;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

public class Healing extends Buff {

	private int healingLeft;
	
	private float percentHealPerTick;
	private int flatHealPerTick;
	
	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
		
		type = buffType.POSITIVE;
	}
	
	@Override
	public boolean act(){

		target.HP = Math.min(target.HT, target.HP + healingThisTick());

		if (target.HP == target.HT && target instanceof Hero){
			((Hero)target).resting = false;
		}
		
		healingLeft -= healingThisTick();
		
		if (healingLeft <= 0){
			detach();
		}
		
		spend( TICK );
		
		return true;
	}
	
	private int healingThisTick(){
		return (int)GameMath.gate(1,
				Math.round(healingLeft * percentHealPerTick) + flatHealPerTick,
				healingLeft);
	}
	
	public void setHeal(int amount, float percentPerTick, int flatPerTick){
		MIME.GOLD_FIVE getHeal = Dungeon.hero.belongings.getItem(MIME.GOLD_FIVE.class);

		healingLeft = amount;
		percentHealPerTick = percentPerTick;
		flatHealPerTick = flatPerTick;


		if(getHeal!=null){
			healingLeft = amount*2;
			percentHealPerTick = percentPerTick*2;
			flatHealPerTick = flatPerTick*2;
		}
		CrystalLing crystalLing = Dungeon.hero.belongings.getItem(CrystalLing.class);
		if(crystalLing != null) {
			healingLeft = amount+(amount/3);
			percentHealPerTick = percentPerTick*1.2f;
			flatHealPerTick = flatPerTick+(flatPerTick/5);
		}
	}
	
	public void increaseHeal( int amount ){
		healingLeft += amount;
	}
	
	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.HEALING );
		else    target.sprite.remove( CharSprite.State.HEALING );
	}
	
	private static final String LEFT = "left";
	private static final String PERCENT = "percent";
	private static final String FLAT = "flat";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, healingLeft);
		bundle.put(PERCENT, percentHealPerTick);
		bundle.put(FLAT, flatHealPerTick);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		healingLeft = bundle.getInt(LEFT);
		percentHealPerTick = bundle.getFloat(PERCENT);
		flatHealPerTick = bundle.getInt(FLAT);
	}
	
	@Override
	public int icon() {
		return BuffIndicator.HEALING;
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(healingLeft);
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", healingThisTick(), healingLeft);
	}
}
