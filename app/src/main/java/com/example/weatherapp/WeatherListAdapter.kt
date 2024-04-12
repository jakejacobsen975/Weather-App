package com.example.weatherapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.api.WeatherItem
import com.example.weatherapp.databinding.ListItemWeatherBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "WeatherListAdapter"


class WeatherViewHolder (
    private val binding: ListItemWeatherBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(weatherItem: WeatherItem) {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(weatherItem.date)

        val dayOfMonth = SimpleDateFormat("dd", Locale.getDefault()).format(date)
        val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)

        val maxTemp = weatherItem.maxTemp.toInt().toString()
        var minTemp = weatherItem.minTemp.toInt().toString()
        if(maxTemp == minTemp){
            minTemp = ""
        }else{
            minTemp = "/"+ minTemp + "°"
        }
        binding.dateTime.text = "$dayOfWeek, $dayOfMonth, $time"
        binding.maxTemp.text = maxTemp + "°"
        binding.minTemp.text =  minTemp
        binding.humidity.text = "Humidity: " + weatherItem.humidity.toString()+"%"
        binding.weather.text =  weatherItem.weather
        binding.rainPercent.text = (weatherItem.rainPercent*100).toInt().toString()+"%"
        val iconCode = weatherItem.weatherIcon
        Picasso.get()
            .load("https://openweathermap.org/img/wn/$iconCode@2x.png")
            .into(binding.WeatherIcon)

    }
}
class WeatherListAdapter(
    private val weatherItems: List<WeatherItem>
) : RecyclerView.Adapter<WeatherViewHolder>(){

    init {
        Log.d(TAG, "Adapter item count: ${weatherItems.size}")
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemWeatherBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${position}")
        val item = weatherItems[position]
        holder.bind(item)
    }




    override fun getItemCount() = weatherItems.size
}
