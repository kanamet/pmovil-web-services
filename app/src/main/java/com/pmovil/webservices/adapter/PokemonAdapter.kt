package com.pmovil.webservices.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pmovil.persistencia.interfaces.OnItemClickListener
import com.pmovil.webservices.R
import com.pmovil.webservices.model.Pokemon
import kotlinx.android.synthetic.main.item_pokemon.view.*

abstract class PokemonAdapter(val context: Context) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    var data: List<Pokemon> = emptyList()
    var onItemClickListener: OnItemClickListener<Pokemon>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)

        return PokemonViewHolder(
            itemView,
            itemView.pokemon_number,
            itemView.pokemon_name,
            itemView.pokemon_types,
            itemView.pokemon_picture
        )
    }

    override fun getItemCount(): Int = data.size

    class PokemonViewHolder(
        val view: View,
        val numberTxt: TextView,
        val nameTxt: TextView,
        val typesTxt: TextView,
        val picture: ImageView
    ) :
        RecyclerView.ViewHolder(view)

}