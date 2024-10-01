final a = {
    final b = {
        final c = {magicNumber + 10}
        c()
    }
    b()
}

//TASK Set the delegates so that to code passes and uses the value below
final data = [magicNumber: 20]
a.delegate = data
// C nema magicNumber, zepta se hornijsi vrstvy, B taky nema,
// zepta se hornejsi, A uz ma - bylo mu pridano skrz nastaveni delegata
println a()
assert 30 == a()