package com.kyc.wallyv3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.kyc.wallyv3.databinding.ActivityHomeBinding
import okhttp3.*
import org.json.JSONException
import java.io.IOException

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var photoAdapter: PhotoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)


        val client = OkHttpClient()
        val url = "https://api.slingacademy.com/v1/sample-data/photos?offset=5&limit=30"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        try {
                            val jsonString = response.body?.string()
                            val gson = Gson()
                            val photoResponse: Wallpapers =
                                gson.fromJson(jsonString, Wallpapers::class.java)

                            val photoAdapter = PhotoAdapter(photoResponse.photos)
                            val layoutManager = LinearLayoutManager(this@HomeActivity)
                            binding.photoRecyclerView.layoutManager = layoutManager
                            binding.photoRecyclerView.adapter = photoAdapter


                            photoAdapter.onItemClick = {
                                val intent = Intent(this@HomeActivity, PhotoActivity::class.java)
                                intent.putExtra("photo", it)
                                startActivity(intent)
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
    }

    class PhotoAdapter(private val photo: List<Photo>) :
        RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

        var onItemClick: ((Photo) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false)
            return PhotoViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            val photo = photo[position]


            Glide.with(holder.itemView.context)
                .load(photo.url)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(holder.photoImageView)

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(photo)
            }

        }

        override fun getItemCount(): Int {
            return photo.size
        }

        class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        }
    }
}