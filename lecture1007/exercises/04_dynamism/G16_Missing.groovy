hello ladies, gentlemen
hi guys









def methodMissing(String name, args) {
    println "$name ${args.join(' and ')}"
    println "${args.join(' and ')} say $name back"
    println ""
}

def propertyMissing(String name) { name }


// hello ladies, gentlemen MEANS hello(ladies, gentlemen)
// hi guys MEANS hi(guys)