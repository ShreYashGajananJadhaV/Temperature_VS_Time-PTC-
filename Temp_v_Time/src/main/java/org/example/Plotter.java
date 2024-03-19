package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Plotter {

    public static void main(String[] args) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader("D:\\All downloads\\SpringProjects\\Temp_v_Time\\src\\main\\resources\\data.csv"));

        reader.readLine();

        Data data = new Data();
        Queue<Entry> entries = new LinkedList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            Entry entry = Entry.builder()
                    .serialNum(Integer.valueOf(line.split(",")[0]))
                    .temperature(Float.parseFloat(line.split(",")[1]))
                    .time(Integer.parseInt(line.split(",")[2]))
                    .build();

            entries.add(entry);

        }
        data.setEntries(entries);


        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Temperature_VS_Time");

        Entry entry = data.getEntries().poll();

        series.add(entry.getTime(), entry.getTemperature());
        dataset.addSeries(series);


        JFreeChart chart = ChartFactory.createXYLineChart(
                "Graph",
                "Time(sec)",
                "Temperature(°C)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.setBorderPaint(
                new Color(25, 233, 0)
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));

        ApplicationFrame frame = new ApplicationFrame("Graph");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);


        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {

                Entry currEntry = data.getEntries().poll();
                series.add(currEntry.getTime(), currEntry.getTemperature());

                chart.getXYPlot().setDataset(dataset);
                chartPanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);


    }
}