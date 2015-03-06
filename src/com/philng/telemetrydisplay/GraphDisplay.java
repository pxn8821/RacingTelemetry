/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author phil
 */
public class GraphDisplay extends JPanel{
    /** The time series data. */
    TimeSeries voltage;   
    TimeSeries current;

    /**
     * Build all the initial data
     */
    public GraphDisplay() {
        voltage = new TimeSeries( "Voltage" );
        current = new TimeSeries( "Current" );

        this.setLayout( new BorderLayout() );        
        final JFreeChart chart = createChart(createDatasetVoltage());
        

        final ChartPanel chartPanel = new ChartPanel(chart);

        add(chartPanel, BorderLayout.CENTER);
        chartPanel.setVisible( true );
        setVisible( true );

        // Add a mouse listener for when the user double clicks the mousse
        // on the graph, it will reset the zoome
        ChartMouseListener cml = new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
                if(chartMouseEvent.getTrigger().getClickCount() == 2){
                    chartPanel.restoreAutoBounds();
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {

            }
        };
        chartPanel.addChartMouseListener(cml);
    }

    public TimeSeries getVoltageDataset(){
        return voltage;
    }

    public TimeSeries getCurrentDataset(){
        return current;
    }

    /**
     * Create the chart itself with datasets
     * @param dataset
     * @return
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "Telemetry Display",
            "Time",
            "Voltage",
            dataset,
            true,
            true,
            false
        );
        final XYPlot plot = result.getXYPlot();

        // Add in a new y axis for current
        ValueAxis currentAxis = new NumberAxis();
        currentAxis.setRange(0,100);
        currentAxis.setLabel("Current");

        plot.setRangeAxis(1, currentAxis);
        plot.setDataset(1, createDatasetCurrent());
        plot.mapDatasetToRangeAxis(1, 1);

        // Set information for the x axis (time)
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);

        // Set the information for the voltage axis
        axis = plot.getRangeAxis();
        axis.setAutoRange(false);
        axis.setRange(0.0, 12.0);

        final XYItemRenderer renderer = plot.getRenderer();
        renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
        if (renderer instanceof StandardXYItemRenderer) {
            final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
            rr.setShapesFilled(true);
        }

        final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.GREEN);
        renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
        plot.setRenderer(1, renderer2);

        return result;
    }

    /**
     * Add data to the chart, this function ignores all errors
     * @param v voltage
     * @param c current
     */
    public void addData(Float v, Float c){
        try{
            voltage.add(new Second(), v);
            current.add(new Second(), c);
        } catch(SeriesException e){
            
        }
    }

    /**
     * Clear all the data in the thread
     */
    public void resetData(){
        voltage.clear();
        current.clear();
    }

    /**
     * Creates the voltage dataset
     * @return
     */
    private XYDataset createDatasetVoltage() {
        return new TimeSeriesCollection(voltage);
    }

    /**
     * Create the current dataset
     * @return
     */
    private XYDataset createDatasetCurrent() {
        return new TimeSeriesCollection(current);
    }
}
