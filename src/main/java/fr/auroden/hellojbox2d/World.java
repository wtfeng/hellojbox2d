/**********************************************************************
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org>
**********************************************************************/

package fr.auroden.hellojbox2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import java.util.ArrayList;
import java.util.List;

public class World extends org.jbox2d.dynamics.World {
	List<BodyWrapper> bodies = new ArrayList<>();

	// time step for Box2D.
	float timeStep = 1f / 60f;

	// iterations for physics pass.
	int velocityIterations = 8;
	int positionIterations = 3;

	// listeners
	private final List<WorldListener> listeners = new ArrayList<>();

	public World(Vec2 gravity) {
		super(gravity);
		// setAllowSleep(true); // default value.
	}

	@Override
	public Body createBody(BodyDef def) {
		BodyWrapper body = new BodyWrapper(super.createBody(def));
		this.bodies.add(body);
		fireBodyAdded(body);
		return body.getBody();
	}

	public void update() {
		step(this.timeStep, this.velocityIterations, this.positionIterations);
		this.bodies.stream()
				.filter(bodyWrapper -> (bodyWrapper.getBody().isAwake()))
				.forEach(BodyWrapper::update);
		fireWorldUpdate();
	}

	public void addWorldListener(WorldListener worldListener) {
		assert (worldListener != null);
		this.listeners.add(worldListener);
	}

	public void removeWorldListener(WorldListener worldListener) {
		assert (worldListener != null);
		this.listeners.remove(worldListener);
	}

	private void fireEvent(WorldEvent e) {
		for (final WorldListener worldListener : this.listeners) {
			worldListener.worldUpdate(e);
		}
	}

	private void fireWorldUpdate() {
		final WorldEvent e = new WorldEvent(this, WorldEvent.Type.WORLD_UPDATE);
		fireEvent(e);
	}

	private void fireBodyAdded(BodyWrapper body) {
		final WorldEvent e = new WorldEvent(this, body, WorldEvent.Type.BODY_ADDED);
		fireEvent(e);
	}

	private void fireBodyRemoved(BodyWrapper body) {
		final WorldEvent e = new WorldEvent(this, body, WorldEvent.Type.BODY_REMOVED);
		fireEvent(e);
	}
}
