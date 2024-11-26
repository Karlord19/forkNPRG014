package e31

import reflect.Selectable.reflectiveSelectable

// duck typing (structural typing)
// if it walks like a duck and quacks like a duck, it must be a duck
// takze od printera v Loggeru ocekavame cokoli, co ma metodu println s danou signaturou
// jvm to nepodporuje, takze to jednou necha sebehnout, zjisti typ, a dle toho to zkompiluje

class Logger(printer : { def println(msg: String): Unit}):
	def log(msg: String): Unit =
		printer.println(msg)


object ConsoleLogger:
	def println(msg: String): Unit =
		Console.println(msg)


object StructuralSubtyping:
	
	def main(args: Array[String]): Unit =
		val logger = new Logger(ConsoleLogger)

		logger.log("Hello world!")
