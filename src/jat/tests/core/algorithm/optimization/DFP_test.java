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

package jat.tests.core.algorithm.optimization;

import jat.coreNOSA.algorithm.optimization.DFP;
import jat.coreNOSA.algorithm.optimization.LabeledXYDataset;
import jat.tests.core.algorithm.optimization.functions.MyFunction;
import org.jfree.data.xy.XYSeriesCollection;
import org.mariuszgromada.math.mxparser.Function;

import javax.swing.*;

/**
 * Davidon-Fletcher-Powell variable metric method
 * @author Tobias Berthold
 *
 */
public class DFP_test
{

	public static void main(String argv[])
	{
		double[] x_init=new double[2];
		//(x[0] * x[0])) + 2*x[1]*x[1]-6*x[0]+x[0]*x[1])
		String f = "f(x1,x2)=x1^2+2*x2^2-6*x1+x1*x2";
		Function function = new Function(f);
		JTextArea area = new JTextArea();
		XYSeriesCollection ds = new XYSeriesCollection();
		XYSeriesCollection ds2 = new XYSeriesCollection();

		System.out.println("MyFunction function, Numerical derivs, DFP");

		// create instances of the classes
		DFP_test dt = new DFP_test();
		x_init[0] = 0;
		x_init[1] = 0;
		DFP dfp = null;
		try {
			dfp = new DFP(function, x_init);
		} catch (Exception e) {
			e.printStackTrace();
		}


		dfp.err_ods=1.e-6;
		dfp.err_dfp=1.e-6;
		dfp.eps_CD=1.e-5;
		dfp.max_it=50;
		double[] x=dfp.find_min_DFP(ds, ds2, area);
		
	}
}
