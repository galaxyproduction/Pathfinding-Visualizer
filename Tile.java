import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

import java.util.Set;

import javafx.event.EventHandler;
import javafx.scene.input.*;

public class Tile extends StackPane {
    public int x;
    public int y;

    public NodeType type;
    private Rectangle rect;
    private Text text;

    public Tile(int x, int y, double size) {
        this.x = x;
        this.y = y;

        text = new Text();
        text.setFont(Font.font(12));

        rect = new Rectangle(size, size);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);

        setAlignment(Pos.TOP_LEFT);
        getChildren().addAll(rect, text);

        SetType(NodeType.EMPTY);

        setOnMouseClicked(event -> {
            if (!Main.hasAnimated) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (type != NodeType.START && type != NodeType.GOAL) {
                        if (type == NodeType.EMPTY) {
                            SetType(NodeType.BARRIER);
                        } else {
                            SetType(NodeType.EMPTY);
                        }
                    }
                }
            }
        });

        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!Main.hasAnimated) {
                    Dragboard db = rect.startDragAndDrop(TransferMode.ANY);

                    ClipboardContent content = new ClipboardContent();
                    content.putString(type.name());
                    db.setContent(content);

                    event.consume();

                }
            }
        });

        setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != rect && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != rect && event.getDragboard().hasString()) {
                    switch (event.getDragboard().getString()) {
                        case "START":
                            rect.setFill(Color.LIMEGREEN);
                            break;
                        case "GOAL":
                            rect.setFill(Color.TOMATO);
                            break;
                        case "EMPTY":
                            SetType(NodeType.BARRIER);
                            break;
                        case "BARRIER":
                            SetType(NodeType.EMPTY);
                            break;
                    }
                    event.consume();
                }
            }
        });

        setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (!event.isDropCompleted()) {
                    if (type == NodeType.BARRIER) {
                        rect.setFill(Color.BLACK);
                    } else {
                        rect.setFill(Color.WHITE);
                    }

                    event.consume();
                }
            }
        });

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

        setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                SetType(NodeType.EMPTY);

                event.consume();
            }
        });
    }

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
}
