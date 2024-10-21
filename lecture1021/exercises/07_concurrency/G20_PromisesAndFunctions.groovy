import groovyx.gpars.dataflow.Dataflow
import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup

// nejede to, ani ten typek nevi co se s tim deje

def elTest = task { checkElectricity() }

def checkEngine() {
    if (elTest.get() == true) {
        println "Checking the engine"
        sleep 3000
        return true
    }
    println "Engine check failed"
    return false
}

def checkTyres() {
    println "Preparing the tyres"
    sleep 4000
    return true
}

def checkElectricity() {
    println "Checking electricity"
    sleep 3000
    return System.currentTimeMillis() % 3 == 0 ? true : false
}

def checkRadar() {
    println "Turning radar on"
    sleep 1000
    if (elTest.get()) {
        println "Electricity ok, continue with the radar"
        sleep 500
        return System.currentTimeMillis() % 2 == 0 ? true : false
    } else {
        println "Electricity failed, do not continue with radar"    
        return false
    }
}

def engineCheck = task { checkEngine() }
def tyrePressure = task { checkTyres() }
def radarOn = task { checkRadar() }

engineCheck.then {println "Engine ok"}
tyrePressure.then {println "Tyres ok"}
radarOn.then {println "Radar ${it ? 'ok' : 'failed'}"}

boolean ready = [engineCheck, tyrePressure, radarOn].every {it.val}
if(ready) {
    println 'Taking off'
} else {
    println 'Staying on the ground today'
}
// TASK Currently, checkElectricity() is local to the checkRadar() function. Modify the code is a way
// that allows both checkRadar() and checkEngine() to first have the electricity checked and only after
// a successful result of electricity check the radar or engine can be turned on, respectively.
// Make sure that electricity is checked only once.