package com.example.gashistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gashistory.adapter.GasolinaAdapter
import com.example.gashistory.controller.GasolinaController
import com.example.gashistory.databinding.ActivityMainBinding
import com.example.gashistory.model.Gasolina
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnGasolinaClickListener {

    companion object Extras {
        const val EXTRA_GASOLINA = "EXTRA_GASOLINA"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var gasolinaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarGasolinaActivityResultLauncher: ActivityResultLauncher<Intent>

    //Data Source
    private val gasolinaList: MutableList<Gasolina> by lazy {
        gasolinaController.buscarGasolina()
    }

    //Controller
    private val gasolinaController: GasolinaController by lazy {
        GasolinaController(this)
    }

    //LayoutManager
    private val gasolinaLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val gasolinaAdapter: GasolinaAdapter by lazy {
        GasolinaAdapter(this, gasolinaList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        //Associar Adapter e LayoutManager ao RecycleView
        activityMainBinding.gasolinaRv.adapter = gasolinaAdapter //Kotlin
        activityMainBinding.gasolinaRv.layoutManager = gasolinaLayoutManager

        //Adicionar um livro
        gasolinaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Gasolina>(EXTRA_GASOLINA)?.apply {
                    gasolinaController.inserirGasolina(this)
                    gasolinaList.add(this)
                    gasolinaAdapter.notifyDataSetChanged()
                }
            }
        }

        editarGasolinaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                val livro = resultado.data?.getParcelableExtra<Gasolina>(EXTRA_GASOLINA)?.apply {
                    if(posicao!=null && posicao!=-1) {
                        gasolinaController.modificarGasolina(this) // modificando no banco de dados
                        gasolinaList[posicao] = this
                        gasolinaAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarGasolinaFab.setOnClickListener {
            gasolinaActivityResultLauncher.launch(Intent(this, GasolinaActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val posicao = gasolinaAdapter.posicao
        val gasolina = gasolinaList[posicao]

        return when (item.itemId) {
            R.id.editarGasolinaMi -> {
                //Editar livro
                val editarGasolinaIntent = Intent(this, GasolinaActivity::class.java)
                editarGasolinaIntent.putExtra(EXTRA_GASOLINA, gasolina)
                editarGasolinaIntent.putExtra(EXTRA_POSICAO, posicao)
                editarGasolinaActivityResultLauncher.launch(editarGasolinaIntent)

                true
            }
            R.id.removerGasolinaMi -> {
                //Remover livro
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _, ->
                        gasolinaController.apagarGasolina(gasolina.data)
                        gasolinaList.removeAt(posicao)
                        gasolinaAdapter.notifyDataSetChanged()
                        Snackbar.make(
                            activityMainBinding.root,
                            "Histórico removido!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    setNegativeButton("Não") { _, _, ->
                        Snackbar.make(
                            activityMainBinding.root,
                            "Remoção cancelada",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    create()
                }.show()

                true
            }
            else -> {
                false
            }
        }
    }

    override fun onGasolinaClick(posicao: Int) {
        val gasolina = gasolinaList[posicao]
        val consultarGasolinaIntent = Intent (this, GasolinaActivity::class.java)
        consultarGasolinaIntent.putExtra(EXTRA_GASOLINA, gasolina)
        startActivity(consultarGasolinaIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.atualizarMi -> {
            gasolinaAdapter.notifyDataSetChanged()
            true
        } else ->  {
            false;
        }
    }
}
