package my.edu.tarc.contact

import android.util.Patterns

class Helper {
    fun isValidEmail(input: CharSequence): Boolean {
        return !input.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    fun isValidPhone(input: CharSequence): Boolean {
        return !input.isNullOrEmpty() && Patterns.PHONE.matcher(input).matches()
    }
}