package org.example.util

fun List<Double>.calculateMode(): Double? {
    if (this.isEmpty()) {
        return null // No mode for an empty list
    }

    val groupedByValue = this.groupingBy { it }.eachCount() // Group by value and count occurrences
    val maxCount = groupedByValue.values.maxOrNull() ?: return null  // Find the maximum count
    return groupedByValue.filter { it.value == maxCount }.keys.firstOrNull()  // Return the first key with max count (mode)
}

fun List<Int>.calculateMode(): Int? {
    if (this.isEmpty()) {
        return null // No mode for an empty list
    }

    val groupedByValue = this.groupingBy { it }.eachCount() // Group by value and count occurrences
    val maxCount = groupedByValue.values.maxOrNull() ?: return null  // Find the maximum count
    return groupedByValue.filter { it.value == maxCount }.keys.firstOrNull()  // Return the first key with max count (mode)
}


fun List<Double>.calculateSecondMode(): Double? {
    if (this.isEmpty()) {
        return null // No mode for an empty list
    }

    val groupedByValue = this.groupingBy { it }.eachCount().toList().sortedByDescending { it.second } // Group, count, sort descending

    // Find first and second most frequent values (might be equal)
    val firstModeCount = groupedByValue.firstOrNull()?.second ?: return null
    val secondModeCount = groupedByValue.getOrNull(1)?.second ?: return null

    // Second mode exists only if the counts are different
    return if (firstModeCount != secondModeCount) {
        groupedByValue.first { it.second == secondModeCount }.first // Return first value with second mode count
    } else {
        null // No second mode if counts are the same (or single mode)
    }
}


fun List<Int>.calculateSecondMode(): Int? {
    if (this.isEmpty()) {
        return null // No mode for an empty list
    }

    val groupedByValue = this.groupingBy { it }.eachCount().toList().sortedByDescending { it.second } // Group, count, sort descending

    // Find first and second most frequent values (might be equal)
    val firstModeCount = groupedByValue.firstOrNull()?.second ?: return null
    val secondModeCount = groupedByValue.getOrNull(1)?.second ?: return null

    // Second mode exists only if the counts are different
    return if (firstModeCount != secondModeCount) {
        groupedByValue.first { it.second == secondModeCount }.first // Return first value with second mode count
    } else {
        null // No second mode if counts are the same (or single mode)
    }
}
