import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

public class Main extends Application {
    static final int SIZE = 600;
    static final int GRID_SIZE = 5;

    private Tile[][] tiles;

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        root.setPrefSize(SIZE, SIZE);

        tiles = new Tile[GRID_SIZE][GRID_SIZE];

        double tileSize = SIZE / GRID_SIZE;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                Tile tile = new Tile(x, y, tileSize);
                tile.setTranslateX(x * tileSize);
                tile.setTranslateY(y * tileSize);

                tiles[x][y] = tile;
                root.getChildren().add(tile);
            }
        }

        tiles[0][0].SetType(NodeType.START);

        tiles[GRID_SIZE - 1][GRID_SIZE - 1].SetType(NodeType.GOAL);

        Button run = new Button("Run");
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Thread thread = new Thread(new Runnable(){
					@Override
					public void run() {
						StartAnimation();
                    } 
                });

                thread.start();
            }
        });

        root.getChildren().add(run);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void StartAnimation() {
        Tile start = null;
        Tile goal = null;

        /* Copies type of tiles to tiles and finds the start and end tiles */
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if (tiles[x][y].type == NodeType.START) {
                    start = tiles[x][y];
                }

                if (tiles[x][y].type == NodeType.GOAL) {
                    goal = tiles[x][y];
                }
            }
        }

        Pathfinding.AStar(start, goal, tiles);

    }
}
