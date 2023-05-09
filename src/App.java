import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application
{

    public static void main(String[] args) {
        launch(args);
    }

    /* (non-Javadoc)
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    /* (non-Javadoc)
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    
    @Override
    public void start(Stage stage) {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("CalculatorUI.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Calculator");
        ((Controller)loader.getController()).initialize();
        stage.show();
        
    }


}
