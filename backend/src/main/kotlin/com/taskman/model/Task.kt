package com.taskman.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Task(
    var id: Long? = null,
    var title: String,
    var description: String? = null,
    var completed: Boolean = false,
    var dueDate: String? = null
)