package com.example.ruznev_magaz.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ruznev_magaz.R
import com.example.ruznev_magaz.data.DbHelper
import com.example.ruznev_magaz.presentation.home.HomeNavigation

class Authorization : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.authorization_screen)

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_password_auth)
        val button: Button = findViewById(R.id.user_button_auth)
        val linkToRegistration: TextView = findViewById(R.id.create_account_text)
        val linkToForgetPassword: TextView= findViewById(R.id.user_email)

        linkToRegistration.setOnClickListener()
        {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
        linkToForgetPassword.setOnClickListener()
        {
            val intent= Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

        button.setOnClickListener()
        {
            val login = userLogin.text.toString().trim()
            val password = userPass.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            } else {
                val db = DbHelper(this, null)
                val isAuth = db.getUser(login, password)

                if(isAuth)
                {
                    Toast.makeText(this, "Авторизация успешна, пользователь $login найден", Toast.LENGTH_SHORT).show()
                    userLogin.text.clear()
                    userPass.text.clear()

                    val user = db.getUserByLogin(login)
                    if (user != null) {
                        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("login", login)
                            putString("email", user.email)
                            apply()
                        }

                        Log.d("Authorization", "Saved login : $login")
                        Log.d("Authorization", "Saved email : ${user.email}")

                        val intent = Intent(this, HomeNavigation::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Ошибка: email пользователя не найден", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(this, "Неверные данные, $login не найден", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}