package com.example.eventeasy.controllers.back.note;

import com.example.eventeasy.entities.Note;
import com.example.eventeasy.services.NoteService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    @FXML
    public BarChart<String, Number> chart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Note> notes = NoteService.getInstance().getAll();
        Map<Integer, Integer> map = new HashMap<>();

        int i = 0;
        for (Note note : notes) {
            int noteValue = getNote(note.getDescription());
            if (noteValue >= 0) {
                i++;

                map.put(i, noteValue);
            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            chart.getData().add(series);
        }

        chart.setLegendVisible(false);

        chart.getXAxis().setLabel("Nombre");
        chart.getYAxis().setLabel("Note");
    }


    private int getNote(String description) {
        try {
            return Integer.parseInt(description);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
