package com.kyc.wallyv3

import android.app.WallpaperManager
import android.content.ContentValues
import android.database.Cursor
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kyc.wallyv3.databinding.ActivityPhotoBinding
import com.bumptech.glide.Glide
import okio.IOException

class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoBinding
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        dbHelper = DBHelper(this)

        val photo = intent.getParcelableExtra<Photo>("photo")
        if (photo != null) {
            Glide.with(this).load(photo.url).into(binding.imageView)
        }

        val photoList = getFav()

        val isFavorite = photoList.contains(photo?.id ?: -1)

        updateFavoriteView(isFavorite)

        binding.imageView2.setOnClickListener {
            val isCurrentlyFav = photoList.contains(photo?.id ?: -1)
            if (isCurrentlyFav) {
                removeFav(photo?.id ?: -1)
            } else {
                insertFav(photo?.id ?: -1, photoList)
            }
            updateFavoriteView(!isCurrentlyFav)
        }

        binding.setWallpaper.setOnClickListener {
            val wallManager = WallpaperManager.getInstance(applicationContext)
            val bmpImg = (binding.imageView.drawable as BitmapDrawable).bitmap
            try {
                wallManager.setBitmap(bmpImg)
                Toast.makeText(
                    this@PhotoActivity,
                    "Wallpaper set successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: IOException) {
                Toast.makeText(this@PhotoActivity, "Setting Wallpaper Failed!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun insertFav(id: Int, photoList: List<Int>) {
        val db = dbHelper.writableDatabase
        if (photoList.contains(id)) {
            removeFav(id)
        } else {
            val values = ContentValues().apply {
                put(DBHelper.PHOTO_ID, id)
            }
            db?.insert(DBHelper.TABLE_NAME, null, values)
        }
    }


    private fun removeFav(id: Int) {
        val db = dbHelper.writableDatabase
        val selection = "${DBHelper.PHOTO_ID} = ?"
        val selectionArg = arrayOf(id.toString())
        db?.delete(DBHelper.TABLE_NAME, selection, selectionArg)
    }


    private fun updateFavoriteView(isFavorite: Boolean) {
        if (isFavorite) {
            binding.imageView2.setImageResource(R.drawable.ic_baseline_image_24)
        } else {
            binding.imageView2.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }

    private fun getFav(): List<Int> {
        val photoList = mutableListOf<Int>()
        val db = dbHelper.readableDatabase

        val cursor: Cursor? = db?.query(
            DBHelper.TABLE_NAME,
            arrayOf(DBHelper.PHOTO_ID),
            null,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val photoID = it.getInt(it.getColumnIndexOrThrow(DBHelper.PHOTO_ID))
                photoList.add((photoID))
            }
        }
        return photoList
    }

}