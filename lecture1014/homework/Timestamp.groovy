// 2024/2025
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import org.codehaus.groovy.ast.stmt.TryCatchStatement
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ClassHelper
import static org.codehaus.groovy.ast.tools.GeneralUtils.*
import org.codehaus.groovy.ast.VariableScope
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.expr.ConstantExpression

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass("CreatedAtTransformation")
public @interface CreatedAt {
    String name() default "";
}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class CreatedAtTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {

        //...
        // TASK Ensure the annotated class has a private long field holding the time of instantiation of the object.
        // Also, generate a public final method returning the value stored in the field. The name of the method should be configurable through 
        // the annotation 'name' parameter.
        // Additionally, all existing methods of the class should be enhanced so that they reset the time stored in the field to the current time,
        // whenever they are called, but ONLY if more than 1 second has elapsed since the latest update to the time stored in the field.
        // A new method, named "clearTimestamp()" must be added to the class. This method sets the time stored in the field to "0".
                
        // Fill in the missing AST generation code to make the script pass
        // You can take inspiration from exercises
        // Documentation and hints:
        // http://docs.groovy-lang.org/docs/next/html/documentation/
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/expr/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/stmt/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/tools/package-summary.html        
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/tools/GeneralUtils.html
        
        // Use ClassHelper.long_TYPE to specify a long type.
        // buildFromString() returns an array, which holds a BlockStatement for the passed-in code as its first element.
        // ClassNode.addField() accepts an expression, which can be obtained from a BlockStatement as blockStatement.statements.expression
        // ClassNode.addMethod() accepts a BlockStatement
        
        //TODO Implement this method

        ClassNode classNode = (ClassNode) astNodes[1]
        AnnotationNode annotationNode = (AnnotationNode) astNodes[0]
        AstBuilder ab = new AstBuilder()
        
        // add the private long field
        classNode.addField("timestamp", Opcodes.ACC_PRIVATE, ClassHelper.long_TYPE, new ConstantExpression(0L))

        // add the getter method
        String getterName = annotationNode.getMember("name")?.getText() ?: "getTimestamp"
        List<ASTNode> res = ab.buildFromCode(SEMANTIC_ANALYSIS, true) {
            return timestamp
        }
        MethodNode getterMethod = new MethodNode(
            getterName,
            Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
            ClassHelper.long_TYPE,
            Parameter.EMPTY_ARRAY,
            ClassNode.EMPTY_ARRAY,
            new BlockStatement(
                [res[0]] as org.codehaus.groovy.ast.stmt.Statement[],
                new VariableScope()
            )
        )
        classNode.addMethod(getterMethod)

        // add the clearTimestamp method
        res = ab.buildFromCode(SEMANTIC_ANALYSIS, true) {
            timestamp = 0
        }
        MethodNode clearTimestampMethod = new MethodNode(
            "clearTimestamp",
            Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
            ClassHelper.VOID_TYPE,
            Parameter.EMPTY_ARRAY,
            ClassNode.EMPTY_ARRAY,
            new BlockStatement(
                [res[0]] as org.codehaus.groovy.ast.stmt.Statement[],
                new VariableScope()
            )
        )
        classNode.addMethod(clearTimestampMethod)

        // add the reset on call to all existing methods, after one second has passed
        res = ab.buildFromCode(SEMANTIC_ANALYSIS, true) {
            def currentTime = System.currentTimeMillis()
            def timeDiff = currentTime - timestamp
            if (timeDiff > 1000) {
                timestamp = currentTime
            }
        }
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("clearTimestamp") || method.name.equals(getterName)) {
                continue
            }
            method.setCode(new BlockStatement(
                [res[0], method.code] as org.codehaus.groovy.ast.stmt.Statement[],
                new VariableScope()
            ))
        }
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
@CreatedAt(name = "timestamp")
class Calculator {
    int sum = 0
    
    def add(int value) {
        int v = sum + value
        sum = v
    }

    def subtract(int value) {
        sum -= value
    }
}

new Calculator()
''')

assert System.currentTimeMillis() >= calculator.timestamp()
assert calculator.timestamp() == calculator.timestamp()
def oldTimeStamp = calculator.timestamp()

sleep(1500)
calculator.add(10)
assert calculator.sum == 10

assert oldTimeStamp < calculator.timestamp()
//The timestamp should have been updated since the pause was longer than 1s
assert calculator.timestamp() == calculator.timestamp()
oldTimeStamp = calculator.timestamp()

sleep(1500)
calculator.subtract(1)
assert calculator.sum == 9
//The timestamp should have been updated since the pause was longer than 1s
assert oldTimeStamp < calculator.timestamp()
assert calculator.timestamp() == calculator.timestamp()

oldTimeStamp = calculator.timestamp()
sleep(100)
calculator.subtract(1)
assert calculator.sum == 8
//The timestamp should not have been updated since the pause was shorter than 1s
assert oldTimeStamp == calculator.timestamp()
assert calculator.timestamp() == calculator.timestamp()

calculator.clearTimestamp()
assert calculator.timestamp() == 0

println 'well done'