package com.aditya.sta.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditya.sta.data.KaryawanDatabaseHelper
import com.aditya.sta.databinding.FragmentHomeBinding
import java.util.Calendar


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: KaryawanDatabaseHelper
    private lateinit var adapter: KaryawanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = KaryawanDatabaseHelper(requireContext())
        setupRecyclerView()
        loadKaryawan()

        binding.apply {
            btnSearch.setOnClickListener {
                loadKaryawan()
            }

            btnNew.setOnClickListener {
                Toast.makeText(requireContext(), "New coming soon", Toast.LENGTH_SHORT).show()
            }

            btnEdit.setOnClickListener {
                Toast.makeText(requireContext(), "Edit coming soon", Toast.LENGTH_SHORT).show()
            }

            btnDelete.setOnClickListener {
                Toast.makeText(requireContext(), "Delete coming soon", Toast.LENGTH_SHORT).show()

            }

            btnClose.setOnClickListener {
                requireActivity().finish()
            }

            tvTglAwal.setOnClickListener {
                showDatePickerDialog { selectedDate ->
                    binding.tvTglAwal.setText(selectedDate)
                }
            }
        }


    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedYear-${selectedMonth + 1}-${selectedDay}"
                onDateSelected(date)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setupRecyclerView() {
        adapter = KaryawanAdapter()
        binding.apply {
            rvKaryawan.layoutManager = LinearLayoutManager(requireContext())
            rvKaryawan.adapter = adapter
        }

    }

    private fun loadKaryawan() {
        binding.apply {
            val nama = etNamaKaryawan.text.toString()
            val usiaMinStr = etUsiaMin.text.toString()

            val usiaMin = if (usiaMinStr.isNotEmpty()) {
                val usia = usiaMinStr.toIntOrNull()
                if (usia != null && usia in 17..100) usia else null
            } else {
                null
            }

            val tglAwal = tvTglAwal.text.toString().takeIf { it.isNotEmpty() }

            val filteredList = dbHelper.getFilteredKaryawan(nama, usiaMin, tglAwal)
            adapter.submitList(filteredList)

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "Tidak ada data karyawan", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

