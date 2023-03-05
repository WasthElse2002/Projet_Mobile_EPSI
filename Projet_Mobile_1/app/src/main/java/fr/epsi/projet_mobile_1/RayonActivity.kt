package fr.epsi.projet_mobile_1

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okio.IOException
import org.json.JSONObject

class RayonActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rayon)
        showBack()
        setHeaderTitle(getString(R.string.title_activity_rayon))

        val url="https://img.freepik.com/photos-gratuite/rendu-3d-fond-espace-planetes-abstraites-nebuleuse_1048-12994.jpg?w=740&t=st=1673970224~exp=1673970824~hmac=5db29203b7bcddbecb50f50b6bab3920b729055ce5b87aaccc3260362a801f19"
        val rayons= arrayListOf<Rayon>()

        val recyclerviewRayons=findViewById<RecyclerView>(R.id.recyclerviewRayon)
        recyclerviewRayons.layoutManager= LinearLayoutManager(this)
        val rayonAdapter=RayonAdapter(rayons)
        recyclerviewRayons.adapter=rayonAdapter

        val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
        val mRequestURL="https://www.ugarit.online/epsi/categories.json"
        val request = Request.Builder()
            .url(mRequestURL)
            .get()
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body?.string()

                if(data!=null) {
                    val jsStudents = JSONObject(data)
                    val jsArrayStudents = jsStudents.getJSONArray("items")
                    for (i in 0 until jsArrayStudents.length()) {
                        val js = jsArrayStudents.getJSONObject(i)
                        val student = Rayon(
                            js.optString("categoryId", "not found"),
                            js.optString("title", "not found"),
                            js.optString("productUrl", "not found")
                        )
                        rayons.add(student)
                        runOnUiThread(Runnable {
                            rayonAdapter.notifyDataSetChanged()
                        })
                    }
                }
            }

        })
    }
}