/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 National Aeronautics and Space Administration. All rights reserved.
 *
 * This file is part of JAT. JAT is free software; you can
 * redistribute it and/or modify it under the terms of the
 * NASA Open Source Agreement
 * 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NASA Open Source Agreement for more details.
 *
 * You should have received a copy of the NASA Open Source Agreement
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package jat.coreNOSA.algorithm.optimization;

import jat.coreNOSA.algorithm.ScalarfromArrayFunction;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.tests.core.algorithm.optimization.functions.MyFunction;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.mariuszgromada.math.mxparser.Function;

import javax.swing.*;

/**
 * Davidon-Fletcher-Powell variable metric method
 * @author Tobias Berthold
 *
 */
public class DFP extends optimize
{
	public double err_ods = 1.e-4; // Error tolerance for linesearch
	public double err_dfp = 1.e-6; // Error tolerance for search
	public double eps_CD = 1.e-4; // Perturbation for central difference 
	public int max_it = 50; // maximum iterations 
	public MyFunction G;
	XYSeries series1 = new XYSeries("kolejne_minima");
	XYSeries series2 = new XYSeries("minimum_lokalne_funkcji");

	public DFP(Function function, double[] x_init)
	{
		super(function, x_init);
		G = new MyFunction(function);
	}

	void DFP_update(VectorN dx, VectorN dg, Matrix H)
	{
		int n = dx.length;
		VectorN H_dg = new VectorN(n);
		double dxT_dg = 0., dgT_H_dg = 0.;
		Matrix dx_dxT, dH, dH1, dH2, H_dg_dgT_H;

		H_dg = H.times(dg);
		dxT_dg = dx.dotProduct(dg);     // mianownik A
		dgT_H_dg = dg.dotProduct(H_dg); // mianownik B
		dx_dxT = dx.outerProduct(dx);   // licznik A
		H_dg_dgT_H = H_dg.outerProduct(H_dg); // - licznik B

		dH1 = dx_dxT.ebeDivide(dxT_dg);     // A
		dH2 = H_dg_dgT_H.ebeDivide(dgT_H_dg);   // - B
		dH = dH1.minus(dH2); // A + B
		H.setMatrix(0, 0, H.plus(dH));  // V + A + B = V(i+1)
		return;
	}

	public double[] find_min_DFP(XYSeriesCollection dataset, JTextArea area)
	{
		Matrix H = new Matrix(n); // Set H to identity matrix
		VectorN x, xn, dx, gx, gxn, dgx;
		double[] dummy;
		int i, it = 0;
		double norm = 0.;
		boolean more_iter = true;
		int status = 0;

		// Copy initial guess to x
		x = new VectorN(x_init);
		xn = new VectorN(x_init);
		gxn = new VectorN(x_init);
		//copy(x_init, this.x);
		print_header();

		gx = new VectorN(NumDerivs.G_x_central(G, x.getArray(), eps_CD)); // robi gradient
		while (more_iter)
		{
			norm = norm(gx.getArray());
			if (norm < err_dfp)
			{
				more_iter = false;
				if(Double.isNaN( G.evaluate(x.getArray()))){
					return null;
				}
				print_line(dataset, area,it, x.getArray(), G.evaluate(x.getArray()), gx.getArray(), norm);
			} else
			{
				// Step 4
				dx = H.times(gx);      ///
				dx = dx.times(-1.);    /// d(i)
				//H.print("H");
				copy(x.getArray(), xn.getArray());
				xn = GolsteinSearch.ods(G, gx, xn, dx, err_ods);
				if(Double.isNaN( G.evaluate(x.getArray()))){
					return null;
				}
				print_line(dataset, area,it, x.getArray(), G.evaluate(x.getArray()), gx.getArray(), norm);
				if(x.length == 2) series1.add(x.getArray()[0],x.getArray()[1]);
				// Step 5
				dx = xn.minus(x);
				// Step 6
				gxn = new VectorN(NumDerivs.G_x_central(G, xn.getArray(), eps_CD));
				dgx = gxn.minus(gx); // dgx = gxn - gx
				// Step 7
				DFP_update(dx, dgx, H);
				// Step 8
				copy(xn, x);
				copy(gxn, gx);
				it++;
				if (it > max_it)
				{
					more_iter = false;
					status = 1;
				}
				if (GolsteinSearch.status > 0)
				{
					System.out.println("Linesearch failed, status: " + GolsteinSearch.status);
					// x might still be minimum, check
					more_iter = false;
					status = 2;
				}
			}
		}

		// Conclusion		
		if (status == 0)
			System.out.println("Convergence:");
		if (status == 1)
			System.out.println("Maximum number of iterations reached");
		if (status == 2)
			System.out.println("Goldstein Search failed");
		for (i = 0; i < x.length; i++)
			System.out.print("x" + i + "= " + x.x[i] + "  ");
		System.out.println("");
		System.out.println("|Gx|= " + norm);
		dataset.addSeries(series1);
		series2.add(x.getArray()[0],x.getArray()[1]);
		dataset.addSeries(series2);

		return x.getArray();

	}

	private void copy(double[] from, double[] to)
	{
		System.arraycopy(from, 0, to, 0, from.length);
	}

	private void copy(VectorN from, VectorN to)
	{
		System.arraycopy(from.getArray(), 0, to.getArray(), 0, from.length);
	}

}
