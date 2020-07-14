import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Main extends Application {
    public static boolean hasAnimated = false;

    private static final int SIZE = 600;
    private static final int GRID_SIZE = 15;

    private Tile[][] tiles;
    private Tile start;
    private Tile goal;

    ComboBox<String> algorithmSelect;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(8, 8, 16, 8));

        GridPane grid = new GridPane();
        grid.setHgap(10);

        Label algorithmLabel = new Label("Algorithm:");
        GridPane.setConstraints(algorithmLabel, 0, 0);

        algorithmSelect = new ComboBox<>();
        algorithmSelect.getItems().addAll("Breath First Search", "Greedy Best-First Search", "A*");
        algorithmSelect.getSelectionModel().selectFirst();
        GridPane.setConstraints(algorithmSelect, 1, 0);

        Button runBtn = new Button("Run");
        runBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        StartAnimation();
                    }
                });

                thread.start();
            }
        });
        GridPane.setConstraints(runBtn, 2, 0);

        grid.getChildren().addAll(algorithmLabel, algorithmSelect, runBtn);

        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(SIZE, SIZE);
        stackPane.setPadding(new Insets(8, 0, 0, 0));

        tiles = new Tile[GRID_SIZE][GRID_SIZE];
        double tileSize = SIZE / GRID_SIZE;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                Tile tile = new Tile(x, y, tileSize);
                tile.setTranslateX(x * tileSize);
                tile.setTranslateY(y * tileSize);

                tiles[x][y] = tile;
                stackPane.getChildren().add(tile);
            }
        }

        tiles[0][0].SetType(NodeType.START);
        tiles[GRID_SIZE - 1][GRID_SIZE - 1].SetType(NodeType.GOAL);

        ResetTiles();

        root.setTop(grid);
        root.setCenter(stackPane);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pathfinding Visualization");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void StartAnimation() {
        ResetTiles();

        hasAnimated = true;
        switch(algorithmSelect.getValue()){
            case "Breath First Search":
                Pathfinding.BreathFirstSearch(start, goal, tiles);
                break;
            case "Greedy Best-First Search":
                Pathfinding.GreedyBestFirstSearch(start, goal, tiles);
                break;
            case "A*":
                Pathfinding.AStar(start, goal, tiles);
                break;
        }

        hasAnimated = false;
        start.SetType(NodeType.START);
        goal.SetType(NodeType.GOAL);
    }

    private void ResetTiles() {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if (tiles[x][y].type == NodeType.START) {
                    start = tiles[x][y];
                    continue;
                }

                if (tiles[x][y].type == NodeType.GOAL) {
                    goal = tiles[x][y];
                    continue;
                }

                if(tiles[x][y].type != NodeType.BARRIER){
                    tiles[x][y].SetType(NodeType.EMPTY);
                }
            }
        }

        hasAnimated = false;
    }
}
