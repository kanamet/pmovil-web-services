package com.pmovil.webservices.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class PokemonAdapterFirebase(context: Context) : PokemonAdapter(context) {

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    companion object {
        private const val TAG = "PokemonAdapterFirebase"
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {

        val pokemon = data[position]
        holder.numberTxt.text = "N.º %03d".format(pokemon.number)
        holder.nameTxt.text = pokemon.name
        holder.typesTxt.text = pokemon.types.toString()

//        loadImageWithCache(holder.picture, pokemon.pictureId)
        loadImageWithGlide(holder.picture, pokemon.pictureId)

        holder.view.setOnClickListener {
            onItemClickListener?.onItemClick(pokemon)
        }
    }

    private fun loadImageWithCache(imageView: ImageView, pictureId: String) {
        val fileName = "$pictureId.png"
        val imageFile = File(context.cacheDir, fileName)

        if (!imageFile.exists()) {
            // Storage reference
            storage.reference.child(fileName).getFile(imageFile)
                .addOnSuccessListener {
                    imageView.setImageDrawable(Drawable.createFromPath(imageFile.absolutePath))
                    Log.i(TAG, "Success Download")
                }.addOnFailureListener {
                    Log.e(TAG, "Failed Download")
                }.addOnCanceledListener {
                    Log.e(TAG, "Canceled Download")
                }.addOnPausedListener {
                    Log.i(TAG, "Paused Download")
                }.addOnProgressListener {
                    Log.i(TAG, "Progress Download")
                }
        } else {
            imageView.setImageDrawable(Drawable.createFromPath(imageFile.absolutePath))
        }

        // GS reference
//        storage.getReferenceFromUrl("gs://pmovil-20192-pm.appspot.com/$pictureId.png")

        // Https Reference
/*
        storage.getReferenceFromUrl(
            "https://firebasestorage.googleapis.com/v0/b/pmovil-20192-pm.appspot.com/o/$pictureId.png"
        )
*/
    }

    private fun loadImageWithGlide(imageView: ImageView, pictureId: String) {
        storage.reference.child("$pictureId.png").downloadUrl
            .addOnCompleteListener {
                Glide.with(context)
                    .load(it.result)
                    .into(imageView)
            }
    }
}