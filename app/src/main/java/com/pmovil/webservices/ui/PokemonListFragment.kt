package com.pmovil.webservices.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pmovil.persistencia.interfaces.OnItemClickListener
import com.pmovil.webservices.AppUtils
import com.pmovil.webservices.R
import com.pmovil.webservices.adapter.PokemonAdapterDrawable
import com.pmovil.webservices.adapter.PokemonAdapterFirebase
import com.pmovil.webservices.database.AppCollection
import com.pmovil.webservices.model.Pokemon
import kotlinx.android.synthetic.main.fragment_pokemon_list.view.*
import java.io.FileNotFoundException
import java.io.FileOutputStream


class PokemonListFragment : Fragment() {
    private lateinit var mView: View

    private lateinit var firebaseStorage: FirebaseStorage

    companion object {
        private const val TAG = "PokemonListFragment"
        const val RC_OPEN_DOCUMENT = 100
        const val RC_CREATE_DOCUMENT = 200
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        mView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

//        loadDataLocal()

        firebaseStorage = FirebaseStorage.getInstance()
        loadDataFirebase()

        return mView
    }

    private fun loadDataLocal() {
        val pokemonList = getPokemonList()
        val pokemonAdapter = PokemonAdapterDrawable(requireContext())

        pokemonAdapter.data = pokemonList
        pokemonAdapter.onItemClickListener = mItemClickListener

        mView.recyclerView.adapter = pokemonAdapter
        mView.recyclerView.layoutManager = GridLayoutManager(context, 2)
    }

    private fun loadDataFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection(AppCollection.POKEMON).get()
            .addOnSuccessListener { document ->
                val pokemonList = document.toObjects(Pokemon::class.java)
                val pokemonAdapter = PokemonAdapterFirebase(requireContext())

                pokemonAdapter.data = pokemonList
                pokemonAdapter.onItemClickListener = mItemClickListener

                mView.recyclerView.adapter = pokemonAdapter
                mView.recyclerView.layoutManager = GridLayoutManager(context, 2)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == RC_OPEN_DOCUMENT && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                Log.i(TAG, "Uri: $uri")
                onOpenDocumentResult(uri);
            }
        }

        if (requestCode == RC_CREATE_DOCUMENT) {
            resultData?.data?.also { uri ->
                Log.i(TAG, "Uri: $uri")
                onCreateDocumentResult(uri)
            }
        }
    }

    private fun onOpenDocumentResult(uri: Uri) {
        try {
            val fileName = getFilename(uri, "document")
            firebaseStorage.reference
                .child("documents/$fileName")
                .putFile(uri)
            AppUtils.showToast(context, "Filename: $fileName")
        } catch (e: FileNotFoundException) {
            Log.i(TAG, "${e.message}")
            e.printStackTrace()
        }
    }

    private fun onCreateDocumentResult(uri: Uri) {
        firebaseStorage.reference
            .child("pokemon001.png")
            .getStream { taskSnapshot, inputStream ->
                context?.contentResolver?.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use { outputStream ->
                        val buffer = ByteArray(4096)
                        var bytesRead = inputStream.read(buffer)

                        while (bytesRead != -1) {
                            outputStream.write(buffer)
                            bytesRead = inputStream.read(buffer)
                        }
                    }
                }
            }
    }

    private fun getFilename(uri: Uri, defaultValue: String): String {
        context?.contentResolver?.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst().let {
                return cursor.getString(nameIndex)
            }
        }

        return defaultValue
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_pokemon_list_fragment, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option_export_to_firestore -> exportToFirestore()

            R.id.menu_option_upload_file -> {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }

                startActivityForResult(intent, RC_OPEN_DOCUMENT)
            }

            R.id.menu_option_download_file -> {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/png"
                    putExtra(Intent.EXTRA_TITLE, "pokemon001.png")
                }

                startActivityForResult(intent, RC_CREATE_DOCUMENT)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val mItemClickListener = object : OnItemClickListener<Pokemon> {
        override fun onItemClick(pokemon: Pokemon) {
        }
    }

    private fun exportToFirestore() {
        val db = FirebaseFirestore.getInstance()

        getPokemonList().forEach {
            db.collection("pokemon").document("%03d".format(it.number)).set(it)
        }

        getPokemonListExtra().forEach {
            db.collection("pokemon").document("%03d".format(it.number)).set(it)
        }

        AppUtils.showSnackbar(mView, "Data exported to Firestore")

    }

    private fun getPokemonList(): ArrayList<Pokemon> {
        return arrayListOf(
            Pokemon(1, "Bulbasaur", arrayListOf("Planta", "Veneno"), "pokemon001"),
            Pokemon(2, "Ivysaur", arrayListOf("Planta", "Veneno"), "pokemon002"),
            Pokemon(3, "Venusaur", arrayListOf("Planta", "Veneno"), "pokemon003"),
            Pokemon(4, "Charmander", arrayListOf("Fuego"), "pokemon004"),
            Pokemon(5, "Charmeleon", arrayListOf("Fuego"), "pokemon005"),
            Pokemon(6, "Charizard", arrayListOf("Fuego", "Volador"), "pokemon006"),
            Pokemon(7, "Squirtle", arrayListOf("Agua"), "pokemon007"),
            Pokemon(8, "Wartortle", arrayListOf("Agua"), "pokemon008"),
            Pokemon(9, "Blatoide", arrayListOf("Agua"), "pokemon009")
        )
    }

    private fun getPokemonListExtra(): ArrayList<Pokemon> {
        return arrayListOf(
            Pokemon(10, "Caterpie", arrayListOf("Bicho"), "pokemon010"),
            Pokemon(11, "Metapod", arrayListOf("Bicho"), "pokemon011"),
            Pokemon(12, "Butterfree", arrayListOf("Bicho, Volador"), "pokemon012"),
            Pokemon(13, "Weedle", arrayListOf("Bicho", "Veneno"), "pokemon013"),
            Pokemon(14, "Kakuna", arrayListOf("Bicho", "Veneno"), "pokemon014"),
            Pokemon(15, "Beedril", arrayListOf("Bicho", "Veneno"), "pokemon015"),
            Pokemon(16, "Pidgey", arrayListOf("Normal", "Volador"), "pokemon016"),
            Pokemon(17, "Pidgeotto", arrayListOf("Normal", "Volador"), "pokemon017"),
            Pokemon(18, "Pidgeot", arrayListOf("Normal", "Volador"), "pokemon018")
        )
    }
}