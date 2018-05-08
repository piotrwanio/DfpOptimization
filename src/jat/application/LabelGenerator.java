package jat.application;

import jat.coreNOSA.algorithm.optimization.LabeledXYDataset;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;

public class LabelGenerator implements XYItemLabelGenerator {

    @Override
    public String generateLabel(XYDataset dataset, int series, int item) {
        LabeledXYDataset labelSource = (LabeledXYDataset) dataset;
        return labelSource.getLabel(series, item);
    }

}