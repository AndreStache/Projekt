package vorlesungsplan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vorlesungsplan.ui.ControllerVV;

import java.net.URL;


public class ApplicationVorlesungsplan extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //	@Override
//	public void start(Stage stage) throws Exception{
//		stage.setTitle("Vorlesungsplan");
//		final URL fxml = getClass().getResource("/vorlesungsplan/ui/ViewVorlesungsplan.fxml");
//		final FXMLLoader loader = new FXMLLoader(fxml);
//		loader.setController(new ControllerVV());
//		final Parent root = loader.load();
//		Scene scene = new Scene(root);
//		stage.setScene(scene);
//		stage.show();
//		
//	}
    public void start(Stage stage) throws Exception {
        stage.setTitle("Vorlesungsplan");
        final URL fxml = getClass().getResource("/vorlesungsplan/ui/ViewVorlesungsplan.fxml");
        final FXMLLoader loader = new FXMLLoader(fxml);
        loader.setController(new ControllerVV());
        final Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
