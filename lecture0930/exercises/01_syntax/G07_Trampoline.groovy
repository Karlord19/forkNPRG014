def factorial
factorial = { int n, def accu = 1G -> // accu je accumulator
    if (n < 2) return accu
    factorial.trampoline(n - 1, n * accu)
}
factorial = factorial.trampoline()

// trampoline zabranuje stack overflow na rekurzivnich funkcich
// udela jeden krok a vrati novou closure, kterou zavola
// na konci uz nevrati closure ale jen hodnotu a tam "zanorovani" skonci

assert factorial(1)    == 1
assert factorial(3)    == 1 * 2 * 3
assert factorial(1000) // == 402387260.. plus another 2560 digits
println factorial(1000)
println 'Done'