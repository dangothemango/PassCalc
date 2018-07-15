package com.simplemobiletools.calculator.helpers

import android.content.Context
import android.util.Log
import com.simplemobiletools.calculator.R
import com.simplemobiletools.calculator.operation.OperationFactory
import com.simplemobiletools.calculator.operation.base.Operation

class CalculatorImpl(calculator: Calculator, private val context: Context, private val pass3: Int, private val pass4: Int) {
    var displayedNumber: String? = null
    var displayedFormula: String? = null
    var lastKey: String? = null
    private var mLastOperation: String? = null
    private var mCallback: Calculator? = calculator

    private var mIsFirstOperation = false
    private var mResetValue = false
    private var mBaseValue = 0.0
    private var mSecondValue = 0.0

    init {
        resetValues()
        setValue("0")
        setFormula("")
    }

    private fun resetValueIfNeeded() {
        if (mResetValue)
            displayedNumber = "0"

        mResetValue = false
    }

    private fun resetValues() {
        mBaseValue = 0.0
        mSecondValue = 0.0
        mResetValue = false
        mLastOperation = ""
        displayedNumber = ""
        displayedFormula = ""
        mIsFirstOperation = true
        lastKey = ""
    }

    fun setValue(value: String) {
        mCallback!!.setValue(value, context)
        displayedNumber = value
    }

    private fun setFormula(value: String) {
        mCallback!!.setFormula(value, context)
        displayedFormula = value
    }

    private fun updateFormula() {
        val first = Formatter.doubleToString(mBaseValue)
        val second = Formatter.doubleToString(mSecondValue)
        val sign = getSign(mLastOperation)

        if (sign == "√") {
            setFormula(sign + first)
        } else if (sign == "P"){
            setFormula( "PASS($first)")
        }
        else if (!sign.isEmpty()) {
            setFormula(first + sign + second)
        }
    }

    fun addDigit(number: Int) {
        val currentValue = displayedNumber
        val newValue = formatString(currentValue!! + number)
        setValue(newValue)
    }

    private fun formatString(str: String): String {
        // if the number contains a decimal, do not try removing the leading zero anymore, nor add group separator
        // it would prevent writing values like 1.02
        if (str.contains(".")) {
            return str
        }

        val doubleValue = Formatter.stringToDouble(str)
        return Formatter.doubleToString(doubleValue)
    }

    private fun updateResult(value: Double) {
        setValue(Formatter.doubleToString(value))
        mBaseValue = value
    }

    private fun getDisplayedNumberAsDouble() = Formatter.stringToDouble(displayedNumber!!)

    fun handleResult() {
        mSecondValue = getDisplayedNumberAsDouble()
        calculateResult()
        mBaseValue = getDisplayedNumberAsDouble()
    }

    private fun handleRootPass() {
        mBaseValue = getDisplayedNumberAsDouble()
        calculateResult()
    }

    private fun calculateResult() {
        updateFormula()

        Log.d("TAG", PASS)
        Log.d("TAG", pass3.toDouble().toString())
        Log.d("TAG", pass4.toDouble().toString())
        Log.d("TAG", mBaseValue.toString())

        val operation = when (mLastOperation) {
                            PASS -> OperationFactory.forId(PASS, mBaseValue, pass3.toDouble(), pass4.toDouble())
                            else -> OperationFactory.forId(mLastOperation!!, mBaseValue, mSecondValue)
                        }
        updateResult(operation?.getResult()!!)

        mIsFirstOperation = false
    }

    fun handleOperation(operation: String) {
        if (lastKey == DIGIT && operation != ROOT && operation != PASS) {
            handleResult()
        }

        mResetValue = true
        lastKey = operation
        mLastOperation = operation

        if (operation == ROOT || operation == PASS) {
            handleRootPass()
            mResetValue = false
        }
    }

    fun handleClear() {
        handleReset()
    }

    fun handleReset() {
        resetValues()
        setValue("0")
        setFormula("")
    }

    fun handleEquals() {
        if (lastKey == EQUALS)
            calculateResult()

        if (lastKey != DIGIT)
            return

        mSecondValue = getDisplayedNumberAsDouble()
        calculateResult()
        lastKey = EQUALS
    }

    private fun decimalClicked() {
        var value = displayedNumber
        if (!value!!.contains(".")) {
            value += "."
        }
        setValue(value)
    }

    private fun zeroClicked() {
        val value = displayedNumber
        if (value != "0") {
            addDigit(0)
        }
    }

    private fun getSign(lastOperation: String?) = when (lastOperation) {
        PLUS -> "+"
        MINUS -> "-"
        MULTIPLY -> "*"
        DIVIDE -> "/"
        POWER -> "^"
        ROOT -> "√"
        PASS -> "P"
        else -> ""
    }

    fun numpadClicked(id: Int) {
        if (lastKey == EQUALS) {
            mLastOperation = EQUALS
        }

        lastKey = DIGIT
        resetValueIfNeeded()

        when (id) {
            R.id.btn_decimal -> decimalClicked()
            R.id.btn_0 -> zeroClicked()
            R.id.btn_1 -> addDigit(1)
            R.id.btn_2 -> addDigit(2)
            R.id.btn_3 -> addDigit(3)
            R.id.btn_4 -> addDigit(4)
            R.id.btn_5 -> addDigit(5)
            R.id.btn_6 -> addDigit(6)
            R.id.btn_7 -> addDigit(7)
            R.id.btn_8 -> addDigit(8)
            R.id.btn_9 -> addDigit(9)
        }
    }
}
