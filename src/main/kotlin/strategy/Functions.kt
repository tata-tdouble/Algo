package org.example.strategy


class Functions {

    fun c_all(arrayDeque: ArrayDeque<Double>) : Double {
        return (arrayDeque[0] - arrayDeque[2])
    }

    fun c_first_two(arrayDeque: ArrayDeque<Double>) : Double {
        return (arrayDeque[0] - arrayDeque[1])
    }

    fun c_last_two(arrayDeque: ArrayDeque<Double>) : Double {
        return (arrayDeque[1] - arrayDeque[2])
    }


    fun c_tan_inverse(value: Double) : Double {
        val angleInRadians = Math.atan(value)
        return Math.toDegrees(angleInRadians)
    }











}