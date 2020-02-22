import java.util.*;

/**
 * @author yibozhang
 * @create 2020/2/22
 * 逆波兰表达式求值
 */
public class RPN {

    final static Set<Character> OPERATOR_TABLE = new HashSet<>();

    static {
        OPERATOR_TABLE.add('+');
        OPERATOR_TABLE.add('-');
        OPERATOR_TABLE.add('*');
        OPERATOR_TABLE.add('/');
        OPERATOR_TABLE.add('(');
        OPERATOR_TABLE.add(')');
    }

    public int calc(String express) {
        String rpnExpress = buildRPNExpress(express);
        Stack<Integer> calcStack = new Stack<>();
        for (char c : rpnExpress.toCharArray()) {
            if (isDigit(c)) calcStack.push(c - '0');
            else if (isOperator(c)) {
                int b = calcStack.pop();
                int a = calcStack.pop();
                calcStack.push(calc(a, b, c));
            } else {
                throw new RuntimeException("invalid express.");
            }
        }

        return calcStack.pop();
    }

    public int calc(int a, int b, char op) {
        if (op == '+') {
            return a + b;
        } else if (op == '-') {
            return a - b;
        } else if (op == '*') {
            return a * b;
        } else if (op == '/') {
            return a / b;
        } else {
            throw new RuntimeException("invalid operator.");
        }
    }

    public String buildRPNExpress(String express) {
        StringBuilder rpnExpress = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();
        for (char c : express.toCharArray()) {
            if (c == ' ') continue;
            if (isDigit(c)) {
                rpnExpress.append(c);
            } else if (isOperator(c)) {
                if (c == '(') {
                    operatorStack.push(c);
                } else if (c == ')') {
                    while (operatorStack.peek() != '(') {
                        rpnExpress.append(operatorStack.pop());
                    }
                    //弹出(
                    operatorStack.pop();
                } else {
                    //其他操作符
                    while (!operatorStack.isEmpty() &&
                            getPriority(operatorStack.peek()) > getPriority(c)) {
                        //栈顶优先级比待操作的表达式的操作符优先级高 加入rpn表达式中
                        rpnExpress.append(operatorStack.pop());
                    }
                    operatorStack.push(c);
                }
            }
        }

        while (!operatorStack.isEmpty()) {
            rpnExpress.append(operatorStack.pop());
        }

        return rpnExpress.toString();
    }

    public static int getPriority(Character op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    private boolean isDigit(char c) {
        return c - '0' >= 0 && c - '0' <= '9';
    }

    private boolean isOperator(char c) {
        return OPERATOR_TABLE.contains(c);
    }

    public static void main(String[] args) {
        int r1 = new RPN().calc("5 + ( 6 - 4 / 2 ) * 3");
        System.out.println(r1);
    }

}
