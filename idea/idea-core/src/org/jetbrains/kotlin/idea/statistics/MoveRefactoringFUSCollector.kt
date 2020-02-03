/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.statistics

object MoveRefactoringFUSCollector {

    /**
     * @param isDefault is something changed in Move Refactoring Dialog check-boxes state
     */
    fun log(
        timeStarted: Long,
        timeFinished: Long,
        numberOfFiles: Int,
        destination: MoveRefactoringDestination,
        isDefault: Boolean
    ) {

        val data = mapOf(
            "lagging" to (timeFinished - timeStarted).toString(),
            "destination" to destination.toString(),
            "number_of_entities" to numberOfFiles.toString(),
            "are_settings_changed" to isDefault.toString()
        )

        KotlinFUSLogger.log(FUSEventGroups.Refactoring, "Move", data)
    }

    enum class MoveRefactoringDestination {
        PACKAGE, FILE, DIRECTORY
    }

    enum class MovedEntity {
        FUNCTION, CLASS, PACKAGE, MPPFUNCTION, MPPCLASS, MIXED
    }
}