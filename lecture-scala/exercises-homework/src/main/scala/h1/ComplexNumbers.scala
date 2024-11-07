package h1
import scala.language.implicitConversions

// Add necessary class and object definitions in order to make the statements in the main work.

class Complex(val r: Int, val i: Int):
	def +(other: Complex): Complex = Complex(r + other.r, i + other.i)
	def *(other: Complex): Complex = Complex(r*other.r - i*other.i, r*other.i + i*other.r)
	def unary_- : Complex = Complex(-r, -i)
	override def toString: String = s"$r" + { if i < 0 then "-" else "+" } + s"${Math.abs(i)}i"
	def +(other: Int): Complex = Complex(r + other, i)
	def *(other: Int): Complex = Complex(r*other, i*other)

val I:Complex = Complex(0,1)

implicit def intToComplex(value: Int): Complex = Complex(value, 0)

object ComplexNumbers:
	def main(args: Array[String]): Unit =

		println(Complex(1,2)) // 1+2i

		println(1 + 2*I + I*3 + 2) // 3+5i

		val c = (2+3*I + 1 + 4*I) * I
		println(-c) // 7-3i
		