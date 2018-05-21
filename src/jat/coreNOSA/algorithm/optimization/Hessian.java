package jat.coreNOSA.algorithm.optimization;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.tests.core.algorithm.optimization.functions.MyFunction;
import javacalculus.core.CALC;
import javacalculus.core.CalcParser;
import javacalculus.evaluator.CalcSUB;
import javacalculus.struct.CalcDouble;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;
import org.mariuszgromada.math.mxparser.Function;

public class Hessian {
    Function functionMxparser;
    public String functionString;
    public String testString;
    public String x1, x2;
    public CalcObject resultDF_DX1_DX1;
    public CalcObject resultDF_DX1_DX2;
    public CalcObject resultDF_DX2_DX1;
    public CalcObject resultDF_DX2_DX2;

    public Hessian(MyFunction function) throws Exception {
        functionMxparser = function.getF();
        functionString = functionMxparser.getFunctionExpressionString();
        functionString = functionString.replace("sin", "SIN");
        functionString = functionString.replace("cos", "COS");
        functionString = functionString.replace("ln", "LN");
        functionString = functionString.replace("exp", "1*");

        x1 = functionMxparser.getArgument(0).getArgumentName();
        x2 = functionMxparser.getArgument(1).getArgumentName();

        //Diff df/dx1 calculate
        String command = "DIFF(" + functionString + ", " + x1 + ")";
        CalcParser parser = new CalcParser();
        CalcObject parsed = parser.parse(command);
        CalcObject resultDF_DX1 = parsed.evaluate();
        resultDF_DX1 = CALC.SYM_EVAL(resultDF_DX1);
        String resultDF_DX1_String = resultDF_DX1.toString();

        //Diff df/dx2 calculate
        command = "DIFF(" + functionString + ", " + x2 + ")";
        parsed = parser.parse(command);
        CalcObject resultDF_DX2 = parsed.evaluate();
        resultDF_DX2 = CALC.SYM_EVAL(resultDF_DX2);
        String resultDF_DX2_String = resultDF_DX2.toString();

        //Diff df/dx1_dx1 calculate
        command = "DIFF(" + resultDF_DX1_String + ", " + x1 + ")";
        parsed = parser.parse(command);
        resultDF_DX1_DX1 = parsed.evaluate();
        resultDF_DX1_DX1 = CALC.SYM_EVAL(resultDF_DX1_DX1);
        String resultDF_DX1_DX1_String = resultDF_DX1_DX1.toString();

        //Diff df/dx1_dx2 calculate
        command = "DIFF(" + resultDF_DX1_String + ", " + x2 + ")";
        parsed = parser.parse(command);
        resultDF_DX1_DX2 = parsed.evaluate();
        resultDF_DX1_DX2 = CALC.SYM_EVAL(resultDF_DX1_DX2);
        String resultDF_DX1_DX2_String = resultDF_DX1_DX2.toString();

        //Diff df/dx2_dx2 calculate
        command = "DIFF(" + resultDF_DX2_String + ", " + x2 + ")";
        parsed = parser.parse(command);
        resultDF_DX2_DX2 = parsed.evaluate();
        resultDF_DX2_DX2 = CALC.SYM_EVAL(resultDF_DX2_DX2);
        String resultDF_DX2_DX2_String = resultDF_DX2_DX2.toString();

        //Diff df/dx2_dx1 calculate
        command = "DIFF(" + resultDF_DX2_String + ", " + x1 + ")";
        parsed = parser.parse(command);
        resultDF_DX2_DX1 = parsed.evaluate();
        resultDF_DX2_DX1 = CALC.SYM_EVAL(resultDF_DX2_DX1);
        String resultDF_DX2_DX1_String = resultDF_DX2_DX1.toString();


        testString = resultDF_DX1_DX1_String + "\n" + resultDF_DX1_DX2_String + "\n" + resultDF_DX2_DX1_String + "\n" + resultDF_DX2_DX2_String;
    }

    public double detHessian(double[] x){
        double det = 0;
        // compute numerical value
        resultDF_DX1_DX1 = subst(resultDF_DX1_DX1, x1, x[0]);
        resultDF_DX1_DX1 = subst(resultDF_DX1_DX1, x2 , x[1]);
        resultDF_DX1_DX1 = CALC.SYM_EVAL(resultDF_DX1_DX1);
        double dx1dx1 = Double.parseDouble(resultDF_DX1_DX1.toString());

        resultDF_DX1_DX2 = subst(resultDF_DX1_DX2, x1, x[0]);
        resultDF_DX1_DX2 = subst(resultDF_DX1_DX2, x2 , x[1]);
        resultDF_DX1_DX2 = CALC.SYM_EVAL(resultDF_DX1_DX2);
        double dx1dx2 = Double.parseDouble(resultDF_DX1_DX2.toString());

        resultDF_DX2_DX1 = subst(resultDF_DX2_DX1, x1, x[0]);
        resultDF_DX2_DX1 = subst(resultDF_DX2_DX1, x2 , x[1]);
        resultDF_DX2_DX1 = CALC.SYM_EVAL(resultDF_DX2_DX1);
        double dx2dx1 = Double.parseDouble(resultDF_DX2_DX1.toString());

        resultDF_DX2_DX2 = subst(resultDF_DX2_DX2, x1, x[0]);
        resultDF_DX2_DX2 = subst(resultDF_DX2_DX2, x2 , x[1]);
        resultDF_DX2_DX2 = CALC.SYM_EVAL(resultDF_DX2_DX2);
        double dx2dx2 = Double.parseDouble(resultDF_DX2_DX2.toString());

        det = dx1dx1*dx2dx2-dx1dx2*dx2dx1;
        return det;
    }

    static CalcObject subst(CalcObject input, String var, double number) {
        CalcSymbol symbol = new CalcSymbol(var);
        CalcDouble value = new CalcDouble(number);
        return CalcSUB.numericSubstitute(input, symbol, value);
    }
}
