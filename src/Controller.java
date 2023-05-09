import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private TextField tfField;

    private String expression = "";

    public void initialize() {
        tfField.setFocusTraversable(false);
    }

    @FXML
    void buttonPressed(ActionEvent event) {
        String text = ((Button)event.getSource()).getText();

        if(text.equals(""))
        {
            text = ((Text)((StackPane)((Button)event.getSource()).getParent()).getChildren().get(1)).getText();
        }

        checkInvalid();

        expression = tfField.getText();
        expression += text;
        tfField.setText(expression);

    }

    private void checkInvalid() {

        if(tfField.getText().equals("Invalid input"))
        {
            expression = "";
            tfField.setText("");
        }

    }

    @FXML
    void clear(ActionEvent event) {
        tfField.clear();
    }

    @FXML
    void delete(ActionEvent event) {

        checkInvalid();

        expression = tfField.getText();

        if(expression.equals(""))
        {
            return;
        }

        StringBuffer del = new StringBuffer(expression);
        del.deleteCharAt(del.length() - 1);
        expression = del.toString();
        tfField.setText(expression);
    }

    @FXML
    void printAnswer(ActionEvent event) {

        checkInvalid();

        expression = tfField.getText();
        expression = Calculator.solveExpression(expression);
        tfField.setText(expression);
    }

}

