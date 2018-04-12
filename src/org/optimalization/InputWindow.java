package org.optimalization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import org.mariuszgromada.math.mxparser.mXparser;
//import org.mariuszgromada.math.mxparser.mXparser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Paint;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

public class InputWindow implements ActionListener {

    private static final int N = 1000;
	JTextArea resultsArea;
	JTextField inputField;
	
 // creating new window
	public void createWindow() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.pink);

		// leftPanel.setSize(250, 500);
		f.getContentPane().add(BorderLayout.WEST, leftPanel);
		inputField = new JTextField(20);
		inputField.setMaximumSize(inputField.getPreferredSize());
		JLabel label = new JLabel("Wprowadü funkcjÍ:");

		JButton inputButton = new JButton("Zatwierdü");
		inputButton.addActionListener(this);
		resultsArea = new JTextArea(10, 40);
		resultsArea.setLineWrap(true);
		resultsArea.setEditable(false);

		JScrollPane scroll = new JScrollPane(resultsArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(10, 30, 0, 0);
		leftPanel.add(label, c);

		// c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 1;
		leftPanel.add(inputField, c);

		// leftPanel.add(inputField);
		// c.fill = GridBagConstraints.BOTH;
		c.ipady = 40; // make this component tall
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 2;
		leftPanel.add(inputButton, c);

		// leftPanel.add(inputButton);
		c.ipady = 0; // make this component tall
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.PAGE_END;
		leftPanel.add(scroll, c);
		// leftPanel.add(scroll);
/*
		JPanel rightPanel = new JPanel();
		JTextField inputField2 = new JTextField(20);
		rightPanel.add(inputField2);
		rightPanel.setBackground(Color.black);
		// rightPanel.setSize(250, 500);
		f.getContentPane().add(rightPanel);
		f.setSize(1000, 400);
		f.setVisible(true);
*/
		
        ChartPanel chartPanel = new ChartPanel(createChart(createDataset())) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        chartPanel.setMouseZoomable(true, false);
		f.getContentPane().add(chartPanel);
		f.setSize(1000, 400);
		f.setVisible(true);
	}
	

    private static JFreeChart createChart(XYDataset dataset) {
        NumberAxis xAxis = new NumberAxis("x Axis");
        NumberAxis yAxis = new NumberAxis("y Axis");
        XYDataset dataset0 = createSampleDataset();
        XYPlot plot = new XYPlot();
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);
        
        /// creating renderer for line and shape chart
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        /// setting lines non-visible 
        renderer2.setSeriesLinesVisible(0, false);
        
        // creating renderer for map chart
        XYBlockRenderer r = new XYBlockRenderer();
        SpectrumPaintScale ps = new SpectrumPaintScale(0, N * N);
        r.setPaintScale(ps);
        r.setBlockHeight(10.0f);
        r.setBlockWidth(10.0f);
        r.setSeriesVisible(0, true);
        
        /// setting datasets
        plot.setDataset(0, dataset0);
        plot.setDataset(1, dataset);
        
        /// setting renderer for each dataset
        plot.setRenderer(1, r);        
        plot.setRenderer(0, renderer2);        
        
        JFreeChart chart = new JFreeChart("Wykres",
            JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        NumberAxis scaleAxis = new NumberAxis("Scale");
        scaleAxis.setAxisLinePaint(Color.white);
        scaleAxis.setTickMarkPaint(Color.white);
        PaintScaleLegend legend = new PaintScaleLegend(ps, scaleAxis);
        legend.setSubdivisionCount(128);
        legend.setAxisLocation(AxisLocation.TOP_OR_RIGHT);
        legend.setPadding(new RectangleInsets(10, 10, 10, 10));
        legend.setStripWidth(20);
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setBackgroundPaint(Color.WHITE);
        chart.addSubtitle(legend);
        chart.setBackgroundPaint(Color.white);
        return chart;
    }

    private static XYDataset createSampleDataset() {
        XYSeries series1 = new XYSeries("Series 1");
        series1.add(100.0, 300.3);
        series1.add(200.0, 400.4);
        series1.add(300.0, 100.7);
        XYSeries series2 = new XYSeries("Series 2");
        series2.add(100.0, 700.3);
        series2.add(200.0, 600.8);
        series2.add(300.0, 900.6);
        series2.add(400.0, 500.6);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

	private static XYZDataset createDataset() {
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        for (int i = 0; i < N; i = i + 10) {
            double[][] data = new double[3][N];
            for (int j = 0; j < N; j = j + 10) {
                data[0][j] = i;
                data[1][j] = j;
                data[2][j] = i * j;
            }
            dataset.addSeries("Series" + i, data);
        }
        return dataset;
    }

    private static class SpectrumPaintScale implements PaintScale {

        private static final float H1 = 0f;
        private static final float H2 = 1f;
        private final double lowerBound;
        private final double upperBound;

        public SpectrumPaintScale(double lowerBound, double upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public double getLowerBound() {
            return lowerBound;
        }

        @Override
        public double getUpperBound() {
            return upperBound;
        }

        @Override
        public Paint getPaint(double value) {
            float scaledValue = (float) (value / (getUpperBound() - getLowerBound()));
            float scaledH = H1 + scaledValue * (H2 - H1);
            return Color.getHSBColor(scaledH, 1f, 1f);
        }
    }
	
	
	
	public void actionPerformed(ActionEvent event) {
	//	resultsArea.append("WciúniÍto przycisk \n");
		String inputFunction = inputField.getText();
		Function f = new Function(inputFunction);
		
		int parnum = f.getArgumentsNumber();
		
		Expression e = new Expression("f(3,2)", f);
		// mXparser.consolePrintln("Res: " + e.getExpressionString() + "=" +
		// e.calculate());
		double result = e.calculate();
		
		resultsArea.append("Liczba parametrÛw to " + parnum + "\n");
		
	//	Expression e2 = new Expression("der(y+ x^2 , x,1)");
	//	mXparser.consolePrintln("Res: " + e2.getExpressionString() + " = " + e2.calculate());

		if (Double.isNaN(result)) {
			resultsArea.append("Wrong function's format:/ \n");
		} else {
			resultsArea.append("Res: " + e.getExpressionString() + "=" + result + "\n");
		}
	}

	public static void main(String[] args) {
		InputWindow W = new InputWindow();
		W.createWindow();
	}
}
