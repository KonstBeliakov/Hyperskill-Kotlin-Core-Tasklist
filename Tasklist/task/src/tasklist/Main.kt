package tasklist

fun main() {
val tasks: MutableList<MutableList<String>> = mutableListOf()

    var end = false

    while (!end) {
        println("Input an action (add, print, end):")
        val command = readln()
        when (command) {
            "add" -> {
                println("Input a new task (enter a blank line to end):")
                var line = readln()
                val task: MutableList<String> = mutableListOf()
                while (line.isNotEmpty()) {
                    task.add(line.trim())
                    line = readln()
                }
                tasks.add(task)
            }

            "print" -> {
                val indent = tasks.size.toString().length + 1
                val indentPrefix = " ".repeat(indent)

                tasks.forEachIndexed{ index, task ->
                    print((index + 1).toString().padEnd(indent, ' '))
                    println(task.first())

                    task.drop(1).forEach{ line ->
                        println("$indentPrefix$line")
                    }
                    println()
                }
            }

            "end" -> {
                println("Tasklist exiting!")
                end = true
            }

            else -> println("Invalid command")
        }
    }
}


