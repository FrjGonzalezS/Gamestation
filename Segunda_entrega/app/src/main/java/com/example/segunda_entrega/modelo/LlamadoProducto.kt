package com.example.segunda_entrega.modelo

import com.google.gson.annotations.SerializedName

data class LlamadoProducto (
    @SerializedName("p_codigo")
    val p_codigo: String,
    @SerializedName("p_nombre")
    val p_nombre: String,
    @SerializedName("p_descripcion")
    val p_descripcion: String,
    @SerializedName("p_precio")
    val p_precio: Int,
    @SerializedName("p_mail_creado")
    val p_mail_creado: String
)