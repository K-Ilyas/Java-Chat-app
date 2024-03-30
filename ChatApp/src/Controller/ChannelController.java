package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import DAO.UserInformation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ChannelController implements Initializable{

    @FXML
    private MFXButton cancel;

    UserInformation user = new UserInformation();
    @FXML
    private MFXCheckListView<String> checklist;

    @FXML
    private MFXButton create;

    @FXML
    private Label friendSelected;

    @FXML
    private Label newchannel;

    @FXML
    void Cancel(ActionEvent event) {
        // Implémentez ce que vous souhaitez faire lorsque le bouton "cancel" est cliqué
    }

    @FXML
    void Create(ActionEvent event) {
        // Implémentez ce que vous souhaitez faire lorsque le bouton "create" est cliqué
    }

    // Méthode pour remplir la checklistview avec des noms
    public void fillChecklistWithNames() {
        ObservableList<String> names = FXCollections.observableArrayList(
          //  user.getPseudo(),
            "Mary",
            "Alice",
            "John",
            "Mary",
            "Alice",
            "John",
            "Mary",
            "Alice",
            "John",
            "Mary",
            "Alice",
            "John",
            "Mary",
            "Alice",
            "John",
            "Mary",
            "Alice",
            "John",
            "Mary",
            "Alice",
              "John",
            "Mary",
            "Alice",
            "Bob"
            // Ajoutez d'autres noms si nécessaire
        );
        checklist.setItems(names);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillChecklistWithNames(); // Appel de la méthode pour remplir la checklistview avec des noms
    }
    
}
