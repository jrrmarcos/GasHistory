package com.example.gashistory.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gashistory.OnGasolinaClickListener
import com.example.gashistory.R
import com.example.gashistory.databinding.LayoutGasolinaBinding
import com.example.gashistory.model.Gasolina

class GasolinaAdapter (
    private val onGasolinaClickListener: OnGasolinaClickListener,
    private val gasolinaList: MutableList<Gasolina>
    ): RecyclerView.Adapter<GasolinaAdapter.GasolinaLayoutHolder>() {

    var posicao: Int = -1

    inner class GasolinaLayoutHolder(layoutGasolinaBinding: LayoutGasolinaBinding): RecyclerView.ViewHolder(layoutGasolinaBinding.root),
        View.OnCreateContextMenuListener {
            val dataTv: TextView = layoutGasolinaBinding.dataTv
            val precoTv: TextView = layoutGasolinaBinding.precoTv

            init {
                itemView.setOnCreateContextMenuListener(this)
            }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                view: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                MenuInflater(view?.context).inflate(R.menu.context_manu_main, menu)
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GasolinaLayoutHolder {
        val layoutGasolinaBinding = LayoutGasolinaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder: GasolinaLayoutHolder = GasolinaLayoutHolder(layoutGasolinaBinding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: GasolinaLayoutHolder, position: Int) {
        val gasolina = gasolinaList[position]

        with(holder) {
            dataTv.text = gasolina.data
            precoTv.text = gasolina.preco

            itemView.setOnClickListener{
                onGasolinaClickListener.onGasolinaClick(position)
            }

            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }

    override fun getItemCount(): Int = gasolinaList.size
    }
