package com.jindrak.handsight

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DictionaryFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var letterArrayList: MutableList<Letter>? = null
    private lateinit var letters: List<String>
    private lateinit var descriptions: List<String>
    private lateinit var imageResourceIds: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dictionary, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)

        letters = listOf("A", "B", "C", "D", "E", "F", "G", "H", "CH", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
        descriptions = listOf(
            getString(R.string.description_a),
            getString(R.string.description_b),
            getString(R.string.description_c),
            getString(R.string.description_d),
            getString(R.string.description_e),
            getString(R.string.description_f),
            getString(R.string.description_g),
            getString(R.string.description_h),
            getString(R.string.description_ch),
            getString(R.string.description_i),
            getString(R.string.description_j),
            getString(R.string.description_k),
            getString(R.string.description_l),
            getString(R.string.description_m),
            getString(R.string.description_n),
            getString(R.string.description_o),
            getString(R.string.description_p),
            getString(R.string.description_q),
            getString(R.string.description_r),
            getString(R.string.description_s),
            getString(R.string.description_t),
            getString(R.string.description_u),
            getString(R.string.description_v),
            getString(R.string.description_w),
            getString(R.string.description_x),
            getString(R.string.description_y),
            getString(R.string.description_z)
        )
        imageResourceIds = listOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g,
            R.drawable.h,
            R.drawable.ch,
            R.drawable.i,
            R.drawable.j,
            R.drawable.k,
            R.drawable.l,
            R.drawable.m,
            R.drawable.n,
            R.drawable.o,
            R.drawable.p,
            R.drawable.q,
            R.drawable.r,
            R.drawable.s,
            R.drawable.t,
            R.drawable.u,
            R.drawable.v,
            R.drawable.w,
            R.drawable.x,
            R.drawable.y,
            R.drawable.z
        )

        recyclerView!!.adapter = LetterAdapter(initData())
        return view
    }


    private fun initData(): List<Letter> {
        letterArrayList = ArrayList()
        for (i in letters.indices) {
            letterArrayList!!.add(
                Letter(
                letters[i],
                "▪ "+descriptions[i].replace(";", "\n▪ "),
                imageResourceIds[i]
            )
            )
        }
        return letterArrayList!!
    }

}