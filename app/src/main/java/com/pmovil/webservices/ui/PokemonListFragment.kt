package com.pmovil.webservices.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pmovil.persistencia.interfaces.OnItemClickListener
import com.pmovil.webservices.R
import com.pmovil.webservices.adapter.PokemonAdapterDrawable
import com.pmovil.webservices.model.Pokemon
import kotlinx.android.synthetic.main.fragment_pokemon_list.view.*


class PokemonListFragment : Fragment() {
    private lateinit var mView: View


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

        loadDataLocal()

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
        // TODO Implement Logic
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        // TODO Implement Logic
    }


    private fun onOpenDocumentResult(uri: Uri) {
        // TODO Implement Logic
    }

    private fun onCreateDocumentResult(uri: Uri) {
        // TODO Implement Logic
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
                // TODO Implement Logic
            }

            R.id.menu_option_download_file -> {
                // TODO Implement Logic
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val mItemClickListener = object : OnItemClickListener<Pokemon> {
        override fun onItemClick(pokemon: Pokemon) {
        }
    }

    private fun exportToFirestore() {
/*
        val db = FirebaseFirestore.getInstance()

        getPokemonList().forEach {
            db.collection("pokemon").document("%03d".format(it.number)).set(it)
        }

        getPokemonListExtra().forEach {
            db.collection("pokemon").document("%03d".format(it.number)).set(it)
        }

        AppUtils.showSnackbar(mView, "Data exported to Firestore")

*/
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