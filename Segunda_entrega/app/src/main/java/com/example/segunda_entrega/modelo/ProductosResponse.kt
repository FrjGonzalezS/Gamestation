package com.example.segunda_entrega.modelo

data class ProductosResponse(
    val productos: List<LlamadoProducto>,
    val metaData: MetaData
)