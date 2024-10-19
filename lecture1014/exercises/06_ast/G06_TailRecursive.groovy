@groovy.transform.TailRecursive
def fact(int number, BigInteger acc = 1) {
    number < 2 ? acc : fact(number - 1, number * acc)
}

println fact(5)

//TASK Make the function tail recursive so that it can pass the following line
println fact(10000)