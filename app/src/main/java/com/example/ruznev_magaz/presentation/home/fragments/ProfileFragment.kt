package com.example.ruznev_magaz.presentation.home.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.example.ruznev_magaz.R
import com.example.ruznev_magaz.data.DbHelper
import com.example.ruznev_magaz.presentation.home.HomeNavigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView

class ProfileFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile,container,false)

        val frameProfileUsername = view.findViewById<TextView>(R.id.fragment_profile_username)
        val frameProfileEmail = view.findViewById<TextView>(R.id.fragment_profile_email)
        val changePasswordButton = view.findViewById<AppCompatButton>(R.id.fragment_profile_change_password)

        val login = arguments?.getString("login")?:""
        val email = arguments?.getString("email")?:""

        frameProfileUsername.text = login
        frameProfileEmail.text = email

        val profileImage: ShapeableImageView = view.findViewById(R.id.frame_profile_picture)
        val changeImage: FloatingActionButton = view.findViewById(R.id.change_profile_picture)

        val db = DbHelper(requireContext(),null)
        val bitmap = db.loadProfileImage(email)
        if (bitmap != null){
            profileImage.setImageBitmap(bitmap)
        } else {
            profileImage.setImageResource(R.drawable.icons_default_avatar)
        }

        val launchGallery = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,result.data!!.data)
                profileImage.setImageBitmap(bitmap)

                val dbHelper = DbHelper(requireContext(),null)
                val email = arguments?.getString("email")?:""
                dbHelper.saveProfileImage(email, bitmap)

                val sharedPreferences = requireContext().getSharedPreferences("user_prefs",Context.MODE_PRIVATE)
                val login = sharedPreferences.getString("login","") ?: ""
                (requireActivity() as HomeNavigation).updateNavigationHeader(login,email)
            }
        }

        changeImage.setOnClickListener {
            val openGallery = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
            launchGallery.launch(openGallery)
        }

        changePasswordButton.setOnClickListener{
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Изменение пароля")
                setMessage("Пожалуйста, заполните все поля")

                val container = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(50,40,50,40)
                }
                val newPassword = EditText(requireContext()).apply {
                    hint = " Новый пароль"
                    inputType = InputType.TYPE_CLASS_TEXT
                }
                val confirmPassword = EditText(requireContext()).apply {
                    hint = "Подтвердите новый пароль"
                    inputType = InputType.TYPE_CLASS_TEXT
                }

                container.addView(newPassword)
                container.addView(confirmPassword)

                setView(container)

                setPositiveButton("Готово") { dialog, which ->
                    val newPass = newPassword.text.toString().trim()
                    val confirmPass = confirmPassword.text.toString().trim()

                    if (newPass.isEmpty() || confirmPass.isEmpty()) {
                        Toast.makeText(requireContext(),"Заполните все поля",Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    if (newPass !=confirmPass) {
                        Toast.makeText(requireContext(), "Ошибка подтверждения пароля", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val sharedPreferences = requireContext().getSharedPreferences("user_prefs",Context.MODE_PRIVATE)
                    val userEmail = sharedPreferences.getString("email",null)

                    if (userEmail == null) {
                        Toast.makeText(requireContext(),"Ошибка: пользователь не авторизован",Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val db = DbHelper(requireContext(),null)
                    val isUpdate = db.updatePassword(userEmail.toString(),newPass)
                    if(isUpdate) {
                        Toast.makeText(requireContext(),"Ваш пароль успешно изменен",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),"Неизвестная ошибка",Toast.LENGTH_SHORT).show()
                    }
                }

                setNegativeButton("Отмена"){ dialog, which ->
                    dialog.cancel()
                }
                show()
            }
        }

        return view
    }
}
