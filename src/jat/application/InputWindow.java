package jat.application;

import jat.coreNOSA.math.MatrixVector.data.VectorN;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.*;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import org.mariuszgromada.math.mxparser.mXparser;
import jat.coreNOSA.algorithm.optimization.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class InputWindow implements ActionListener {

    private static final int N = 50;
    static double MAX = 0.1;
    static double MIN = 0;
    static double[] x0;
    static double rangeX = 0;
    static double rangeY = 0;
    static int max_iter = 30;
    boolean wasPainted = true;
    static JTextArea resultsArea;
    JTextField inputField, eField, iterField, eps1Field, eps2Field, eps3Field, eps4Field, betaField;
    JTextField[] x;
    ChartPanel chartPanel;
    static Function f;
    XYZDataset dataset;
    XYSeriesCollection markPoint;
    XYSeriesCollection pointsDataset;
    JFreeChart chart;
    private String sampleFunction;

    // creating new window
    public void createWindow() {
        JFrame f = new JFrame();

        java.net.URL iconURL = getClass().getResource("Icon.png");
        // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        f.setIconImage(icon.getImage());
        f.setTitle("Metoda Davidona-Fletshera-Powell'a.");

        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(124, 160, 238));

        JPanel leftPanel2 = new JPanel();
        leftPanel2.setBackground(new Color(124, 160, 238));

        JPanel leftPanel3 = new JPanel();
        leftPanel3.setBackground(new Color(124, 160, 238));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Funkcja testowa", null, leftPanel,
                "Does twice as much nothing");
        tabbedPane.addTab("Punkt początkowy", null, leftPanel2,
                "Does twice as much nothing");
        tabbedPane.addTab("Kryteria", null, leftPanel3,
                "Does twice as much nothing");

        f.getContentPane().add(BorderLayout.WEST, tabbedPane);

        //LeftPanel content
        inputField = new JTextField(20);
        inputField.setMaximumSize(inputField.getPreferredSize());
        JLabel label = new JLabel("Wprowadź funkcję testową:");
        label.setForeground(Color.white);
        eField = new JTextField(20);
        inputField.setMaximumSize(eField.getPreferredSize());
        JLabel elabel = new JLabel("Lub wybierz funkcję testową z listy:");
        elabel.setForeground(Color.white);

        //Create sample function list
        String[] functionSamples = {"Wybierz funkcję", "f(x1,x2)=x1^2+x2^2", "f(x1,x2)=100*(x2-x1^2)^2+(1-x1)^2",
                "f(x1,x2)=(x1^2+x2-11)^2+(x1+x2^2-7)^2-200",
                "f(x1,x2)=4*x1^2-2.1*x1^4+(1.0/3.0)*x1^6 +x1*x2-4*x2^2+4*x2^4"};

        //Create the combo box, select item at index 0
        JComboBox functionList = new JComboBox(functionSamples);
        functionList.setSelectedIndex(0);
        functionList.setPrototypeDisplayValue("Wybierz funkcję nanananananananana");
        functionList.setPopupVisible(false);
        functionList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JComboBox cb = (JComboBox) arg0.getSource();
                String choosedFunction = (String) cb.getSelectedItem();
                System.out.println("button A Action");
                sampleFunction = choosedFunction;
            }
        });

        JButton dfpButton = new JButton("Znajdź minimum lokalne");
        dfpButton.setForeground(Color.white);
        dfpButton.setBackground(new Color(36, 73, 152));
        dfpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                if(iterField.getText().isEmpty()){
                    resultsArea.append("Nie wprowadzono liczby iteracji.\n");
                    return;
                }
                max_iter = Integer.parseInt(iterField.getText());
                pointsDataset = new XYSeriesCollection();
                String inputFunction = inputField.getText();
                System.out.println(inputFunction);
                if (inputFunction.isEmpty()) {
                    if (sampleFunction == "Wybierz funkcję" || sampleFunction == null) {
                        resultsArea.append("Nie wybrano funkcji testowej.\n");
                        return;
                    }
                    inputFunction = sampleFunction;
                    System.out.println(inputFunction);
                }

                //         double[] x_init=new double[2];
                //(x[0] * x[0])) + 2*x[1]*x[1]-6*x[0]+x[0]*x[1])
                String f = "f(x1,x2)=x1^2+2*x2^2-6*x1+x1*x2";
                Function function = new Function(inputFunction);

                x0 = new double[function.getArgumentsNumber()];
                System.out.println("MyFunction function, Numerical derivs, DFP");

                for (int i = 0; i < function.getArgumentsNumber(); i++) {
                    if (x[i].getText().isEmpty()) {
                        resultsArea.append("Nie podano współrzędnej x_" + (i + 1) + " punktu początkowego.\n");
                        return;
                    }
                    x0[i] = Double.parseDouble(x[i].getText());
                }

                // create instance of the class DFP
                DFP dfp = null;
                try {
                    dfp = new DFP(function, x0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(eps1Field.getText().isEmpty()){
                    dfp.err_dfp = 1.e-6;
                }
                else{
                    dfp.err_dfp = Double.parseDouble(eps1Field.getText());
                }
                if(eps2Field.getText().isEmpty()){
                    dfp.eps_x = 1.e-6;
                }
                else{
                    dfp.eps_x = Double.parseDouble(eps2Field.getText());
                }
                if(eps3Field.getText().isEmpty()){
                    dfp.eps_fx = 1.e-6;
                }
                else{
                    dfp.eps_fx = Double.parseDouble(eps3Field.getText());
                }
                if(betaField.getText().isEmpty()){
                    dfp.beta = 0.4;
                }
                else{
                    dfp.beta = Double.parseDouble(betaField.getText());
                }
                if(eps4Field.getText().isEmpty()){
                    dfp.err_ods = 1.e-6;
                }
                else{
                    dfp.err_ods = Double.parseDouble(eps4Field.getText());
                }
                dfp.eps_CD = 1.e-5;
                dfp.max_it = max_iter;
                double[] x = dfp.find_min_DFP(pointsDataset, markPoint, resultsArea);
                if (x == null) {
                    wasPainted = true;
                    resultsArea.append("BŁĄD! Wprowadzona funkcja ma nieprawidłowy format! Domyślny format funkcji to f(x1,x2,...,xn) = a*x1+b*x2+...+z*xn\n");
                    return;
                }
                rangeX = 1+ 1.2 * Math.abs(x[0] - x0[0]);
                rangeY = 1+ 1.2 * Math.abs(x[1] - x0[1]);
                wasPainted = false;
            }
        });

        JButton chartButton = new JButton("Rysuj wykres");
        chartButton.setForeground(Color.white);
        chartButton.setBackground(new Color(36, 73, 152));
        chartButton.addActionListener(this);
        resultsArea = new JTextArea(10, 45);
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
        w.anchor = GridBagConstraints.WEST;
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
        //    leftPanel.add(eField, c);
        leftPanel.add(functionList, c);

        // leftPanel.add(inputField);
        // c.fill = GridBagConstraints.BOTH;
        w.ipady = 6; // make this component tall
        w.weightx = 1.0;
        w.weighty = 1.0;
        w.gridwidth = 3;
        w.gridx = 0;
        w.gridy = 3;
        w.insets = new Insets(0, 60, 0, 0);
        leftPanel.add(dfpButton, w);

        w.ipady = 6; // make this component tall
        w.weightx = 1.0;
        w.weighty = 1.0;
        w.gridwidth = 3;
        w.gridx = 1;
        w.gridy = 3;
        w.insets = new Insets(0, 60, 0, 0);
        leftPanel.add(chartButton, w);

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

        //LeftPanel2 content

        //Creating x_i input fields
        x = new JTextField[10];
        JLabel[] label_x = new JLabel[10];
        for (int i = 0; i < 10; i++) {
            x[i] = new JTextField("", 2);
            x[i].setMaximumSize(x[i].getPreferredSize());
            label_x[i] = new JLabel("x" + (i + 1) + ":");
            label_x[i].setForeground(Color.white);
        }
        JLabel initials = new JLabel("Wprowadź punkt początkowy:");
        initials.setForeground(new Color(36, 73, 152));
        initials.setFont(initials.getFont().deriveFont(15.0f));
        JLabel initials2 = new JLabel(" ");

        leftPanel2.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.anchor = GridBagConstraints.WEST;
        GridBagConstraints w2 = new GridBagConstraints();
        w2.anchor = GridBagConstraints.CENTER;
        //    w2.fill = GridBagConstraints.HORIZONTAL;

        //Adding JComponents to GridBag
        w2.weighty = 0.0;
        w2.weightx = 0.0;
        w2.gridwidth = 6;
        // w2.gridheight = 0;
        w2.gridx = 2;
        w2.gridy = 0;
        w2.insets = new Insets(30, 10, 0, 0);
        leftPanel2.add(initials, w2);

        for (int i = 0; i < 10; i++) {
            c2.weighty = 0.0;
            c2.weightx = 0.0;
            if (i % 5 == 0) c2.gridx = 0;
            if (i % 5 == 1) c2.gridx = 2;
            if (i % 5 == 2) c2.gridx = 4;
            if (i % 5 == 3) c2.gridx = 6;
            if (i % 5 == 4) c2.gridx = 8;
            c2.gridy = 3 + i / 5;
            c2.insets = new Insets(40, 10, 0, 0);
            leftPanel2.add(label_x[i], c2);

            c2.weighty = 0.0;
            c2.weightx = 1.0;
            if (i % 5 == 0) c2.gridx = 1;
            if (i % 5 == 1) c2.gridx = 3;
            if (i % 5 == 2) c2.gridx = 5;
            if (i % 5 == 3) c2.gridx = 7;
            if (i % 5 == 4) c2.gridx = 9;
            c2.gridy = 3 + i / 5;
            leftPanel2.add(x[i], c2);
        }

        c2.weighty = 1.0;
        c2.weightx = 0.0;
        c2.gridx = 2;
        c2.gridy = 6;
        leftPanel2.add(initials2, c2);

        //LeftPanel3 content

        JLabel dfpTitle = new JLabel("Parametry dla algorytmu DFP:");
        dfpTitle.setForeground(new Color(36, 73, 152));
        dfpTitle.setFont(dfpTitle.getFont().deriveFont(15.0f));

        JLabel goldTitle = new JLabel("Parametry dla algorytmu min. w kierunku (Goldsteina):");
        goldTitle.setForeground(new Color(36, 73, 152));
        goldTitle.setFont(goldTitle.getFont().deriveFont(15.0f));

        iterField = new JTextField(20);
        iterField.setMaximumSize(iterField.getPreferredSize());
        JLabel iterLabel = new JLabel("Wprowadź maksymalną liczbę iteracji:");
        iterLabel.setForeground(Color.white);
        eps1Field = new JTextField(20);
        eps1Field.setMaximumSize(eps1Field.getPreferredSize());
        JLabel eps1Label = new JLabel("Wprowadź kryterium stopu grad:");
        eps1Label.setForeground(Color.white);
        eps2Field = new JTextField(20);
        eps2Field.setMaximumSize(eField.getPreferredSize());
        JLabel eps2Label = new JLabel("Wprowadź kryterium stopu dla x:");
        eps2Label.setForeground(Color.white);
        eps3Field = new JTextField(20);
        eps3Field.setMaximumSize(eField.getPreferredSize());
        JLabel eps3Label = new JLabel("Wprowadź kryterium stopu dla f(x):");
        eps3Label.setForeground(Color.white);
        eps4Field = new JTextField(20);
        eps4Field.setMaximumSize(eField.getPreferredSize());
        JLabel eps4Label = new JLabel("Wprowadź kryterium stopu dla min. w kierunku:");
        eps4Label.setForeground(Color.white);
        betaField = new JTextField(20);
        betaField.setMaximumSize(eField.getPreferredSize());
        JLabel betaLabel = new JLabel("Wprowadź parametr beta:");
        betaLabel.setForeground(Color.white);
        JLabel dummyLabel = new JLabel(" ");
        dummyLabel.setForeground(Color.white);


        leftPanel3.setLayout(new GridBagLayout());

        GridBagConstraints c3 = new GridBagConstraints();
        c3.anchor = GridBagConstraints.WEST;
        //	c.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints w3 = new GridBagConstraints();
        w3.anchor = GridBagConstraints.CENTER;

        w3.weighty = 0.0;
        w3.gridwidth = 6;
        w3.gridx = 0;
        w3.gridy = 0;
        w3.insets = new Insets(10, 0, 0, 0);
        leftPanel3.add(dfpTitle, w3);

        c3.weighty = 0.0;
        c3.gridx = 0;
        c3.gridy = 1;
        c3.insets = new Insets(10, 10, 0, 0);
        leftPanel3.add(iterLabel, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0.0;
        c3.gridx = 1;
        c3.gridy = 1;
        leftPanel3.add(iterField, c3);

        c3.weighty = 0.0;
        c3.gridx = 0;
        c3.gridy = 2;
        leftPanel3.add(eps1Label, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0.0;
        c3.gridx = 1;
        c3.gridy = 2;
        leftPanel3.add(eps1Field, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0;
        c3.gridx = 0;
        c3.gridy = 3;
        leftPanel3.add(eps2Label, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0;
        c3.gridx = 1;
        c3.gridy = 3;
        //    leftPanel.add(eField, c);
        leftPanel3.add(eps2Field, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0;
        c3.gridx = 0;
        c3.gridy = 4;
        //    leftPanel.add(eField, c);
        leftPanel3.add(eps3Label, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0;
        c3.gridx = 1;
        c3.gridy = 4;
        //    leftPanel.add(eField, c);
        leftPanel3.add(eps3Field, c3);

        w3.weighty = 0;
        w3.gridwidth = 6;
        w3.gridx = 0;
        w3.gridy = 5;
        w3.insets = new Insets(50, 0, 0, 0);
        leftPanel3.add(goldTitle, w3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0;
        c3.gridx = 0;
        c3.gridy = 6;
        //    leftPanel.add(eField, c);
        leftPanel3.add(eps4Label, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0;
        c3.gridx = 1;
        c3.gridy = 6;
        //    leftPanel.add(eField, c);
        leftPanel3.add(eps4Field, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0;
        c3.gridx = 0;
        c3.gridy = 7;
        //    leftPanel.add(eField, c);
        leftPanel3.add(betaLabel, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 0;
        c3.gridx = 1;
        c3.gridy = 7;
        //    leftPanel.add(eField, c);
        leftPanel3.add(betaField, c3);

        // c.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.weighty = 1.0;
        c3.gridx = 1;
        c3.gridy = 8;
        //    leftPanel.add(eField, c);
        leftPanel3.add(dummyLabel, c3);


        //Adding chart panel

        DefaultXYZDataset ds = new DefaultXYZDataset();
        //  chart = createChart(createDataset());
        chart = createChart(ds);
        chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 480);
            }
        };
        chartPanel.setMouseZoomable(true, true);
        chartPanel.setMouseWheelEnabled(true);
        f.getContentPane().add(chartPanel);
        f.setSize(1100, 480);
        f.setVisible(true);
    }

    // Method that creates chart with dataset provided as an argument
    private static JFreeChart createChart(XYDataset dataset) {
        NumberAxis xAxis = new NumberAxis("x");
        NumberAxis yAxis = new NumberAxis("y");
        Range range = new Range(-5, -2);
        yAxis.setDefaultAutoRange(range);
        // yAxis.setRange(range);
        //   xAxis.setRange(range);
        yAxis.setAutoRangeIncludesZero(false);
        xAxis.setAutoRangeIncludesZero(false);
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
        r.setBlockHeight(0.1f);
        r.setBlockWidth(0.1f);
        //   r.setSeriesVisible(0, true);

        /// setting datasets
        //    plot.setDataset(0, dataset0);
        plot.setDataset(1, dataset);

        /// setting renderer for each dataset
        plot.setRenderer(1, r);
        //	plot.setRenderer(0, renderer2);


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
        double kX = ((2 * rangeX) / (double) N);
        double kY = ((2 * rangeY) / (double) N);
        for (int i = 0; i < N; i = i + 1) {
            double[][] data = new double[3][N];
            for (int j = 0; j < N; j = j + 1) {
                data[0][j] = x0[0] + rangeX - kX * i;
                data[1][j] = x0[1] + rangeY - kY * j;
                if (f != null) {
                    Expression e = new Expression("f(" + (x0[0] + rangeX - kX * i) + "," + (x0[1] + rangeY - kY * j) + ")", f);
                    data[2][j] = e.calculate();
                    if (Double.isNaN(data[2][j])) {
                        resultsArea.append("Wrong function's format:/ \n");
                        return null;
                    }
                    if (data[2][j] > MAX) {
                        MAX = data[2][j];
                    }
                    if (data[2][j] < MIN) {
                        MIN = data[2][j];
                    }
                } else {
                    data[2][j] = (double) j * (double) i / (double) N;
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

        if (wasPainted) {
            resultsArea.append("Najpierw znajdź minimum!\n");
            return;
        }
        // resultsArea.append("Wciśnięto przycisk \n");
        String inputFunction = inputField.getText();
        System.out.println(inputFunction);
        if (inputFunction.isEmpty()) {
            inputFunction = sampleFunction;
            System.out.println(inputFunction);
        }
        f = new Function(inputFunction);
        System.out.println(f.toString());
        int parnum = f.getArgumentsNumber();
        if (parnum > 2) {
            resultsArea.append("Warstwica jest rysowana tylko dla funkcji z dwoma argumentami!\n");
        }

        Expression e = new Expression("f(3,2)", f);
        //   mXparser.consolePrintln("Res: " + e.getExpressionString() + "=" );
        // e.calculate());
        double result = e.calculate();

        resultsArea.append("Liczba parametrów to " + parnum + "\n");


        dataset = createDataset();
        if (dataset == null) {
            resultsArea.append("Wprowadzona funkcja ma nieprawidłowy format! Domyślny format funkcji to f(x1,x2,...,xn) = a*x1+b*x2+...+z*xn\n");
        }
        ((XYPlot) chart.getPlot()).setDataset(1, dataset);
        ((XYPlot) chart.getPlot()).setDataset(0, pointsDataset);
        ((XYPlot) chart.getPlot()).setDataset(2, markPoint);

        /// creating renderer for line and shape chart
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesPaint(1, Color.black);
   //     renderer2.setSeriesLinesVisible(0,false);
        renderer2.setSeriesPaint(0, Color.white);

        XYLineAndShapeRenderer renderer3 = new XYLineAndShapeRenderer(true,true);
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(3); // etc.
        XYItemLabelGenerator generator =
                new StandardXYItemLabelGenerator("{0} [{1};{2}]", format, format);
        renderer3.setBaseItemLabelGenerator(generator);
        renderer3.setBaseItemLabelsVisible(true);
        renderer3.setBaseItemLabelFont(new Font("TimesRoman", Font.BOLD, 12));
        renderer3.setBaseItemLabelPaint(Color.white);
        renderer3.setSeriesItemLabelsVisible(1,false);
        renderer3.setSeriesItemLabelsVisible(0,true);
        renderer3.setSeriesPaint(1,Color.black);
        renderer3.setSeriesPaint(0,Color.white);

        /// setting lines non-visible
        //    renderer2.setSeriesLinesVisible(1, false);

        XYBlockRenderer r = new XYBlockRenderer();
        //	MAX = 1000;
        SpectrumPaintScale ps = new SpectrumPaintScale(MIN, MAX);
        r.setPaintScale(ps);
        r.setBlockHeight(2.0 * rangeY / (double) N);
        r.setBlockWidth(2.0 * rangeX / (double) N);
        r.setSeriesVisible(0, true);
        ((XYPlot) chart.getPlot()).setRenderer(1, r);
      //  ((XYPlot) chart.getPlot()).setRenderer(0, renderer2);
        ((XYPlot) chart.getPlot()).setRenderer(0, renderer3);

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
            //        resultsArea.append("Res: " + e.getExpressionString() + "=" + result + "\n");
        }

        wasPainted = true;

    }



    public static void main(String[] args) {
        InputWindow W = new InputWindow();
        W.createWindow();
    }
}
