package com.example.gerador_senha

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var switchUppercase: Switch
    private lateinit var switchLowercase: Switch
    private lateinit var switchNumbers: Switch
    private lateinit var switchSpecialChars: Switch
    private lateinit var switchExcludeSimilar: Switch
    private lateinit var seekBarLength: SeekBar
    private lateinit var textLength: TextView
    private lateinit var textGeneratedPassword: TextView
    private lateinit var buttonGenerate: Button
    private lateinit var buttonCopy: Button

    private val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
    private val numbers = "0123456789"
    private val specialChars = "!@#$%^&*()_-+=<>?/{}~|"
    private val similarChars = "il1o0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchUppercase = findViewById(R.id.switch_uppercase)
        switchLowercase = findViewById(R.id.switch_lowercase)
        switchNumbers = findViewById(R.id.switch_numbers)
        switchSpecialChars = findViewById(R.id.switch_special_chars)
        switchExcludeSimilar = findViewById(R.id.switch_exclude_similar)
        seekBarLength = findViewById(R.id.seekbar_length)
        textLength = findViewById(R.id.text_length)
        textGeneratedPassword = findViewById(R.id.text_generated_password)
        buttonGenerate = findViewById(R.id.button_generate)
        buttonCopy = findViewById(R.id.button_copy)

        seekBarLength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textLength.text = "Comprimento: ${progress + 8}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        buttonGenerate.setOnClickListener {
            val length = seekBarLength.progress + 8
            textGeneratedPassword.text = generatePassword(length)
        }

        buttonCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Generated Password", textGeneratedPassword.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Senha copiada!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generatePassword(length: Int): String {
        var characterSet = ""

        if (switchUppercase.isChecked) characterSet += uppercaseChars
        if (switchLowercase.isChecked) characterSet += lowercaseChars
        if (switchNumbers.isChecked) characterSet += numbers
        if (switchSpecialChars.isChecked) characterSet += specialChars

        if (switchExcludeSimilar.isChecked) {
            characterSet = characterSet.filterNot { it in similarChars }
        }

        if (characterSet.isEmpty()) return "Selecione ao menos uma opção"

        return (1..length)
            .map { characterSet[Random.nextInt(characterSet.length)] }
            .joinToString("")
    }
}
