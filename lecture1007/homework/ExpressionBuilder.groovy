// 2024/2025
// TASK The MarkupBuilder in Groovy can transform a hierarchy of method calls and nested closures into a valid XML document.
// Create a NumericExpressionBuilder builder that will read a user-specified hierarchy of simple math expressions and build a tree representation of it.
// The basic arithmetics operations as well as the power (aka '^') operation must be supported.
// It will feature a toString() method that will pretty-print the expression tree into a string with the same semantics, as verified by the assert on the last line.
// This means that parentheses must be placed where necessary with respect to the mathematical operator priorities.
// Change or add to the code in the script. Reuse the infrastructure code at the bottom of the script.
class NumericExpressionBuilder extends BuilderSupport {

    private Item root

    public Item rootItem() {
        return root
    }

    protected void setParent(Object parent, Object child) {
        parent.children << child
    }

    protected Object createNode(Object nodeName) {
        createNode nodeName, null, null
    }

    protected Object createNode(Object nodeName, Object value) {
        createNode nodeName, null, value
    }

    protected Object createNode(Object nodeName, Map attrs) {
        createNode nodeName, attrs, null
    }

    protected Object createNode(Object nodeName, Map attrs, Object value) {
        final node = new Item(nodeName)
        if (value) node.value = value
        if (attrs && attrs.containsKey('value')) {
            node.value = attrs.get('value')
        }
        if (!root) {
            root = node
        }
        return node
    }
}

class Item {
    final String name
    final List children = []
    String value

    public Item(String name) {
        this.name = name
        if (name == 'power') {
            this.name = '^'
        }
    }

    public priority() {
        switch (name) {
            case '+': return 1
            case '-': return 1
            case '*': return 2
            case '/': return 2
            case '^': return 3
            case 'number': return 4
            case 'variable': return 4
            default: throw new IllegalArgumentException('Unknown name: ' + name)
        }
    }

    @Override
    public String toString() {
        if (children.size() == 0) {
            return value
        }
        def result = new StringBuilder()
        for (int i = 0; i < children.size(); i++) {
            def child = children[i]
            if (i > 0) {
                result.append(" $name ")
            }
            if (child.priority() < this.priority()) {
                result.append('(')
            }
            result.append(child.toString())
            if (child.priority() < this.priority()) {
                result.append(')')
            }
        }
        return result.toString()
    }
}

//------------------------- Do not modify beyond this point!

def build(builder, String specification) {
    def binding = new Binding()
    binding['builder'] = builder
    new GroovyShell(binding).evaluate(specification)
}

//Custom expression to display. It should be eventually pretty-printed as 10 + x * (2 - 3) / 8 ^ (9 - 5)
String description = '''
builder.'+' {
    number(value: 10)
    '*' {
        variable(value: 'x')
        '/' {
            '-' {
                number(value: 2)
                number(value: 3)
            }
            power {
                number(value: 8)
                '-' {
                    number(value: 9)
                    number(value: 5)
                }
            }
        }
    }
}
'''

//XML builder building an XML document
build(new groovy.xml.MarkupBuilder(), description)

//NumericExpressionBuilder building a hierarchy of Items to represent the expression
def expressionBuilder = new NumericExpressionBuilder()
build(expressionBuilder, description)
def expression = expressionBuilder.rootItem()
println (expression.toString())
assert '10 + x * (2 - 3) / 8 ^ (9 - 5)' == expression.toString()