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

class Task(var lines: MutableList<String>, var priority: Priority, var date: LocalDate, var time: LocalTime)


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

fun readPriority(): Priority {
    println("Input the task priority (C, H, N, L):")
    return Priority.fromString(readln().uppercase())!!
}

fun readDate(): LocalDate {
    println("Input the date (yyyy-mm-dd):")
    var dateString = readln()
    while (stringToDate(dateString) == null) {
        println("The input date is invalid")
        dateString = readln()
    }
    return stringToDate(dateString)!!
}

fun readTime(): LocalTime {
    println("Input the time (hh:mm):")

    var timeString = readln()
    while (stringToTime(timeString) == null) {
        println("The input time is invalid")
        timeString = readln()
    }
    return stringToTime(timeString)!!
}

fun readLines(): MutableList<String> {
    println("Input a new task (enter a blank line to end):")
    var line = readln()
    val lines: MutableList<String> = mutableListOf()
    while (line.isNotEmpty()) {
        lines.add(line.trim())
        line = readln()
    }

    return lines
}

fun addTask(tasks: MutableList<Task>) {
    val priority = readPriority()
    val date = readDate()
    val time = readTime()
    val lines = readLines()

    if (lines.isEmpty())
        println("The task is blank")
    else
        tasks.add(Task(lines, priority, date, time))
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

fun chooseTask(tasks: MutableList<Task>): Int? {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
        return null
    } else {
        printTasksInfo(tasks)
        println("Input the task number (1-${tasks.size}):")
        var taskNumber = readln().toIntOrNull()
        while (taskNumber == null || taskNumber !in 1..tasks.size) {
            println("Invalid task number")
            taskNumber = readln().toIntOrNull()
        }
        return taskNumber - 1
    }
}

fun editTask(tasks: MutableList<Task>) {
    val taskNumber = chooseTask(tasks)
    taskNumber?.let {
        println("Input a field to edit (priority, date, time, task):")
        var field = readln()
        while (field !in listOf("priority", "date", "time", "task")) {
            println("Invalid field")
            field = readln()
        }

        tasks[taskNumber].apply {
            when (field) {
                "priority" -> priority = readPriority()
                "date" -> date = readDate()
                "time" -> time = readTime()
                "task" -> lines = readLines()
            }
        }
        println("The task is changed")
    }
}

fun deleteTask(tasks: MutableList<Task>) {
    val taskNumber = chooseTask(tasks)
    if (taskNumber != null) {
        tasks.removeAt(taskNumber - 1)
        println("The task is deleted")
    }
}

fun main() {
    val tasks: MutableList<Task> = mutableListOf()

    var end = false

    while (!end) {
        println("Input an action (add, print, edit, delete, end):")
        val command = readln()
        when (command) {
            "add" -> addTask(tasks)

            "print" -> printTasksInfo(tasks)

            "edit" -> editTask(tasks)

            "delete" -> deleteTask(tasks)

            "end" -> {
                println("Tasklist exiting!")
                end = true
            }

            else -> println("Invalid command")
        }
    }
}


