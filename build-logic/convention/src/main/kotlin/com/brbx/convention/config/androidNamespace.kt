package com.brbx.convention.config

import com.brbx.convention.constants.BrbxMvi
import org.gradle.api.Project

internal val Project.androidNamespace: String
    get() {
        val modulePath = path
            .split(":")
            .filter { it.isNotEmpty() }
            .joinToString(separator = ".") { it.replace(oldValue = "-", newValue = "_") }

        return if (modulePath.isNotEmpty()) {
            "${BrbxMvi.Domain}.$modulePath"
        } else {
            BrbxMvi.Domain
        }
    }