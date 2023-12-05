package com.example.treecare.service.api.v1

import android.util.Log
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.response.UserResponse
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Call

class TokenAuthenticator(private val prefManager:PreferenceManager):Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            val newToken = refreshToken()
            if (newToken != null) {
                prefManager.setAccessToken(newToken)
                Log.e("TokenAuthenticator", "Token refreshed successfully")
                return response.request.newBuilder()
                    .header("Authorization", newToken)
                    .build()
            }
            else {
                Log.e("TokenAuthenticator", "Token refresh failed")
                Log.e("Message: ", response.message)
            }
        }
        return null
    }

    private fun refreshToken(): String? {
        val retrofit = RetrofitHelperV1().getApiClient()
        val authService = retrofit.create(UserService::class.java)
        val refreshTokenCall: Call<UserResponse> = authService.refreshToken(prefManager.getRefreshToken())

        try {
            val refreshTokenResponse: retrofit2.Response<UserResponse> = refreshTokenCall.execute()

            if (refreshTokenResponse.isSuccessful) {
                val newToken = refreshTokenResponse.body()?.data?.access_token
                return newToken
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}