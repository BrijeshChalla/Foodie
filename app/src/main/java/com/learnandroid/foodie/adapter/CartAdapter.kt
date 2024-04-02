package com.learnandroid.foodie.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodie.databinding.CartItemBinding

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemsPrice: MutableList<String>,
    private var cartInfo: MutableList<String>,
    private val cartImage: MutableList<String>,
    private var cartIngredient: MutableList<String>,
    private var cartQuantity: MutableList<Int>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // instance Firebase
    private val auth = FirebaseAuth.getInstance()

    init {
        // initialize firebase
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemsReference = database.reference.child("user").child(userId).child("cartItems")
    }

    companion object {
        // get updated quantity
        fun getUpdatedItemsQuantities(): MutableList<Int> {
            val itemQuantity = mutableListOf<Int>()
            itemQuantity.addAll(itemQuantities.toList())
            return itemQuantity
        }

        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size
    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                txtFoodNameCart.text = cartItems[position]
                priceCart.text = cartItemsPrice[position]

                // load image using glide
                val uriString = cartImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(imgFoodIconCart)
                txtQuantity.text = quantity.toString()

                btnMinus.setOnClickListener {
                    decreaseQuantity(position)
                }
                btnPlus.setOnClickListener {
                    increaseQuantity(position)
                }

                btnDelete.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                cartQuantity[position] = itemQuantities[position]
                binding.txtQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                cartQuantity[position] = itemQuantities[position]
                binding.txtQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if (uniqueKey != null) {
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartImage.removeAt(position)
                    cartInfo.removeAt(position)
                    cartQuantity.removeAt(position)
                    cartItemsPrice.removeAt(position)
                    cartIngredient.removeAt(position)
                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
                    //update itemQuantities
                    itemQuantities =
                        itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    // loop for snapshot children
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}