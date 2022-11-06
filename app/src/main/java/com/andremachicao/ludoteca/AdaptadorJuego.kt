package com.andremachicao.ludoteca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andremachicao.ludoteca.model.Juego

class AdaptadorJuego (val listaJuegos: List<Juego>): RecyclerView.Adapter<AdaptadorJuego.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_juego, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val juego = listaJuegos[position]

        holder.nombre.text = juego.nombre
        holder.estado.text = juego.Estado.toString() + "/10"
        holder.precio.text = juego.Precio.toString() + " bs"
    }

    override fun getItemCount(): Int {
        return listaJuegos.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imagen : ImageView = itemView.findViewById(R.id.rv_juego_imagen)
        val nombre : TextView = itemView.findViewById(R.id.rv_juego_nombre)
        val precio : TextView = itemView.findViewById(R.id.rv_juego_precio)
        val estado : TextView = itemView.findViewById(R.id.rv_juego_estado)

    }
}