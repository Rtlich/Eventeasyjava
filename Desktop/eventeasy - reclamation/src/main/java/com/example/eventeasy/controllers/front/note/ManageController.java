package com.example.eventeasy.controllers.front.note;


import com.example.eventeasy.controllers.front.MainWindowController;
import com.example.eventeasy.controllers.front.note.ShowAllController;
import com.example.eventeasy.entities.Note;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.services.NoteService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public ComboBox<User> userCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;
    @FXML
    public ImageView note1;
    @FXML
    public ImageView note2;
    @FXML
    public ImageView note3;
    @FXML
    public ImageView note4;
    @FXML
    public ImageView note5;

    Note currentNote;

    int currentNoteValue = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (User user : NoteService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }

        note1.setOpacity(0.2);
        note2.setOpacity(0.2);
        note3.setOpacity(0.2);
        note4.setOpacity(0.2);
        note5.setOpacity(0.2);
        

        currentNote = com.example.eventeasy.controllers.front.note.ShowAllController.currentNote;

        note1.setOnMouseClicked((ignored) -> {
            currentNoteValue = 1;
            note1.setOpacity(1);
            note2.setOpacity(0.2);
            note3.setOpacity(0.2);
            note4.setOpacity(0.2);
            note5.setOpacity(0.2);
        });
        note2.setOnMouseClicked((ignored) -> {
            currentNoteValue = 2;
            note1.setOpacity(1);
            note2.setOpacity(1);
            note3.setOpacity(0.2);
            note4.setOpacity(0.2);
            note5.setOpacity(0.2);
        });
        note3.setOnMouseClicked((ignored) -> {
            currentNoteValue = 3;
            note1.setOpacity(1);
            note2.setOpacity(1);
            note3.setOpacity(1);
            note4.setOpacity(0.2);
            note5.setOpacity(0.2);
        });
        note4.setOnMouseClicked((ignored) -> {
            currentNoteValue = 4;
            note1.setOpacity(1);
            note2.setOpacity(1);
            note3.setOpacity(1);
            note4.setOpacity(1);
            note5.setOpacity(0.2);
        });
        note5.setOnMouseClicked((ignored) -> {
            currentNoteValue = 5;
            note1.setOpacity(1);
            note2.setOpacity(1);
            note3.setOpacity(1);
            note4.setOpacity(1);
            note5.setOpacity(1);
        });
        if (currentNote != null) {
            topText.setText("Modifier note");
            btnAjout.setText("Modifier");

            try {
                userCB.setValue(currentNote.getUser());


                int noteValue;

                try {
                    noteValue = Integer.parseInt(currentNote.getDescription());
                } catch (NumberFormatException e) {
                    noteValue = 0;
                }

                note1.setOpacity(noteValue >= 1 ? 1 : 0.2);
                note2.setOpacity(noteValue >= 2 ? 1 : 0.2);
                note3.setOpacity(noteValue >= 3 ? 1 : 0.2);
                note4.setOpacity(noteValue >= 4 ? 1 : 0.2);
                note5.setOpacity(noteValue >= 5 ? 1 : 0.2);

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter note");
            btnAjout.setText("Ajouter");
        }


    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Note note = new Note();

            note.setDescription(String.valueOf(currentNoteValue));
            note.setDate(java.time.LocalDate.now());
            note.setUser(userCB.getValue());

            if (currentNote == null) {
                if (NoteService.getInstance().add(note)) {
                    AlertUtils.makeSuccessNotification("Note ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_NOTE);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                note.setId(currentNote.getId());
                if (NoteService.getInstance().edit(note)) {
                    AlertUtils.makeSuccessNotification("Note modifié avec succés");
                    ShowAllController.currentNote = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_NOTE);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {
        if (currentNoteValue == 0) {
            AlertUtils.makeInformation("Veuillez choisir une note");
            return false;
        }

        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }
        return true;
    }
}