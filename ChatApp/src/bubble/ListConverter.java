package bubble;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.UserInformation;

public class ListConverter {

    public static <T> FilteredList<T> convertToFilteredList(ObservableList<T> observableList) {
        return new FilteredList<>(observableList);
    }


}