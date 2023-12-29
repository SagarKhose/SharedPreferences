package com.example.task

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.task.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefManager = SharedPrefManager(this)
        binding.ed1.requestFocus()
        setEditTextListener(binding.ed1, binding.ed2, null)
        setEditTextListener(binding.ed2, binding.ed3, binding.ed1)
        setEditTextListener(binding.ed3, binding.ed4, binding.ed2)
        setEditTextListener(binding.ed4, null, binding.ed3)
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {

            BiometricManager.BIOMETRIC_SUCCESS ->
                displayMessage("Biometric authentication is available")

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                displayMessage("This device doesn't support biometric authentication")

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                displayMessage("Biometric authentication is currently unavailable")

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                displayMessage("No biometric credentials are enrolled")
        }

        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    displayMessage("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    displayMessage("Authentication succeeded!")
                    navigateToNextPage()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    displayMessage("Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
        binding.setPin.setOnClickListener {
            // Navigate to the page where the user can set a new PIN
            startActivity(Intent(this, SetPinAct::class.java))
        }
        binding.authenticateButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
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
                checkAndNavigate()
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

    private fun checkAndNavigate() {
        val pin = "${binding.ed1.text}${binding.ed2.text}${binding.ed3.text}${binding.ed4.text}"
        if (pin.length == 4) {
            Log.d("TAG", "checkAndNavigate: "+sharedPrefManager.getPASSWORD(applicationContext,"pin","0"))
            if (pin == sharedPrefManager.getPin()) {
                navigateToNextPage()
            } else if (sharedPrefManager.getPin().isEmpty()) {

                displayMessage("User Doesn't exist")
            } else {
                displayMessage("Invalid PIN")
            }
        }
    }

    private fun navigateToNextPage() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}