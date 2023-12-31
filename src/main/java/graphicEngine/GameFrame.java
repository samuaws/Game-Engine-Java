package graphicEngine;
import graphicEngine.GraphicObject;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GameFrame extends Application {
		public static GameFrame instance;
		private static final double TARGET_FRAME_TIME = 1000.0 / 20.0; // Target frame time for 60 FPS
		private long lastFrameTime = System.nanoTime();
		private  static GraphicEngine graphicEngine;
		private static Consumer<Scene> sceneConsumer;
		private static  Runnable gameLoop;

		private Group root = new Group() ;
		private Scene scene ;

		public Group getRoot() {
			return root;
		}

		//addObjToRoot ajoute un objet graphique à la racine de la scène.
		// Elle prend en entrée un objet graphique
		public void addObjToRoot(GraphicObject graphicObject) {
			this.root.getChildren().add( graphicObject.getRectangle());
			if(graphicObject.getTexture() != null)
			{
				this.root.getChildren().add( graphicObject.createImageView());
			}
		}

	public void removeObjectFromRoot(ArrayList<GraphicObject> graphicObjects) {
			if(graphicObjects !=null) {
				for (GraphicObject graphicObject : graphicObjects) {
					root.getChildren().remove(graphicObject.getRectangle());
					if (graphicObject.getTexture() != null) {
						root.getChildren().remove(graphicObject.getImageView());
					}
				}
			}
	}
		public GameFrame() {
			System.out.println("GameFrame afficher");
		}
		/* start() est la méthode d'entrée de l'application.
		Elle est appelée lorsque l'application est lancée.
		 */
		@Override
		public void start(Stage primaryStage) {
			//définit l'instance de la classe coreKernel comme étant l'instance actuelle.
			instance = this;
			//Ajoute tous les objets graphiques du moteur graphique à la racine de la scène.
			addObjectsToRoot(graphicEngine.getGraphicObjects());
			// Get the primary screen
			Screen screen = Screen.getPrimary();
			// Get the visual bounds of the screen
			javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
			// Create your JavaFX content here
			// Set the stage's dimensions to match the screen's dimensions
			primaryStage.setX(bounds.getMinX());
			primaryStage.setY(bounds.getMinY());
			primaryStage.setWidth(960);
			primaryStage.setHeight(600);
			primaryStage.setTitle("Constant Frame Rate Game");
			scene = new Scene(root);
			sceneConsumer.accept(scene);
			primaryStage.setScene(scene);
			primaryStage.show();

			// Set up the AnimationTimer for the game loop
			new AnimationTimer() {
				@Override
				public void handle(long now) {
				double deltaTime = (now - lastFrameTime) / 1_000_000.0; // Convert to milliseconds

				if (deltaTime >= TARGET_FRAME_TIME) {
					removeObjectFromRoot(graphicEngine.getObjectsToRemove());
					addObjectsToRoot(graphicEngine.getGraphicObjects());
					gameLoop.run();

					lastFrameTime = now;

					// Calculate time to sleep to achieve a constant frame rate
					double sleepTime = TARGET_FRAME_TIME - deltaTime;
					if (sleepTime > 0) {
						try {
							Thread.sleep((long) sleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	/*addObjectsToRoot() prend en entrée une liste GraphicObject
	et ajoute chaque objet à la racine de la scène.*/
	void addObjectsToRoot(ArrayList<GraphicObject> objects){
		for (GraphicObject obj : objects) {
			addObjToRoot(obj);
		}
		graphicEngine.setGraphicObjects( new ArrayList<GraphicObject>());
	}

	    public static void main(String[] args , GraphicEngine graphicEngine , Consumer<Scene> sceneConsumer , Runnable gameloop)
		{
			GameFrame.graphicEngine = graphicEngine;
			GameFrame.sceneConsumer = sceneConsumer;
			GameFrame.gameLoop = gameloop;
			launch(args);
		}
}
