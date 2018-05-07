package jat.coreNOSA.algorithm.optimization;

import jat.coreNOSA.algorithm.ScalarfromArrayFunction;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

public class GolsteinSearch {
    static public int n; // Dimension of search vector
    static public double err_ods = 1.e-6d; // error tolerance for linesearch
    static public double[] xnew; //
    static ScalarfromArrayFunction G; //
    static int status;

	/*
	double G(double[] x)
	{
		return G.evaluate(x);
	}
	*/

    static double get_new_G(ScalarfromArrayFunction G, double[] x, double alpha, double[] d) {
        int i;

        for (i = 0; i < n; i++)
            xnew[i] = x[i] + alpha * d[i];

        return G.evaluate(xnew);
    }

    // CAUTION: the parameter x is changed in the function!!!

    /**
     * Performs a linesearch (one-dimensional search) on the function G(x) in the
     * search direction d.
     *
     * @return x : found minimum
     */
    public static VectorN ods(ScalarfromArrayFunction G, VectorN gx, VectorN x, VectorN d, double err_ods, double beta) {
        int iterations = 0;
        double p = 0;
        double B = beta;
        double tr = 4.0, tl = 0, t = 0, ts = 0;
        double w1, w2;
        // d.set(0,1);
        //  d.set(1,0);

        VectorN z, tr_d, t_d, x_plus_t_d;

        p = gx.dotProduct(d);
        tr_d = d.times(tr);
        z = x.plus(tr_d);

        while (G.evaluate(z.getArray()) >= G.evaluate(x.getArray())) {
            if (tr > 0) {
                tr = tr - 0.1;
            }
            if (tr <= 0) {
                System.out.println("tr mniejsze od 0!");
                tr += 0.0001;
            }
            tr_d = d.times(tr);
            z = x.plus(tr_d);
            w1 = G.evaluate(z.getArray());
            w2 = G.evaluate(x.getArray());
            w2 = 0;
        }

        t = 0.5 * (tl + tr);
        t_d = d.times(t);
        x_plus_t_d = x.plus(t_d);
        w1 = G.evaluate(x_plus_t_d.getArray());

        while (true) {
          //  ts = t;
            if (G.evaluate(x_plus_t_d.getArray()) < (G.evaluate(x.getArray()) + (1 - B) * p * t)) {
                tl = t;
                t = 0.5 * (tl + tr);
                t_d = d.times(t);
                x_plus_t_d = x.plus(t_d);
                //   w1 = G.evaluate(x_plus_t_d.getArray());
            } else if (G.evaluate(x_plus_t_d.getArray()) > (G.evaluate(x.getArray()) + (B) * p * t)) {
                tr = t;
                t = 0.5 * (tl + tr);
                t_d = d.times(t);
                x_plus_t_d = x.plus(t_d);
                //    w1 = G.evaluate(x_plus_t_d.getArray());
            }
         /*   if(get_new_G(G,x.getArray(),t,d.getArray())-get_new_G(G,x.getArray(),ts,d.getArray())<= err_ods){
                break;
            }*/
            else {
             break;
            }
            iterations++;
            if (iterations > 100) break;
        }

        status = 0;
        n = x.length;
        xnew = new double[n];

        // Enter minimum into x
        int i;
        for (i = 0; i < n; i++)
            x.getArray()[i] = x.getArray()[i] + t * d.getArray()[i];
        return x;
    }
}

/*
// compute temporary result for printing
double[] xtmp = new double[n];
int i;
for (i = 0; i < n; i++)
	xtmp[i] = x[i] + a4 * d[i];
//			System.out.println(xtmp[0]+" "+xtmp[1]);
*/
