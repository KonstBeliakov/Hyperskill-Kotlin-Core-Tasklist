package tasklist

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

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

data class Task(
    var lines: MutableList<String>,
    var priority: Priority,
    var date: LocalDate,
    var time: LocalTime
)


fun stringToDate(date: String): LocalDate? {
    return try {
        val (year, month, day) = date.split("-").map { it.toInt() }
        LocalDate.of(year, month, day)
    } catch (e: Exception) {
        null
    }
}

fun stringToTime(time: String): LocalTime? {
    return try {
        val (hour, minute) = time.split(":").map { it.toInt() }
        LocalTime.of(hour, minute, 0, 0)
    } catch (e: Exception) {
        null
    }
}

fun readPriority(): Priority {
    println("Input the task priority (C, H, N, L):")
    return Priority.fromString(readln().uppercase())!!
}

fun readDate(): LocalDate {
    do {
        println("Input the date (yyyy-mm-dd):")
        stringToDate(readln())?.let { return it }
        println("The input date is invalid")
    } while (true)
}

fun readTime(): LocalTime {
    do {
        println("Input the time (hh:mm):")
        stringToTime(readln())?.let { return it }
        println("The input time is invalid")
    } while (true)
}

fun readLines(): MutableList<String> {
    println("Input a new task (enter a blank line to end):")
    val lines: MutableList<String> = mutableListOf()
    do {
        val line = readln()
        if (line.isEmpty()) break
        lines.add(line)
    } while (true)
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
    val numberStringSize = 4
    val dateStringSize = 12
    val timeStringSize = 7
    val taskStringSize = 44

    val borderLine = "+${"-".repeat(numberStringSize)}+" +
            "${"-".repeat(dateStringSize)}+" +
            "${"-".repeat(timeStringSize)}+" +
            "---+---+${"-".repeat(taskStringSize)}+"

    println(borderLine)
    println("| N  |    Date    | Time  | P | D |                   Task                     |")
    println(borderLine)

    tasks.forEachIndexed { index, task ->
        val priorityChar = when(task.priority) {
            Priority.CRITICAL -> "\u001B[101m \u001B[0m"
            Priority.HIGH -> "\u001B[103m \u001B[0m"
            Priority.NORMAL -> "\u001B[102m \u001B[0m"
            Priority.LOW -> "\u001B[104m \u001B[0m"
        }

        val daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), task.date)

        val dChar = when{
            daysUntil < 0 ->  "\u001B[101m \u001B[0m"
            daysUntil == 0L -> "\u001B[103m \u001B[0m"
            else -> "\u001B[102m \u001B[0m"
        }

        val taskLines: MutableList<String> = mutableListOf()
        for (line in task.lines) {
            line.chunked(taskStringSize).forEach { l ->
                taskLines.add(l.padEnd(taskStringSize, ' '))
            }
        }

        val n = (index + 1).toString().padEnd(3, ' ')

        println("| ${n}| ${task.date} | ${task.time} | $priorityChar | $dChar |${taskLines.first()}|")

        taskLines.drop(0).forEach { line ->
            println(
                "|${" ".repeat(numberStringSize)}" +
                        "|${" ".repeat(dateStringSize)}" +
                        "|${" ".repeat(timeStringSize)}|   |   |$line|"
            )
        }

        println(borderLine)
    }
}

fun chooseTask(tasks: MutableList<Task>): Int? {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
        return null
    }
    printTasksInfo(tasks)
    println("Input the task number (1-${tasks.size}):")
    var taskNumber = readln().toIntOrNull()
    while (taskNumber == null || taskNumber !in 1..tasks.size) {
        println("Invalid task number")
        taskNumber = readln().toIntOrNull()
    }
    return taskNumber - 1
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
    val taskIndex = chooseTask(tasks)
    taskIndex?.let {
        tasks.removeAt(taskIndex - 1)
        println("The task is deleted")
    }
}

fun main() {
    val tasks: MutableList<Task> = mutableListOf()

    var end = false

    while (!end) {
        println("Input an action (add, print, edit, delete, end):")
        val command = readln().lowercase()
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


