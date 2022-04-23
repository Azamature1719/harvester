package com.example.harvester.framework.ui.main

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harvester.databinding.ProductRecyclerViewItemBinding
import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.entities.realm_entities.product_type.ProductType


class ProductsRecyclerView () : RecyclerView.Adapter<ProductsRecyclerView.ProductItemViewHolder>() {
    private lateinit var listItems: MutableList<ProductInfoDTO>

    fun setItems(items: MutableList<ProductInfoDTO>) {
        println(items)
        listItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        return ProductItemViewHolder(ProductRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) = holder.bind(listItems[position])

    override fun getItemCount(): Int = listItems.size

    inner class ProductItemViewHolder(private val binding: ProductRecyclerViewItemBinding) : RecyclerView.ViewHolder (binding.root) {
        fun bind(item: ProductInfoDTO) {
            when(item.markedGoodTypeCode) {
                ProductType.shoes.ordinal ->{
                    binding.iconBottle.visibility = GONE
                    binding.iconShoes.visibility = VISIBLE
                    binding.shoesArticle.text = item.article
                }
                ProductType.alcoholMarked.ordinal, ProductType.alcoholUnMarked.ordinal ->{
                    binding.iconBottle.visibility = VISIBLE
                    binding.iconShoes.visibility = GONE
                    binding.alcoholCode.text = item.alcoholCode
                    binding.alcoholVolume.text = item.alcoholVolume.toString()
                    binding.alcoholStrength.text = item.alcoholCapacity.toString()
                }
            }
            binding.productName.text = item.name
            binding.productDescription.text = item.description
            binding.productNotScannedCount.text = item.quantity.toString()
            binding.productSum.text = (item.price * item.quantity).toString()
            binding.productNotScannedCount.text = item.quantity.toString()
        }
    }
}