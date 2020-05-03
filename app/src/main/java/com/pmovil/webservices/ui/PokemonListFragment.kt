package com.pmovil.webservices.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pmovil.persistencia.interfaces.OnItemClickListener
import com.pmovil.webservices.AppUtils
import com.pmovil.webservices.R
import com.pmovil.webservices.model.Pokemon
import kotlinx.android.synthetic.main.fragment_pokemon_list.view.*


class PokemonListFragment : Fragment() {
    private lateinit var mView: View
    private var isAuthenticated = false

    companion object {
        private const val TAG = "PokemonListFragment"
        const val REQUEST_LOG_IN = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        mView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)
        mView.trainer_info.visibility = View.GONE

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

    }

    private val mItemClickListener = object : OnItemClickListener<Pokemon> {
        override fun onItemClick(pokemon: Pokemon) {
        }
    }


    private fun initLogIn() {

    }

    private fun initLogOut() {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main_activity, menu)

        if (isAuthenticated) {
            menu?.findItem(R.id.menu_option_log_in)?.isVisible = false
        } else {
            menu?.findItem(R.id.menu_option_log_out)?.isVisible = false
        }

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option_log_in -> initLogIn()
            R.id.menu_option_log_out -> initLogOut()
            R.id.menu_option_export_to_firestore -> exportToFirestore()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exportToFirestore() {
        AppUtils.showSnackbar(mView, "Data exported to Firestore")
    }

    private fun setAuthenticationStatus(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
        setHasOptionsMenu(true)

        if (isAuthenticated) {
            mView.trainer_info.visibility = View.VISIBLE
        } else {
            mView.trainer_info.visibility = View.GONE
        }
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