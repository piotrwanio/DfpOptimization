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

package jat.tests.core.algorithm.optimization.functions;

import jat.coreNOSA.algorithm.ScalarfromArrayFunction;
import org.mariuszgromada.math.mxparser.*;

import static java.lang.Double.NaN;

public class MyFunction implements ScalarfromArrayFunction {
    private Function f;

    public Function getF() {
        return f;
    }

    public MyFunction(Function f) {
        this.f = f;
    }

    public double evaluate(double[] x) {
        int n;
        n = f.getArgumentsNumber();
        String expression;
        expression = "f(";
        for (int i = 0; i < n; i++) {
            expression = expression + x[i];
            if (i < n - 1) {
                expression = expression + ",";
            }
        }
        expression = expression + ")";
        Expression e = new Expression(expression, f);
        double result = e.calculate();

        return result;
    }
}
