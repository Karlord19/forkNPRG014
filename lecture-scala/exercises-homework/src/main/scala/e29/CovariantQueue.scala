package e29

/* Features:
 * - covariance with internal state
 */

object CovariantQueue:
	def apply() = new CovariantQueue(Nil, Nil)

// je to immutable zvenku, mutable zevnitr
// bacha - private znamena ze to je viditelne ze vsech instanci stejne tridy
// ale zachranil to tim ze to je private[this] - takze to je viditelne jen z te jedne instance
class CovariantQueue[+T] private (
		private[this] var leading: List[T],
		private[this] var trailing: List[T] // je to reversed list jakoby logicky
	):

	private def mirror(): Unit =
		// The compiler checks that all accesses to the mutable state are within a single instance only
		if leading.isEmpty then
			leading = trailing.reverse
			trailing = Nil

	def head: T =
		mirror()
		leading.head
	
	def tail: CovariantQueue[T] =
		mirror()
		new CovariantQueue(leading.tail, trailing)
	
	def enqueue[U >: T](x: U) = new CovariantQueue[U](leading, x :: trailing)

