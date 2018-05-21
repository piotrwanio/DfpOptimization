package jat.coreNOSA.algorithm.optimization.test;

import jat.coreNOSA.algorithm.optimization.Hessian;
import jat.tests.core.algorithm.optimization.functions.MyFunction;
import org.mariuszgromada.math.mxparser.Function;

public class HessianTest {
    public static void main(String[] args) throws Exception {
        double[] x = {Math.sqrt(2)/2,0};
        Function mxParserFunction = new Function("f(x1,x2)=x1^4+x2^4-x1^2-x2^2");
        MyFunction myFunction = new MyFunction(mxParserFunction);
        Hessian h = new Hessian(myFunction);
        System.out.println(h.detHessian(x));

    }
}
