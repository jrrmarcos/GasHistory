package com.example.gashistory.controller

import com.example.gashistory.MainActivity
import com.example.gashistory.model.Gasolina
import com.example.gashistory.model.GasolinaDAO
import com.example.gashistory.model.GasolinaFirebase

class GasolinaController (mainActivity: MainActivity){
    private val gasolinaDAO: GasolinaDAO = GasolinaFirebase()

    fun inserirGasolina(gasolina: Gasolina) = gasolinaDAO.criarGasolina(gasolina)
    fun buscarGasolina(data: String) = gasolinaDAO.recuperarGasolina(data)
    fun buscarGasolina() = gasolinaDAO.recuperarGasolina()
    fun modificarGasolina(gasolina: Gasolina) = gasolinaDAO.atualizarGasolina(gasolina)
    fun apagarGasolina(data: String) = gasolinaDAO.removerGasolina(data)

}