package com.example.eventeasy.controllers.front.contrat;

import com.example.eventeasy.entities.Contrat;
import com.example.eventeasy.services.ContratService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {

    public static Contrat currentContrat;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;


    List<Contrat> listContrat;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listContrat = ContratService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listContrat);

        if (!listContrat.isEmpty()) {
            for (Contrat contrat : listContrat) {

                mainVBox.getChildren().add(makeContratModel(contrat));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeContratModel(
            Contrat contrat
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_CONTRAT)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#datedebutText")).setText("Date debut : " + contrat.getDatedebut());
            ((Text) innerContainer.lookup("#datefinText")).setText("Date fin : " + contrat.getDatefin());

            ((Text) innerContainer.lookup("#partenaireText")).setText("Partenaire : " + contrat.getPartenaire().toString());

            ((Button) innerContainer.lookup("#pdfButton")).setOnAction(ignored -> generatePdf(contrat));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void generatePdf(Contrat contrat) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("contrat.pdf")));
            document.open();

            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setSize(20);

            document.add(new Paragraph("- Contrat -"));

            try {
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(contrat.getPartenaire().getImage());
                image.scaleAbsoluteWidth(300);
                image.scaleAbsoluteHeight(300);
                image.isScaleToFitHeight();
                document.add(image);
            } catch (FileNotFoundException | NullPointerException e) {
                AlertUtils.makeError("Image not found, PDF will display without image");
            }

            document.add(new Paragraph("Date debut : " + contrat.getDatedebut()));
            document.add(new Paragraph("Date fin : " + contrat.getDatefin()));
            if (contrat.getPartenaire() != null) {
                document.add(new Paragraph("Nom Partenaire : " + contrat.getPartenaire().getNom()));
                document.add(new Paragraph("Tel Partenaire : " + contrat.getPartenaire().getTel()));
                document.add(new Paragraph("Don Partenaire : " + contrat.getPartenaire().getDon()));
            } else {
                document.add(new Paragraph("Partenaire : null"));
            }

            document.newPage();
            document.close();

            writer.close();

            Desktop.getDesktop().open(new File("contrat.pdf"));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
