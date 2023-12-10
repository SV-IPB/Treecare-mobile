package com.example.treecare

import android.accounts.NetworkErrorException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.user.MainActivity
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.UserService
import com.example.treecare.service.api.v1.request.LoginRequest
import com.example.treecare.service.api.v1.response.UserResponse
import com.google.android.material.textfield.TextInputEditText
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: AppCompatButton
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferenceManager = PreferenceManager(this)

        checkAuthToken()

        btnLogin = findViewById(R.id.btnLogin)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)

        etUsername.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                etUsername.hint = ""
            } else {
                etUsername.hint = "Masukan Username"
            }
        })

        etPassword.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                etPassword.hint = ""
            } else {
                etPassword.hint = "Masukan Password"
            }
        })

        btnLogin.setOnClickListener {
            if (etUsername.text.toString() == ""){
                etUsername.error = "Username wajib diisi"
                etUsername.requestFocus()
            } else if (etPassword.text.toString() == "") {
                etPassword.error = "Password wajib diisi"
                etPassword.requestFocus()
            } else {
                btnLogin.isEnabled = false
                btnLogin.setBackgroundResource(R.drawable.btn_bg_grey)
                etUsername.requestFocus()
                etPassword.requestFocus()

                logIn(etUsername.text.toString(),
                    etPassword.text.toString())

            }
        }
    }

    fun checkAuthToken() {

        if (preferenceManager.getAccessToken().equals("")) {
            return
        }

        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    protected fun logIn(username: String, password: String) {

        val request = LoginRequest()
        request.username = username
        request.password = password

        val retro = RetrofitHelperV1()
            .getApiClient()
            .create(UserService::class.java)

        retro.login(request)
            .enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == 400) {
                    btnLogin.isEnabled = true
                    btnLogin.setBackgroundResource(R.drawable.btn_bg)
                    etUsername.error = "Tidak sesuai"
                    etPassword.error = "Tidak sesuai"
                    etUsername.requestFocus()

                    Toast.makeText(
                        this@LoginActivity,
                        "Username dan Password tidak sesuai",
                        Toast.LENGTH_SHORT
                    ).show()

                    return
                }

                val user = response.body()
                if (user == null) {
                    return
                }

                preferenceManager.setId((user.data?.user?.id.toString()))
                preferenceManager.setAccessToken(user.data?.access_token.toString())
                preferenceManager.setRefreshToken(user.data?.refresh_token.toString())
                preferenceManager.setNama(user.data?.user?.name.toString())
                preferenceManager.setUsername(user.data?.user?.username.toString())
                preferenceManager.setEmail(user.data?.user?.email.toString())
                preferenceManager.setRole(user.data?.user?.role.toString())

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                btnLogin.isEnabled = true
                btnLogin.setBackgroundResource(R.drawable.btn_bg)
                etUsername.requestFocus()

                Toast.makeText(
                    this@LoginActivity,
                    "Periksa koneksi internet anda",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }

}