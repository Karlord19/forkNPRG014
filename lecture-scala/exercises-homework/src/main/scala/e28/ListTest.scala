package e28

import java.io.File

/* Features
 * - covariance
 */

abstract class MyList[+T]:
  def head: T
  def tail: MyList[T]

class MyListItem[T](val head: T, val tail: MyList[T]) extends MyList[T]

object MyNil extends MyList[Nothing]:
  def head = throw Exception()
  def tail = throw Exception()


object ListTest:
  def main(args: Array[String]): Unit =
    val list = MyListItem("aa", MyListItem(1, MyNil))

// pro A <: B
// covariance je pro X[A] <: X[B]
// contra variance je pro X[A] >: X[B]
// invariance je pro neznamou relaci mezi X[A] a X[B]
// MyList[+A] je pro covariances

// kdyz mame kontejner a je immutable,
// tak vetsinou to bude covariantni

// btw
// kdyz assignujeme objekt x do promenne y, kde y ma klidne jiny type nex x,
// tak se stejne ulozi jen reference a y nam dovoli divat se na x jinyma ocima
// zmeny v x se projevi i v y i po assignu
