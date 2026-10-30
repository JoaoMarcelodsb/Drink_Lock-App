package com.example.drink_lock.data.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// 1. DATA CLASSES (O formato dos JSONs que vão e voltam)
data class LoginRequest(val email: String, val senha: String)
data class LoginResponse(val mensagem: String, val token: String?, val erro: String?)

data class CadastroRequest(val nome: String, val email: String, val senha: String)
data class CadastroResponse(val mensagem: String, val erro: String?)

data class EsqueciSenhaRequest(val email: String)
data class EsqueciSenhaResponse(val mensagem: String, val erro: String?)

// 2. INTERFACE (O cardápio de rotas)
interface DrunkLockApi {
    @POST("api/usuarios/login")
    fun fazerLogin(@Body dados: LoginRequest): Call<LoginResponse>

    @POST("api/usuarios/cadastrar")
    fun cadastrarUsuario(@Body dados: CadastroRequest): Call<CadastroResponse>

    @POST("api/usuarios/esqueci-senha")
    fun recuperarSenha(@Body dados: EsqueciSenhaRequest): Call<EsqueciSenhaResponse>
}

// 3. CLIENTE RETROFIT (A máquina que faz a conexão)
object RetrofitClient {
    private const val BASE_URL = "http://192.168.56.10:3000/" // Coloque seu IP real aqui

    val api: DrunkLockApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DrunkLockApi::class.java)
    }
}
