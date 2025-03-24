package com.example.ruznev_magaz.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ruznev_magaz.R
import com.example.ruznev_magaz.data.DbHelper
import com.example.ruznev_magaz.domian.Services.EmailSender
import com.example.ruznev_magaz.domian.Services.EmailSender.Companion.sendNewPasswordByEmail

class ForgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgetpassword_screen)

        val db = DbHelper(this, null)

        val linkToAuthorization: ImageButton = findViewById(R.id.button_back)

        val resetPasswordButton: Button = findViewById(R.id.button_send_message)
        val etEmail: EditText = findViewById(R.id.user_email)

        linkToAuthorization.setOnClickListener()
        {
            val intent = Intent(this, Authorization::class.java)
            startActivity(intent)
        }

        resetPasswordButton.setOnClickListener()
        {
            val email = etEmail.text.toString().trim()

            if (email == "") {
                Toast.makeText(this, "Введите ваши данные", Toast.LENGTH_SHORT).show()
            }
            if (db.checkUserByEmail(email)) {
                val newPassword = EmailSender.generateNewPassword()

                if (db.updatePassword(email, newPassword)) {

                    val user = db.getUserByEmail(email)


                    if (user != null) {
                        sendNewPasswordByEmail(user.email, newPassword)
                        Toast.makeText(this, "Новый пароль отправлен на ваш email", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        Toast.makeText(this, "Ошибка при обновлении пароля", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            } else {
                Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show()
            }

        }
    }
}