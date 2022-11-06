package com.andremachicao.ludoteca.model

class Juego {
    var nombre: String = "Catan"
    var numCompra: Int = 1
    var Estado: Double = 10.0
    var Imagen: String = "ImageNotFound"
    var Precio: Double = 50.0
    var Descripcion: String? = null
    var Idioma: String? = null
    var numJugadores: Int? = null
    var Tiempo: String? = null

    constructor(
        nombre: String,
        numCompra: Int,
        Estado: Double,
        Imagen: String,
        Precio: Double,
        Descripcion: String?,
        Idioma: String?,
        numJugadores: Int?,
        Tiempo: String?
    ) {
        this.nombre = nombre
        this.numCompra = numCompra
        this.Estado = Estado
        this.Imagen = Imagen
        this.Precio = Precio
        this.Descripcion = Descripcion
        this.Idioma = Idioma
        this.numJugadores = numJugadores
        this.Tiempo = Tiempo
    }
}