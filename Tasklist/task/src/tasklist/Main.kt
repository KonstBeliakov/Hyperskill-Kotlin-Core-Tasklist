package tasklist

import java.time.LocalDate
import java.time.LocalTime

enum class Priority(val shortName: String) {
    CRITICAL("C"),
    HIGH("H"),
    NORMAL("N"),
    LOW("L");

    companion object {
        fun fromString(value: String): Priority? {
            return enumValues<Priority>().find { it.shortName == value }
        }
    }
}

class Task(val lines: MutableList<String>, val priority: Priority, val date: LocalDate, val time: LocalTime)


fun stringToDate(date: String): LocalDate? {
    try {
        val (year, month, day) = date.split("-").map { it.toInt() }
        return LocalDate.of(year, month, day)
    } catch (e: Exception) {
        return null
    }

}

fun stringToTime(time: String): LocalTime? {
    try {
        val (hour, minute) = time.split(":").map { it.toInt() }
        return LocalTime.of(hour, minute, 0, 0)
    } catch (e: Exception) {
        return null
    }
}

fun addTask(tasks: MutableList<Task>) {
    println("Input the task priority (C, H, N, L):")
    val priority = Priority.fromString(readln().uppercase())

    println("Input the date (yyyy-mm-dd):")
    var dateString = readln()
    while (stringToDate(dateString) == null) {
        println("The input date is invalid")
        dateString = readln()
    }
    val date = stringToDate(dateString)

    println("Input the time (hh:mm):")

    var timeString = readln()
    while (stringToTime(timeString) == null) {
        println("The input time is invalid")
        timeString = readln()
    }
    val time = stringToTime(timeString)

    println("Input a new task (enter a blank line to end):")
    var line = readln()
    val lines: MutableList<String> = mutableListOf()
    while (line.isNotEmpty()) {
        lines.add(line.trim())
        line = readln()
    }

    if (lines.isEmpty())
        println("The task is blank")
    else
        tasks.add(Task(lines, priority!!, date!!, time!!))
}

fun printTasksInfo(tasks: MutableList<Task>) {
    val indent = tasks.size.toString().length + 1
    val indentPrefix = " ".repeat(indent)

    tasks.forEachIndexed { index, task ->
        print((index + 1).toString().padEnd(indent, ' '))
        println("${task.date} ${task.time} ${task.priority.shortName}")

        task.lines.forEach { line ->
            println("$indentPrefix$line")
        }
        println()
    }
}

fun main() {
    val tasks: MutableList<Task> = mutableListOf()

    var end = false

    while (!end) {
        println("Input an action (add, print, end):")
        val command = readln()
        when (command) {
            "add" -> addTask(tasks)

            "print" -> printTasksInfo(tasks)

            "end" -> {
                println("Tasklist exiting!")
                end = true
            }

            else -> println("Invalid command")
        }
    }
}


