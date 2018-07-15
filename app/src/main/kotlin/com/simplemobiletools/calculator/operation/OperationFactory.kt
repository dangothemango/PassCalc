package com.simplemobiletools.calculator.operation

import com.simplemobiletools.calculator.helpers.*
import com.simplemobiletools.calculator.operation.base.Operation

object OperationFactory {

    fun forId(id: String, baseValue: Double, secondValue: Double, thirdValue: Double = 0.0): Operation? {
        return when (id) {
            PLUS -> PlusOperation(baseValue, secondValue)
            MINUS -> MinusOperation(baseValue, secondValue)
            DIVIDE -> DivideOperation(baseValue, secondValue)
            MULTIPLY -> MultiplyOperation(baseValue, secondValue)
            POWER -> PowerOperation(baseValue, secondValue)
            ROOT -> RootOperation(baseValue)
            PASS -> PassOperation(baseValue,secondValue,thirdValue)
            else -> null
        }
    }
}
