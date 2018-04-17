package org.dfp;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.*;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputWindow implements ActionListener {

    private static final int N = 100;
    static double MAX = 1;
    static double MIN = 0;
    JTextArea resultsArea;
    JTextField inputField, eField;
    ChartPanel chartPanel;
    static Function f;
    XYZDataset dataset;
    JFreeChart chart;

    // creating new window
    public void createWindow() {
        JFrame f = new JFrame();

        java.net.URL iconURL = getClass().getResource("Icon.png");
        // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        f.setIconImage(icon.getImage());

        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(124, 160, 238));

        JPanel leftPanel2 = new JPanel();
        leftPanel2.setBackground(new Color(122, 16, 23));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Tab 1", null, leftPanel,
                "Does twice as much nothing");
        tabbedPane.addTab("Tab 2", null, leftPanel2,
                "Does twice as much nothing");

        // leftPanel.setSize(250, 500);
        f.getContentPane().add(BorderLayout.WEST, tabbedPane);
        inputField = new JTextField(20);
        inputField.setMaximumSize(inputField.getPreferredSize());
        JLabel label = new JLabel("Wprowadź funkcję:");
        label.setForeground(Color.white);
        eField = new JTextField(20);
        inputField.setMaximumSize(eField.getPreferredSize());
        JLabel elabel = new JLabel("Wprowadź kryterium stopu:");
        elabel.setForeground(Color.white);

        JButton inputButton = new JButton("Zatwierdź");
        inputButton.setForeground(Color.white);
        inputButton.setBackground(new Color(36, 73, 152));
        inputButton.addActionListener(this);
        resultsArea = new JTextArea(10, 40);
        resultsArea.setLineWrap(true);
        resultsArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(resultsArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints noFill = new GridBagConstraints();
        noFill.anchor = GridBagConstraints.WEST;
        noFill.fill = GridBagConstraints.NONE;

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        //	c.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints w = new GridBagConstraints();
        w.anchor = GridBagConstraints.CENTER;
        w.fill = GridBagConstraints.NONE;

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 10, 0, 0);
        leftPanel.add(label, c);

        // c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 1;
        leftPanel.add(inputField, c);

        // c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 2;
        leftPanel.add(elabel, c);

        // c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 2;
        leftPanel.add(eField, c);

        // leftPanel.add(inputField);
        // c.fill = GridBagConstraints.BOTH;
        w.ipady = 6; // make this component tall
        w.weightx = 1.0;
        w.weighty = 1.0;
        w.gridwidth = 3;
        w.gridx = 0;
        w.gridy = 3;
        leftPanel.add(inputButton, w);

        // leftPanel.add(inputButton);
        c.ipady = 60; // make this component tall
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 1.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.PAGE_END;
        leftPanel.add(scroll, c);
        // leftPanel.add(scroll);
        /*
         * JPanel rightPanel = new JPanel(); JTextField inputField2 = new
         * JTextField(20); rightPanel.add(inputField2);
         * rightPanel.setBackground(Color.black); // rightPanel.setSize(250, 500);
         * f.getContentPane().add(rightPanel); f.setSize(1000, 400); f.setVisible(true);
         */

        chart = createChart(createDataset());
        chartPanel = new ChartPanel(chart) {
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

    // Method that creates chart with dataset provided as an argument
    private static JFreeChart createChart(XYDataset dataset) {
        NumberAxis xAxis = new NumberAxis("x");
        NumberAxis yAxis = new NumberAxis("y");
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
        SpectrumPaintScale ps = new SpectrumPaintScale(0, MAX);
        r.setPaintScale(ps);
        r.setBlockHeight(1f);
        r.setBlockWidth(1f);
        r.setSeriesVisible(0, true);

        /// setting datasets
        plot.setDataset(0, dataset0);
        plot.setDataset(1, dataset);

        /// setting renderer for each dataset
        plot.setRenderer(1, r);
//		plot.setRenderer(0, renderer2);

        JFreeChart chart = new JFreeChart("Warstwica funkcji f", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        NumberAxis scaleAxis = new NumberAxis("Skala");
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

    // Creating dataset for XYLineAndShapeRenderer
    private static XYDataset createSampleDataset() {
        XYSeries series1 = new XYSeries("Series 1");
        series1.add(1.0, 3.3);
        series1.add(2.0, 4.4);
        series1.add(3.0, 1.7);
        XYSeries series2 = new XYSeries("Series 2");
        series2.add(1.0, 7.3);
        series2.add(2.0, 6.8);
        series2.add(3.0, 9.6);
        series2.add(4.0, 5.6);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    // Creating dataset for XYBlockRenderer
    private static XYZDataset createDataset() {
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        for (int i = 0; i < N; i = i + 1) {
            double[][] data = new double[3][N];
            for (int j = 0; j < N; j = j + 1) {
                data[0][j] = i / 5 - 10;
                data[1][j] = j / 5 - 10;
                if (f != null) {
                    Expression e = new Expression("f(" + (i / 5 - 10) + "," + (j / 5 - 10) + ")", f);
                    data[2][j] = e.calculate();
                    if (data[2][j] > MAX) {
                        MAX = data[2][j];
                    }
                    if (data[2][j] < MIN) {
                        MIN = data[2][j];
                    }
                } else {
                    data[2][j] = j * i / 100;
                    if (data[2][j] > MAX) {
                        MAX = data[2][j];
                    }
                    if (data[2][j] < MIN) {
                        MIN = data[2][j];
                    }
                }
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

    // actionPerformed method started by clicking "Submit" button

    public void actionPerformed(ActionEvent event) {
        // resultsArea.append("Wciśnięto przycisk \n");
        String inputFunction = inputField.getText();
        f = new Function(inputFunction);

        int parnum = f.getArgumentsNumber();

        Expression e = new Expression("f(3,2)", f);
        // mXparser.consolePrintln("Res: " + e.getExpressionString() + "=" +
        // e.calculate());
        double result = e.calculate();

        resultsArea.append("Liczba parametrów to " + parnum + "\n");


        dataset = createDataset();
        ((XYPlot) chart.getPlot()).setDataset(1, dataset);

        XYBlockRenderer r = new XYBlockRenderer();
        //	MAX = 1000;
        SpectrumPaintScale ps = new SpectrumPaintScale(MIN, MAX);
        r.setPaintScale(ps);
        r.setBlockHeight(1.0f);
        r.setBlockWidth(1.0f);
        r.setSeriesVisible(0, true);
        ((XYPlot) chart.getPlot()).setRenderer(1, r);

        // setting chart's color legend
        NumberAxis scaleAxis = new NumberAxis("Skala");
        scaleAxis.setAxisLinePaint(Color.white);
        scaleAxis.setTickMarkPaint(Color.white);
        PaintScaleLegend legend = new PaintScaleLegend(ps, scaleAxis);
        legend.setSubdivisionCount(128);
        legend.setAxisLocation(AxisLocation.TOP_OR_RIGHT);
        legend.setPadding(new RectangleInsets(10, 10, 10, 10));
        legend.setStripWidth(20);
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setBackgroundPaint(Color.WHITE);
        chart.clearSubtitles();
        chart.addSubtitle(legend);
        chart.setBackgroundPaint(Color.white);

        MAX = 1;
        MIN = 0;
        // Expression e2 = new Expression("der(y+ x^2 , x,1)");
        // mXparser.consolePrintln("Res: " + e2.getExpressionString() + " = " +
        // e2.calculate());

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
