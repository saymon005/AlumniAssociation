package com.dayencreation.alumni

import android.widget.EditText

class CommonFunctionContainer {
    public fun checkEmptyField(textFields: ArrayList<EditText>) : Boolean{
        val check : HashSet<Boolean> = HashSet()
        for (textField in textFields) {
            check.add(isFieldEmpty(textField))
        }
        return check.contains(true)
    }

    private fun isFieldEmpty(textField : EditText) : Boolean{
        return if(textField.text.isEmpty()){
            textField.error = "Empty Field"
            true
        } else false
    }
}