package com.company;

import javafx.application.Application;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Controller extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("");
        // определение осей
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Признаки");
        // создание графика

        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

        // Делаем график искомой функции
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Функция Y = √x");

        // создаем популяцию с 20ю особями, по 100 признаков у каждой
        Population population = new Population(20, 100);
        population.create(); // заполняем популяцию особями

        double[] x = population.getX();
        double[] y = population.getFunc();
        for (int i = 0; i < x.length; i++){
            series1.getData().add(new XYChart.Data(x[i], y[i]));
        }

        // Оси
        final NumberAxis xAxis2 = new NumberAxis();
        final NumberAxis yAxis2 = new NumberAxis();
        // Создание графика
        final LineChart<Number,Number> lineChart2 = new LineChart<Number,Number>(xAxis2, yAxis2);

        // Запускаем генетический алгоритм
        population.start();

        // Делаем график признаков особи с наименьшим эвклидовым расстоянием
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Признаки особи");

        double[] y2 = population.getMinEuclideanDistanceElem().getSigns();
        double[] x2 = x;

        // все х и у вбиваем в график поточечно
        for (int i = 0; i < x.length; i++){
            series2.getData().add(new XYChart.Data(x2[i], y2[i]));
        }

        FlowPane root = new FlowPane(lineChart, lineChart2);
        Scene scene  = new Scene(root);

        lineChart.getData().add(series1);
        lineChart2.getData().add(series2);

        stage.setScene(scene);
        stage.show();
    }
}
