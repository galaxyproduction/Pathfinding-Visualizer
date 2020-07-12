import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.scene.input.*;

public class Tile extends StackPane {
    public NodeType type;
    private Rectangle rect;
    private Text text;

    public Tile(double size) {
        text = new Text();
        text.setFont(Font.font(12));
        
        rect = new Rectangle(size, size);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);

        setAlignment(Pos.TOP_LEFT);
        getChildren().addAll(rect, text);

        SetType(NodeType.EMPTY);

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if(type != NodeType.START && type != NodeType.GOAL){
                    if(type == NodeType.EMPTY){
                        SetType(NodeType.BARRIER);
                    } else {
                        SetType(NodeType.EMPTY);
                    }
                }
            }
        });

        this.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(type == NodeType.START ||  type == NodeType.GOAL) {
                    Dragboard db = rect.startDragAndDrop(TransferMode.ANY);

                    ClipboardContent content = new ClipboardContent();
                    content.putString(type.name());
                    db.setContent(content);
    
                    event.consume();
                }
            }
        });

        this.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != rect && event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        this.setOnDragEntered(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent event) {
                if(event.getGestureSource() != rect && event.getDragboard().hasString()){
                    if(event.getDragboard().getString() == "START"){
                        rect.setFill(Color.GREEN);
                    } else {
                        rect.setFill(Color.RED);
                    }
                    
                    event.consume();
                }
            }      
        });

        this.setOnDragExited(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent event) {
                if(!event.isDropCompleted()){
                    if(type == NodeType.BARRIER){
                        rect.setFill(Color.BLACK);
                    } else {
                        rect.setFill(Color.WHITE);
                    }

                    event.consume();
                }
            }      
        });

        this.setOnDragDropped(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if(db.hasString()){
                    SetType(NodeType.valueOf(db.getString()));
                    success = true;
                }

                event.setDropCompleted(success);

                event.consume();
            }  
        });

        this.setOnDragDone(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent event) {
                if(event.getTransferMode() == TransferMode.MOVE){
                    SetType(NodeType.EMPTY);
                }

                event.consume();
            }    
        });
    }

    public void SetType(NodeType nodeType){
        type = nodeType;

        switch(type){
            case BARRIER:
                rect.setFill(Color.BLACK);
                break;
            case START:
                rect.setFill(Color.GREEN);
                break;
            case GOAL:
                rect.setFill(Color.RED);
                break;
            default:
                rect.setFill(Color.WHITE);
                break;
        }
    }
}
