import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.scene.input.*;

public class Tile extends StackPane {
    private int x;
    private int y;
    private int cost;

    private NodeType type;
    private Rectangle rect;

    public Tile(int x, int y, double size) {
        this.x = x;
        this.y = y;
        this.cost = 0;

        rect = new Rectangle(size, size);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);

        setAlignment(Pos.TOP_LEFT);
        getChildren().addAll(rect);

        SetType(NodeType.EMPTY);

        /* All UI input is blocked while animation is playing*/
        // Toggles weather a tile is a barrier
        setOnMousePressed(event -> {
            if (!Main.isAnimationPlaying) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (type != NodeType.START && type != NodeType.GOAL) {
                        if (type == NodeType.BARRIER) {
                            SetType(NodeType.EMPTY);
                        } else {
                            SetType(NodeType.BARRIER);
                        }
                    }
                }
            }
        });

        /* Rest of constructor handles all drag and drop inputs */
        // Starts the drag and drop event
        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!Main.isAnimationPlaying) {
                    Dragboard db = rect.startDragAndDrop(TransferMode.ANY);

                    ClipboardContent content = new ClipboardContent();
                    content.putString(type.name());
                    db.setContent(content);

                    event.consume();

                }
            }
        });

        // Allows for the DragBoard to be copied
        setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        // Changes the tiles color when mouse is dragged overed
        setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getDragboard().hasString()) {
                    // Can only have one start and goal node, so only a visual change when 
                    switch (event.getDragboard().getString()) {
                        case "START":
                            rect.setFill(Color.LIMEGREEN);
                            break;
                        case "GOAL":
                            rect.setFill(Color.TOMATO);
                            break;
                        case "EMPTY":
                            SetType(NodeType.EMPTY);
                            break;
                        case "BARRIER":
                            SetType(NodeType.BARRIER);
                            break;
                    }
                    event.consume();
                }
            }
        });

        // Changes color back to original when mouse leaves
        setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (!event.isDropCompleted()) {
                    if(type == NodeType.BARRIER){
                        SetType(NodeType.BARRIER);
                    } else {
                        SetType(NodeType.EMPTY);
                    }

                    event.consume();
                }
            }
        });

        // Changes the type of the tile when drag is finially finished
        setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    SetType(NodeType.valueOf(db.getString()));
                    success = true;
                }

                event.setDropCompleted(success);

                event.consume();
            }
        });

        // Sets tile which drag was initiated to empty
        setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(event.getDragboard().toString() == "START" || event.getDragboard().toString() == "GOAL")
                    SetType(NodeType.EMPTY);

                event.consume();
            }
        });
    }

    // The color of the tile is tied to the type so both change here
    public void SetType(NodeType nodeType) {
        type = nodeType;

        switch (type) {
            case BARRIER:
                rect.setFill(Color.BLACK);
                break;
            case START:
                rect.setFill(Color.LIMEGREEN);
                break;
            case GOAL:
                rect.setFill(Color.TOMATO);
                break;
            case VISITIED:
                rect.setFill(Color.DARKGRAY);
                break;
            case FRONTIER:
                rect.setFill(Color.DODGERBLUE);
                break;
            case PATH:
                rect.setFill(Color.KHAKI);
                break;
            default:
                rect.setFill(Color.WHITE);
                break;
        }
    }

    public NodeType GetType() {
        return type;
    }

    public void SetCost(int cost){
        this.cost = cost;
    }

    public int GetCost(){
        return cost;
    }

    public int GetX() {
        return x;
    }

    public int GetY() {
        return y;
    }
}
