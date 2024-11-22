package h1
import scala.language.implicitConversions

// Add necessary class and object definitions in order to make the statements in the main work.

class Complex(val r: Int, val i: Int):

	def this(r: Int) = this(r, 0)

	def +(that: Complex) = Complex(r + that.r, i + that.i)
	def *(that: Complex) = Complex(r*that.r - i*that.i, r*that.i + i*that.r)
	def unary_- = new Complex(-r, -i)

	override def toString = s"$r${ if i < 0 then "-" else "+" }${Math.abs(i)}i"

object Complex:
	given Conversion[Int, Complex] = r => new Complex(r)

val I = Complex(0,1)

object ComplexNumbers:
	def main(args: Array[String]): Unit =

		println(Complex(1,2)) // 1+2i

		println(1 + 2*I + I*3 + 2) // 3+5i

		val c = (2+3*I + 1 + 4*I) * I
		println(-c) // 7-3i
		