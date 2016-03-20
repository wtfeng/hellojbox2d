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

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import java.util.Random;

public class HelloJBox2D extends Application {
	Stage primStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primStage = primaryStage;
		this.primStage.setTitle("HelloBox2D");

		Group root = new Group();

		Scene scene = new Scene(root, 600, 600);
		this.primStage.setScene(scene);

		// Creating the world
		Vec2 gravity = new Vec2(0f, -10f);
		World world = new World(gravity);

		GUI gui = new GUI(world, root);

		Scheduler scheduler = new Scheduler(world);

		createGround(world);
		populate(world);

		scheduler.start();
		this.primStage.show();

		Camera camera = new ParallelCamera();
		camera.setLayoutX(-1000);
		camera.setLayoutY(-2000);
		camera.setScaleX(3);
		camera.setScaleY(3);

		scene.setCamera(camera);

		scene.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				populate(world);
			} else {
				createBoxAt(world, e.getX(), e.getY());
			}
		});

		//scheduler.stop();
	}

	private void createGround(World world) {
		// Create a ground.
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0f, 0f);

		Body groundBody = world.createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(50f, 10f);

		groundBody.createFixture(groundBox, 0f);
	}

	private void createBoxAt(World world, double x, double y) {
		// Create a dynamic rigid body.
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DYNAMIC;
		boxBodyDef.position.set((float) x / GUI.SCALE, (float) -y / GUI.SCALE);

		Body boxBody = world.createBody(boxBodyDef);

		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(1f, 1f);

		FixtureDef boxFixctureDef = new FixtureDef();
		boxFixctureDef.shape = dynamicBox;
		boxFixctureDef.density = 0.2f;
		boxFixctureDef.friction = 0.3f;
		boxFixctureDef.restitution = 0.2f;

		boxBody.createFixture(boxFixctureDef);
	}

	private void populate(World world) {
		// Random generator
		Random rand = new Random(System.currentTimeMillis());

		// Create a dynamic rigid body.
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DYNAMIC;
		for (int i = 0; i < 12; i++) {
			boxBodyDef.position.set(rand.nextInt(40) - 20, rand.nextInt(30) + 70);

			Body boxBody = world.createBody(boxBodyDef);

			PolygonShape dynamicBox = new PolygonShape();
			dynamicBox.setAsBox(1f + rand.nextFloat() * 2, 1f + rand.nextFloat() * 2);

			FixtureDef boxFixctureDef = new FixtureDef();
			boxFixctureDef.shape = dynamicBox;
			boxFixctureDef.density = 0.2f;
			boxFixctureDef.friction = 0.3f;
			boxFixctureDef.restitution = 0.2f;

			boxBody.setAngularVelocity((rand.nextFloat() - 0.5f) * 16);
			boxBody.createFixture(boxFixctureDef);
		}
	}
}
