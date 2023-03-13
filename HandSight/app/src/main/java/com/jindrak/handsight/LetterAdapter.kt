package com.jindrak.handsight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView

class LetterAdapter(var letterArrayList: List<Letter>) :
    RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LetterViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.dict_item, parent, false)
        return LetterViewHolder(view)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        holder.itemImage.setImageResource(letterArrayList[position].image)
        holder.itemtxt.text = letterArrayList[position].letter
        holder.description.text = letterArrayList[position].description

        val visible = letterArrayList[position].visible
        holder.expandable.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return letterArrayList.size
    }

    inner class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: RoundedImageView
        var itemtxt: TextView
        private var card: ConstraintLayout
        var description: TextView
        var expandable: ConstraintLayout

        init {
            itemImage = itemView.findViewById(R.id.rivLetter)
            itemtxt = itemView.findViewById(R.id.tvLetter)
            card = itemView.findViewById(R.id.card)
            description = itemView.findViewById(R.id.description)
            expandable = itemView.findViewById(R.id.expandedLayout)
            card.setOnClickListener {
                val letter = letterArrayList[adapterPosition]
                letter.visible = !letter.visible
                notifyItemChanged(adapterPosition)
            }
        }
    }
}
