package com.owen.week4retrofit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.owen.week4retrofit.adapter.CompanyLogoAdapter
import com.owen.week4retrofit.adapter.CountryAdapter
import com.owen.week4retrofit.adapter.GenreAdapter
import com.owen.week4retrofit.databinding.ActivityMovieDetailBinding
import com.owen.week4retrofit.helper.Const
import com.owen.week4retrofit.model.MovieDetails
import com.owen.week4retrofit.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetail : AppCompatActivity() {

    private lateinit var binding : ActivityMovieDetailBinding
    private lateinit var viewModel: MoviesViewModel
    private lateinit var genreadapter: GenreAdapter
    private lateinit var logocompadapter : CompanyLogoAdapter
    private lateinit var countryadapter :CountryAdapter

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("movie_id",0)
        Toast.makeText(applicationContext, "Movie ID : ${movieId}", Toast.LENGTH_SHORT).show()

        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        viewModel.getMovieDetails(movieId,Const.API_KEY)
        viewModel.movieDetails.observe(this, Observer {
            response->
            binding.tvTitleMovieDetail.apply {
                text = response.title
            }
        })

        binding.backgroundLoad.visibility = View.VISIBLE

        viewModel.movieDetails.observe(this) { response ->
                binding.backgroundLoad.visibility = View.INVISIBLE
                //Genre
                binding.rvGenre.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
                genreadapter = GenreAdapter(response.genres)
                binding.rvGenre.adapter = genreadapter

                //Product Company Logo
                binding.rvProductCompanyLogo.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
                logocompadapter = CompanyLogoAdapter(response.production_companies)
                binding.rvProductCompanyLogo.adapter = logocompadapter

                //Production Country
                binding.rvProductionCountry.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
                countryadapter = CountryAdapter(response.production_countries)
                binding.rvProductionCountry.adapter = countryadapter

                //Poster Movie
                binding.tvTitleMovieDetail.apply {
                    text=response.title
                Glide.with(applicationContext)
                    .load(Const.IMG_URL + response.backdrop_path)
                    .into(binding.imgPosterMovieDetail)
                }

                //Movies Overview
                binding.tvOverview.apply {
                    text=response.overview
                }
        }


    }
}