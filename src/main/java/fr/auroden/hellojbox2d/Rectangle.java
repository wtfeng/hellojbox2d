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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

public class Rectangle extends javafx.scene.shape.Rectangle implements BodyListener {
	public Rectangle() {
		setSmooth(true);
	}

	@Override
	public void bodyUpdate(BodyEvent e) {
		if (e.getType() == BodyEvent.Type.BODY_UPDATE) {
			Body body = e.getSource();

			Vec2 position = body.getPosition();
			float angle = body.getAngle();
			Fixture fixture = body.getFixtureList();
			Shape bodyShape = fixture.getShape();

			rotateProperty().set(-Math.toDegrees(angle));

			if (bodyShape.getType() == ShapeType.POLYGON) {
				PolygonShape polygonShape = (PolygonShape) bodyShape;
				Vec2[] vertices = polygonShape.getVertices();
				setX((position.x + vertices[0].x) * GUI.SCALE);
				setY((-position.y + vertices[0].y) * GUI.SCALE);
				setWidth((vertices[1].x - vertices[0].x) * GUI.SCALE);
				setHeight((vertices[2].y - vertices[1].y) * GUI.SCALE);
			}
		}
	}
}
