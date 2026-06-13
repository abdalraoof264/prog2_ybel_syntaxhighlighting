package highlighting.antlr;

import java.util.Scanner;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class PrettyPrinterDemo {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Leerzeichen pro Einruckung: ");
    int indentWidth = Integer.parseInt(scanner.nextLine());

    String[] examples = {
      "class Test{private int x;public int foo(){int a=x;return a;}}",
      "class Test{public int foo(){int a=x;if(a==x){return a;}else{return x;}}}",
      "class Test{public int foo(){int a=x;while(a==x){{a=x;}return a;}}}"
    };
    for (String code : examples) {
      System.out.println("INPUT:");
      System.out.println(code);

      System.out.println();
      System.out.println("OUTPUT:");

      System.out.println(prettyPrint(code, indentWidth));
      System.out.println("------------------------------");
    }
  }

  private static String prettyPrint(String code, int indentWidth) {
    MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(code));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MiniJavaParser parser = new MiniJavaParser(tokens);

    MiniJavaParser.CompilationUnitContext tree = parser.compilationUnit();

    PrettyPrinterVisitor visitor = new PrettyPrinterVisitor(indentWidth);
    visitor.visit(tree);

    return visitor.result();
  }
}
