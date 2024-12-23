import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass("UnsupportedTransformation")
public @interface Unsupported {}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class UnsupportedTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        MethodNode annotatedMethod = astNodes[1]
        String name = annotatedMethod.name
        def methodStatements = annotatedMethod.code.statements
        if (!methodStatements.empty) {
            addError("Unsupported operations must be empty", annotatedMethod, source)
        }
        AstBuilder ab = new AstBuilder()
        List<ASTNode> res = ab.buildFromString("""
                throw new UnsupportedOperationException('The ${name} operation is not supported')
            """)

        BlockStatement block = res[0]
        methodStatements.add(0, block)
        //...
        // TASK The "Unsupported" transformation should make the annotated methods throw the UnsupportedOperationException
        // Fill in the missing AST generation code to make the script pass
        // You can take inspiration from exercises

        //TASK Report an error when the method declared as @Unsupported has nonempty body - e.g. multiply() 
    }

    public void addError(String msg, ASTNode expr, SourceUnit source) {
        int line = expr.lineNumber
        int col = expr.columnNumber
        SyntaxException se = new SyntaxException(msg + '\n', line, col)
        SyntaxErrorMessage sem = new SyntaxErrorMessage(se, source)
        source.errorCollector.addErrorAndContinue(sem)
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
class Calculator {
    def plus(a, b) {
        a + b
    }

    @Unsupported
    def minus(a, b) {}

    @Unsupported
    def divide(a, b) {}

    //@Unsupported (unsupported operations must be empty)
    def multiply(a, b) {
        a * b
    }
}

new Calculator()
''')

assert 5 == calculator.plus(2, 3)
try {
    calculator.minus(10, 5)
    assert false, "Exception should have been thrown since the minus method is not supported"
} catch (RuntimeException e) {
    assert e instanceof UnsupportedOperationException
    assert e.message == 'The minus operation is not supported'
}
try {
    calculator.divide(10, 5)
    assert false, "Exception should have been thrown since the divide method is not supported"
} catch (RuntimeException e) {
    assert e instanceof UnsupportedOperationException
    assert e.message == 'The divide operation is not supported'
}

println 'well done'