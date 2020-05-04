package com.pmovil.webservices.adapter

import android.content.Context

class PokemonAdapterDrawable(context: Context) : PokemonAdapter(context) {
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = data[position]
        holder.numberTxt.text = "N.º %03d".format(pokemon.number)
        holder.nameTxt.text = pokemon.name
        holder.typesTxt.text = pokemon.types.toString()

        val pictureId = context.resources.getIdentifier(
            pokemon.pictureId,
            "drawable",
            context.packageName
        )

        holder.picture.setImageResource(pictureId)

        holder.view.setOnClickListener {
            onItemClickListener?.onItemClick(pokemon)
        }
    }
}