import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

public class Pathfinding extends Application {
    static final int SIZE = 600;
    static final int GRID_SIZE = 5;

    private Tile[][] tiles;

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        root.setPrefSize(SIZE, SIZE);

        tiles = new Tile[GRID_SIZE][GRID_SIZE];
    
        double tileSize = SIZE / GRID_SIZE;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Tile tile = new Tile(tileSize);
                tile.setTranslateX(j * tileSize);
                tile.setTranslateY(i * tileSize);

                tiles[i][j] = tile;
                root.getChildren().add(tile);
            }
        }

        tiles[0][0].SetType(NodeType.START);

        tiles[GRID_SIZE - 1][GRID_SIZE - 1].SetType(NodeType.GOAL);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
