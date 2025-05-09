package com.aditya.sta.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aditya.sta.ui.home.Karyawan

class KaryawanDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "karyawan.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_KARYAWAN = "Karyawan"
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
        CREATE TABLE IF NOT EXISTS tb_karyawan (
            IDKaryawan TEXT PRIMARY KEY,
            NmKaryawan TEXT,
            Usia INTEGER,
            TglMasukKerja TEXT
        )
        """.trimIndent()
        )

        val insertData = """
        INSERT INTO tb_karyawan (IDKaryawan, NmKaryawan, TglMasukKerja, Usia) VALUES
        ('001', 'Andi', '2012-03-02', 25),
        ('002', 'Anto', '2013-02-10', 24),
        ('003', 'Adi',  '2010-05-21', 27),
        ('004', 'Amin', '2018-08-05', 31);
    """.trimIndent()

        db.execSQL(insertData)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_KARYAWAN")
        onCreate(db)
    }

    fun getFilteredKaryawan(
        nama: String?,
        usiaMin: Int?,
        tglAwal: String?,
    ): List<Karyawan> {
        val db = readableDatabase
        val list = mutableListOf<Karyawan>()

        val selection = mutableListOf<String>()
        val args = mutableListOf<String>()

        if (!nama.isNullOrEmpty()) {
            selection.add("NmKaryawan LIKE ?")
            args.add("%$nama%")
        }

        if (usiaMin != null) {
            selection.add("Usia >= ?")
            args.add(usiaMin.toString())
        }

        if (!tglAwal.isNullOrEmpty()) {
            selection.add("TglMasukKerja >= ?")
            args.add(tglAwal)
        }

        val whereClause = if (selection.isNotEmpty()) selection.joinToString(" AND ") else null

        val cursor =
            db.query("tb_karyawan", null, whereClause, args.toTypedArray(), null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val karyawan = Karyawan(
                    id = cursor.getString(cursor.getColumnIndexOrThrow("IDKaryawan")),
                    nama = cursor.getString(cursor.getColumnIndexOrThrow("NmKaryawan")),
                    usia = cursor.getInt(cursor.getColumnIndexOrThrow("Usia")),
                    tglMasuk = cursor.getString(cursor.getColumnIndexOrThrow("TglMasukKerja"))
                )

                list.add(karyawan)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return list
    }


}
