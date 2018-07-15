package com.simplemobiletools.calculator.operation

import android.content.SharedPreferences
import com.simplemobiletools.calculator.operation.base.Operation
import com.simplemobiletools.calculator.operation.base.TernaryOperation
import com.simplemobiletools.calculator.operation.base.UnaryOperation

class PassOperation(baseValue: Double,secondValue: Double, thirdValue: Double) : TernaryOperation(baseValue,secondValue,thirdValue), Operation {

    override fun getResult() : Double {
        if (baseValue < 1000) {
            return -1.0
        }
        return baseValue * secondValue + thirdValue
    }
}
