package e19

class PosInt(value: Int):
  require(value > 0)

  // exports all methods that are defined in the exported object/class
  // je to jakoby inheritance, všechny metody z exportovaného objektu jsou dostupné a přesměrují se na ten objekt
  export value.*
  // This makes the compiler generate methods like:
  // def +(x: Int): Int = value + x

  // One can also do some renaming or omit certain methods from the wildcard: export value.{<< as shl, >> as shr, >>> as _, *}


object Composition:
  def main(args: Array[String]): Unit =
    val x = PosInt(42)
    println(x + 1)

