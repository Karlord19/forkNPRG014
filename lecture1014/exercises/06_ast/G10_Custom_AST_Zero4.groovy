import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import groovyjarjarasm.asm.Opcodes
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS

import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.ast.VariableScope

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass("ZeroTransformation4")
public @interface Zero4 {}

//TASK Complete the transformation code at the indicated position so that the test passes

@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class ZeroTransformation4 implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        ClassNode annotatedClass = astNodes[1]

        ASTNode stmt = new ReturnStatement(new ConstantExpression(0))

        annotatedClass.addMethod(
            'getZero',
            Opcodes.ACC_PUBLIC,
            ClassHelper.Integer_TYPE,
            Parameter.EMPTY_ARRAY,
            ClassNode.EMPTY_ARRAY,
            new org.codehaus.groovy.ast.stmt.BlockStatement(
                [stmt] as org.codehaus.groovy.ast.stmt.Statement[],
                new VariableScope()
            )
        )
    }
}

final calculator = new GroovyShell(Zero4.class.getClassLoader()).evaluate('''
@Zero4
class Calculator {}

new Calculator()
''')

assert 0 == calculator.zero

println 'done'