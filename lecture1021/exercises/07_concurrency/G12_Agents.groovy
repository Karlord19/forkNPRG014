import groovyx.gpars.agent.Agent

def event = new Agent([])
Thread.start {
    event.send { it << 'Joe' }
    event.send { it << 'Dave' }
}

println "Instant peek: " + event.instantVal
// instantVal is a snapshot of the current state of the agent

//Notice syntax sugar here allowing you to omit "send"
Thread.start {
    event { it << 'Alice' }
    event { it << 'Susan' }
}

println "Another instant peek: " + event.instantVal
sleep 2000
event { it << 'Eve' }
println "Final state: " + event.val
// val is the state of the agent after all the events have been processed