package com.taskman

import io.micronaut.runtime.Micronaut

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .packages("com.taskman")
            .mainClass(Application.javaClass)
            .start()
    }
}