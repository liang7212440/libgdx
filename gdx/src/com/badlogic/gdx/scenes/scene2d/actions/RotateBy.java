/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

public class RotateBy extends Action {
	static final Pool<RotateBy> pool = new Pool<RotateBy>(false, 4, 100) {
		protected RotateBy newObject () {
			return new RotateBy();
		}
	};

	protected float rotation;
	protected float startRotation;;
	protected float deltaRotation;
	protected float duration;
	protected float invDuration;
	protected float taken = 0;
	protected Actor target;
	protected boolean done;

	public static RotateBy $ (float rotation, float duration) {
		RotateBy action = pool.add();
		action.rotation = rotation;
		action.duration = duration;
		action.invDuration = 1 / duration;
		return action;
	}

	@Override public void setTarget (Actor actor) {
		this.target = actor;
		this.startRotation = target.rotation;
		this.deltaRotation = rotation;
		this.taken = 0;
		this.done = false;
	}

	@Override public void act (float delta) {
		taken += delta;
		if (taken >= duration) {
			taken = duration;
			done = true;
			return;
		}

		float alpha = taken * invDuration;
		target.rotation = startRotation + deltaRotation * alpha;
	}

	@Override public boolean isDone () {
		return done;
	}

	@Override public void finish () {
		pool.removeValue(this, true);
		if(listener != null)
			listener.completed(this);
	}

	@Override public Action copy () {
		return $(rotation, duration);
	}
}
