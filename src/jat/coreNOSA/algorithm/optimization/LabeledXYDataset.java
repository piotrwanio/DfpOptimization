package jat.coreNOSA.algorithm.optimization;

import org.jfree.data.xy.AbstractXYDataset;

import java.util.ArrayList;
import java.util.List;

public class LabeledXYDataset extends AbstractXYDataset {

    private static final int N = 26;
    private java.util.List<Number> x = new ArrayList<Number>(N);
    private java.util.List<Number> y = new ArrayList<Number>(N);
    private List<String> label = new ArrayList<String>(N);

    public void add(double x, double y, String label){
        this.x.add(x);
        this.y.add(y);
        this.label.add(label);
    }

    public String getLabel(int series, int item) {
        return label.get(item);
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return "Unit";
    }

    @Override
    public int getItemCount(int series) {
        return label.size();
    }

    @Override
    public Number getX(int series, int item) {
        return x.get(item);
    }

    @Override
    public Number getY(int series, int item) {
        return y.get(item);
    }
}