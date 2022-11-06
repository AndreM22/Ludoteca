package com.andremachicao.ludoteca

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremachicao.ludoteca.model.Juego

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JuegosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JuegosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: AdaptadorJuego
    private lateinit var recyclerView: RecyclerView
    private lateinit var juegosList: ArrayList<Juego>

    lateinit var nombre: Array<String>
    lateinit var precio: Array<Double>
    lateinit var estado: Array<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_juegos, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment JuegosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JuegosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarData()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.juegosRecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = AdaptadorJuego(juegosList)
        recyclerView.adapter = adapter
    }

    private fun iniciarData(){
        juegosList = arrayListOf<Juego>()

        nombre= arrayOf<String>(
            "Catan",
            "Exploding kittens",
            "Monopoly u-built",
            "Uno",
            "Clue"
        )

        precio = arrayOf<Double>(
            50.0,
            700.0,
            153.0,
            30.0,
            12.0
        )

        estado = arrayOf<Double>(
            10.0,
            9.0,
            8.0,
            7.0,
            6.0
        )

        for (i in nombre.indices){
            val juego = Juego(nombre[i],1,estado[i],"ImageNotFOund",precio[i],null,null,null,null)
            juegosList.add(juego)
        }
    }
}