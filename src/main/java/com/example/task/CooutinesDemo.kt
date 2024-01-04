package com.example.task
import kotlin.concurrent.thread

fun main() {
    println("My program runs...:${Thread.currentThread().name}")

    thread {
        longRunningTask()
    }

    println("My program run ends...:${Thread.currentThread().name}")
}

fun longRunningTask(){
    println("executing longRunningTask on...:${Thread.currentThread().name}")
    Thread.sleep(1000)
    println("longRunningTask ends on thread ...:${Thread.currentThread().name}")
}