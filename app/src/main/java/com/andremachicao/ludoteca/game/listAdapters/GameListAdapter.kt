package com.andremachicao.ludoteca

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andremachicao.ludoteca.databinding.GameItemBinding
import com.andremachicao.ludoteca.game.model.Game

class GameListAdapter:RecyclerView.Adapter<ListOfGamesViewHolder>() {
    private val gameList: MutableList<Game> = mutableListOf()
    private var onGameItemClickListener:((game: Game)-> Unit)? = null


    @SuppressLint("NotifyDataSetChanged")
    fun addAll(newElementList:List<Game>){
        gameList.clear()
        gameList.addAll(newElementList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfGamesViewHolder {
        val binding = GameItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListOfGamesViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListOfGamesViewHolder, position: Int) {
        holder.bind(gameList[position])
        holder.binding.root.setOnClickListener {
            onGameItemClickListener?.invoke(gameList[position])
        }


    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    fun setOnGameClickListener(onGameItemClickListener: ((game: Game)-> Unit)?){
        this.onGameItemClickListener=onGameItemClickListener
    }


}



class ListOfGamesViewHolder(val binding: GameItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(game: Game){
        binding.gameInfo = game
    }
}