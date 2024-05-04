package com.example.eventeasy.controllers.front.allocation;

import com.example.eventeasy.entities.Allocation;
import com.example.eventeasy.services.AllocationService;
import com.example.eventeasy.utils.AlertUtils;
import com.example.eventeasy.utils.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {

    public static Allocation currentAllocation;

    @FXML
    public Text topText;

    public VBox mainVBox;
    @FXML
    public TextField searchTF;

    List<Allocation> listAllocation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listAllocation = AllocationService.getInstance().afficher();

        displayData("");
    }

    void displayData(String searchText) {
        mainVBox.getChildren().clear();

        Collections.reverse(listAllocation);

        if (!listAllocation.isEmpty()) {
            for (Allocation allocation : listAllocation) {
                if (allocation.getNom().toLowerCase().startsWith(searchText.toLowerCase())) {
                    mainVBox.getChildren().add(makeAllocationModel(allocation));
                }

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeAllocationModel(
            Allocation allocation
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_ALLOCATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + allocation.getNom());
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + allocation.getPrix());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + allocation.getDate());
            ((Text) innerContainer.lookup("#categoryIdText")).setText("Category : " + allocation.getCategoryAllocation().toString());
            ((Text) innerContainer.lookup("#eventIdText")).setText("Event : " + allocation.getEvent().toString());

            Path selectedImagePath = FileSystems.getDefault().getPath(allocation.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

            try {
                String data = allocation.toString();
                String path = "./qr_code.jpg";
                BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);
                MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(path));

                Path qrPath = FileSystems.getDefault().getPath(path);
                if (qrPath.toFile().exists()) {
                    ((ImageView) innerContainer.lookup("#qrImage")).setImage(new Image(qrPath.toUri().toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((Button) innerContainer.lookup("#reserver")).setOnAction(event -> reserver(allocation));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void reserver(Allocation allocation) {
        if (!allocation.isUpToRent()) {
            AlertUtils.showErrorNotification("Allocation non disponible");
            return;
        }

        if (allocation.getQuantity() == 0) {
            AlertUtils.showErrorNotification("Stock épuisé");
            return;
        }

        allocation.setUpToRent(false);
        allocation.setQuantity(allocation.getQuantity() - 1);

        AlertUtils.showSuccessNotification("Reservation effectuée");


        AllocationService.getInstance().modifier(allocation);

        genererPDF(allocation);
    }


    @FXML
    private void search(KeyEvent ignored) {
        displayData(searchTF.getText());
    }

    private void genererPDF(Allocation allocation) {
        String filename = "allocation.pdf";

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filename)));
            document.open();

            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setSize(20);

            document.add(new Paragraph("- Reclamation -"));

            try {
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(allocation.getImage());
                image.scaleAbsoluteWidth(300);
                image.scaleAbsoluteHeight(300);
                image.isScaleToFitHeight();
                document.add(image);
            } catch (FileNotFoundException e) {
                System.out.println("Image not found");
            }

            document.add(new Paragraph("Nom : " + allocation.getNom(), font));
            document.add(new Paragraph("Prix : " + allocation.getPrix(), font));
            document.add(new Paragraph("Date : " + allocation.getDate(), font));
            document.add(new Paragraph("Category : " + allocation.getCategoryAllocation().toString(), font));
            document.add(new Paragraph("Event : " + allocation.getEvent().toString(), font));

            document.newPage();
            document.close();

            writer.close();

            Desktop.getDesktop().open(new File(filename));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
