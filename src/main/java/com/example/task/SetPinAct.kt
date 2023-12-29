package com.example.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.task.databinding.ActivitySetPinBinding

class SetPinAct : AppCompatActivity() {


    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var binding: ActivitySetPinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefManager = SharedPrefManager(this)

        binding.ed1.requestFocus()


        setEditTextListener(binding.ed1, binding.ed2, null)
        setEditTextListener(binding.ed2, binding.ed3, binding.ed1)
        setEditTextListener(binding.ed3, binding.ed4, binding.ed2)
        setEditTextListener(binding.ed4, null, binding.ed3)
        //
        setEditTextListener(binding.edc1, binding.edc2, null)
        setEditTextListener(binding.edc2, binding.edc3, binding.edc1)
        setEditTextListener(binding.edc3, binding.edc4, binding.edc2)
        setEditTextListener(binding.edc4, null, binding.edc3)
    }

    private fun setEditTextListener(
        currentEditText: EditText,
        nextEditText: EditText?,
        previousEditText: EditText?
    ) {
        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.length == 1 && nextEditText != null) {
                    nextEditText.requestFocus()
                }
                if (editable.isEmpty()) {
                    // If current EditText is empty, move focus to the previous EditText and delete content
                    previousEditText?.requestFocus()
                }

            }
        })
        currentEditText.setOnKeyListener(View.OnKeyListener setOnKeyListener@{ _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentEditText.text.isEmpty()) {
                // If delete key is pressed and current EditText is empty, move focus to the previous EditText and delete content
                previousEditText?.requestFocus()

                previousEditText?.text?.clear()
                return@setOnKeyListener true

            }
            false
        })

    }

    fun SubmitPin(view: View) {

        val pin = "${binding.ed1.text}${binding.ed2.text}${binding.ed3.text}${binding.ed4.text}"
        val confirmPin =
            "${binding.edc1.text}${binding.edc2.text}${binding.edc3.text}${binding.edc4.text}"

        if (pin.length == 4 && confirmPin.length == 4) {
            if (pin == confirmPin) {
                // Save PIN to SharedPreferences
                sharedPrefManager.setPin(pin)

                sharedPrefManager.saveString(applicationContext, "pin", pin)

                // Navigate to the first activity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                displayMessage("PINs do not match. Please try again.")
            }
        } else {
            displayMessage("Please enter a 4-digit PIN.")
        }

    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}