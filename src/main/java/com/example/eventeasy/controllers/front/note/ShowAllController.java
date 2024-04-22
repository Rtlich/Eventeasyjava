package com.example.eventeasy.controllers.front.note;

import com.example.eventeasy.entities.Note;
import com.example.eventeasy.services.NoteService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Note currentNote;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;
    @FXML
    public Button manageButton;

    List<Note> listNote;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listNote = NoteService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listNote);

        if (!listNote.isEmpty()) {
            for (Note note : listNote) {

                mainVBox.getChildren().add(makeNoteModel(note));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeNoteModel(
            Note note
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_NOTE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + note.getDate());

            ((Text) innerContainer.lookup("#userText")).setText("User : " + note.getUser().toString());

            int noteValue;

            try {
                noteValue = Integer.parseInt(note.getDescription());
            } catch (NumberFormatException e) {
                noteValue = 0;
            }

            innerContainer.lookup("#note1").setOpacity(noteValue >= 1 ? 1 : 0.3);
            innerContainer.lookup("#note2").setOpacity(noteValue >= 2 ? 1 : 0.3);
            innerContainer.lookup("#note3").setOpacity(noteValue >= 3 ? 1 : 0.3);
            innerContainer.lookup("#note4").setOpacity(noteValue >= 4 ? 1 : 0.3);
            innerContainer.lookup("#note5").setOpacity(noteValue >= 5 ? 1 : 0.3);

            ((Button) innerContainer.lookup("#editButton")).setOnAction((ignored) -> modifierNote(note));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerNote(note));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    public void add(ActionEvent actionEvent) {
        currentNote = null;
        com.example.eventeasy.controllers.front.MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_NOTE);
    }

    private void modifierNote(Note note) {
        currentNote = note;
        com.example.eventeasy.controllers.front.MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_NOTE);
    }

    private void supprimerNote(Note note) {
        currentNote = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer note ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (NoteService.getInstance().delete(note.getId())) {
                    com.example.eventeasy.controllers.front.MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_NOTE);
                } else {
                    AlertUtils.makeError("Could not delete note");
                }
            }
        }
    }

}
