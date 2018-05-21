package test;

import java.util.Scanner;
import javacalculus.core.CALC;
import javacalculus.core.CalcParser;
import javacalculus.evaluator.CalcSUB;
import javacalculus.struct.CalcDouble;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;

public class ParserTest {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Enter expression:");
            String expression = in.nextLine();
            // javacalculus uses uppercase function names
            expression = expression.replace("sin", "SIN");
            expression = expression.replace("cos", "COS");
            expression = expression.replace("ln", "LN");

            System.out.println("Differentiate with respect to:");
            String variable = in.nextLine();

            // differentiate
            String command = "DIFF(" + expression + ", " + variable + ")";
            CalcParser parser = new CalcParser();
            CalcObject parsed = parser.parse(command);

            CalcObject result = parsed.evaluate();

            // compute numerical value
            //	result = subst(result, "a", 0.0);
            //	result = subst(result, "b", 10.0);
            result = CALC.SYM_EVAL(result);

            System.out.println("Result:");
            System.out.println(result);
        }
    }

    static CalcObject subst(CalcObject input, String var, double number) {
        CalcSymbol symbol = new CalcSymbol(var);
        CalcDouble value = new CalcDouble(number);
        return CalcSUB.numericSubstitute(input, symbol, value);
    }
}