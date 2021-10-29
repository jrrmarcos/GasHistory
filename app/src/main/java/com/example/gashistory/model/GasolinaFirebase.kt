package com.example.gashistory.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class GasolinaFirebase: GasolinaDAO {

    companion object {
        private val BD_GASOLINA = "gasolina"
    }

    private val gasolinaRtDb = Firebase.database.getReference(BD_GASOLINA)

    private val gasolinaList = mutableListOf<Gasolina>()

    init {

        gasolinaRtDb.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novoGasolina: Gasolina? = snapshot.value as? Gasolina
                novoGasolina?.apply{
                    if(gasolinaList.find{it.data == this.data} == null) {
                        gasolinaList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val gasolinaEditado: Gasolina? = snapshot.value as? Gasolina
                gasolinaEditado?.apply{
                    gasolinaList[gasolinaList.indexOfFirst { it.data == this.data }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val gasolinaRemovido: Gasolina? = snapshot.value as? Gasolina
                gasolinaRemovido?.apply{
                    gasolinaList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }


        })

        gasolinaRtDb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gasolinaList.clear()
                snapshot.children.forEach {
                    val gasolina: Gasolina = it.getValue<Gasolina>()?: Gasolina()
                    gasolinaList.add(gasolina)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }
        })
    }




    override fun criarGasolina(gasolina: Gasolina): Long {
        criarOuAtualizarGasolina(gasolina)
        return 0L
    }

    override fun recuperarGasolina(data: String): Gasolina = gasolinaList.firstOrNull{ it.data == data} ?: Gasolina()

    override fun recuperarGasolina(): MutableList<Gasolina> = gasolinaList

    override fun atualizarGasolina(gasolina: Gasolina): Int {
        criarOuAtualizarGasolina(gasolina)
        return 1
    }

    override fun removerGasolina(data: String): Int {
        gasolinaRtDb.child(data).removeValue()
        return 1
    }

    private fun criarOuAtualizarGasolina(gasolina: Gasolina) {
        gasolinaRtDb.child(gasolina.data).setValue(gasolina)
    }
}