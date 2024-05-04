package com.example.eventeasy.controllers.back.user;


import com.example.eventeasy.entities.User;
import com.example.eventeasy.services.UserService;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {


    public Text topText;

    public PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<User> users = UserService.getInstance().getAll();

        topText.setText("Blocked users " + users.stream().filter(user -> !user.isEnabled()).count() + " /" +
                "  Active users " + users.stream().filter(User::isEnabled).count());

        long blockedUsers = users.stream().filter(user -> !user.isEnabled()).count();
        long activeUsers = users.stream().filter(User::isEnabled).count();

        pieChart.getData().add(new PieChart.Data("Blocked users", blockedUsers));
        pieChart.getData().add(new PieChart.Data("Active users", activeUsers));

    }

}