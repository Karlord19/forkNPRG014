/*
Implement the classes below such that the main (without modifications) prints out the something like this:

Person John Doe aged 24
Person John Doe aged 25
List(h2.PersonState@3d24753a)
Person John Doe aged 24
Thing Box with color (255,0,0)
Person Joe aged 24

*/

package h2

import scala.collection.mutable.ListBuffer

trait WithExplicitState:

  // State_type always knows the type of the state used inside this trait
  protected type State_type <: Any

  protected var m_state: State_type = _

  protected def state: State_type = m_state
  protected def state_=(m_state: State_type): Unit = this.m_state = m_state


class PersonState(val name: String, val age: Int)

class Person extends WithExplicitState:

  type State_type = PersonState
  
  m_state = new State_type("", 0)

  def setName(name: String): this.type =
    m_state = new State_type(name, m_state.asInstanceOf[State_type].age)
    this
  
  def setAge(age: Int): this.type =
    m_state = new State_type(m_state.asInstanceOf[State_type].name, age)
    this

  override def toString: String =
    s"Person ${m_state.asInstanceOf[State_type].name} aged ${m_state.asInstanceOf[State_type].age}"


type RGBColor = (Int, Int, Int)
class ThingState(val name: String, val color: RGBColor)

class Thing extends WithExplicitState:

  type State_type = ThingState
  
  m_state = new State_type("", (0, 0, 0))

  def setName(name: String): this.type =
    m_state = new State_type(name, m_state.asInstanceOf[State_type].color)
    this
  
  def setColor(color: RGBColor): this.type =
    m_state = new State_type(m_state.asInstanceOf[State_type].name, color)
    this

  override def toString: String =
    s"Thing ${m_state.asInstanceOf[State_type].name} with color ${m_state.asInstanceOf[State_type].color}"


trait History extends WithExplicitState:

    val hist = ListBuffer.empty[ State_type ]

    def checkpoint(): this.type =
      hist.append(m_state)
      this

    def history = hist.toList

    def restoreTo(s: State_type): this.type =
      m_state = s
      this


object ExplicitStateTest:
  def main(args: Array[String]): Unit =
    // The inferred type of variable "john" should be "Person & History".
    val john = (new Person with History).setName("John Doe").setAge(24).checkpoint()

    println(john)
    john.setAge(25)

    println(john)
    println(john.history)

    val johnsPrevState = john.history(0)
    john.restoreTo(johnsPrevState)
    println(john)

    // The inferred type of variable "box" should be "Thing & History".
    val box = new Thing with History
    box.setName("Box")
    box.setColor((255, 0, 0))
    println(box)

    val joe = new Person with History
    joe.restoreTo(johnsPrevState).setName("Joe")
    println(joe)

    // The line below must not compile. It should complain about an incompatible type.
    // box.restoreTo(johnsPrevState)
