package com.aditya.sta.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aditya.sta.databinding.ItemKaryawanBinding


class KaryawanAdapter : RecyclerView.Adapter<KaryawanAdapter.ViewHolder>() {

    private var list = listOf<Karyawan>()

    fun submitList(newList: List<Karyawan>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemKaryawanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(karyawan: Karyawan) {
            binding.apply {
                tvId.text = karyawan.id
                tvNama.text = karyawan.nama
                tvTgl.text = karyawan.tglMasuk
                tvUsia.text = karyawan.usia.toString()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemKaryawanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
