package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Initial implements Initializable {
	@FXML
	Label label;
	@FXML
	TextField text;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
		return;
    }

	public void buttonHandler() {
		label.setText(text.getText());
	}
}
