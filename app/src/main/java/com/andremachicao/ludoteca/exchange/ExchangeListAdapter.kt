package com.andremachicao.ludoteca.exchange

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andremachicao.ludoteca.databinding.ExchangeGameItemBinding
import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange

class ExchangeListAdapter:RecyclerView.Adapter<ListOfGamesExchangeViewHolder>(){
    private val exchangeList: MutableList<Exchange> = mutableListOf()
    private var onExchangeItemClickListener:((exchange: Exchange)->Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(newElementList:List<Exchange>){
        exchangeList.clear()
        exchangeList.addAll(newElementList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfGamesExchangeViewHolder {
        val binding = ExchangeGameItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListOfGamesExchangeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListOfGamesExchangeViewHolder, position: Int) {
        holder.bind(exchangeList[position])
        holder.binding.root.setOnClickListener{
            onExchangeItemClickListener?.invoke(exchangeList[position])
        }
    }

    override fun getItemCount(): Int {
        return exchangeList.size
    }

    fun setOnExchangeClickListener(onExchangeItemClickListener:((exchange: Exchange)->Unit)?){
        this.onExchangeItemClickListener=onExchangeItemClickListener
    }

}

class ListOfGamesExchangeViewHolder(val binding: ExchangeGameItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(exchange: Exchange){
        binding.exchangeInfo = exchange
    }

}

