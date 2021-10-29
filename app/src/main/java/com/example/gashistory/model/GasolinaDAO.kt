package com.example.gashistory.model

interface GasolinaDAO {
    fun criarGasolina(gasolina: Gasolina): Long
    fun recuperarGasolina(data: String): Gasolina
    fun recuperarGasolina(): MutableList<Gasolina>
    fun atualizarGasolina(gasolina: Gasolina): Int
    fun removerGasolina(data: String): Int
}