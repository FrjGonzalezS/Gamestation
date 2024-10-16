package com.example.segunda_entrega.servicio

import com.example.segunda_entrega.modelo.CrearProducto
import com.example.segunda_entrega.modelo.LlamadoProducto
import com.example.segunda_entrega.modelo.ProductosResponse
import com.example.segunda_entrega.modelo.Respuesta
import com.example.segunda_entrega.modelo.Usuario
import com.example.segunda_entrega.modelo.UsuarioLogin
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServicio {
    @POST("route/usuario_duoc_almacenar")
    suspend fun usuarioAlmacenar(@Body usuario: Usuario): Response<List<Respuesta>>

    @POST("route/usuario_duoc_login")
    suspend fun usuarioLogin(@Body usuario: UsuarioLogin): Response<List<Respuesta>>

    @POST("route/producto_duoc_almacenar")
    suspend fun crearProducto(@Body usuario: CrearProducto): Response<List<Respuesta>>

    //@GET("route/producto_duoc_obtener_x_mail?mail=admin@gmail.com")
   // suspend fun getProductosPorCorreo(): Response<List<Respuesta>>
                //(@Query("mail") correo: String): Response<List<Respuesta>>
    /*
    @GET("route/producto_duoc_obtener_x_mail?mail=admin@gmail.com")
    suspend fun getProductosPorCorreo(): Response<List<Respuesta>>
    */


    @GET("route/producto_duoc_obtener_x_mail?mail=admin@gmail.com")
    suspend fun getProductosPorCorreo(): Response<ResponseBody>

    @GET("route/producto_duoc_obtener_x_mail?mail=admin@gmail.com")
    suspend fun getProductosPorCorreo1(): Response<ProductosResponse>

    @GET("route/producto_duoc_obtener_x_mail")
    suspend fun getProductosPorCorreo1(@Query("mail") correo: String): Response<List<Respuesta>>
    @GET("route/producto_duoc_obtener_x_mail/{email}")
    suspend fun getProductosPorCorreo2(@Path("email") correo: String): Response<List<Respuesta>>

}
