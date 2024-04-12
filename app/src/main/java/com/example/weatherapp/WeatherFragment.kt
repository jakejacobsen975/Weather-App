package com.example.weatherapp

import PollWorker
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.databinding.FragmentWeatherBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

private const val TAG = "WeatherListFragment"

class WeatherFragment : Fragment(){
    private var _binding: FragmentWeatherBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater,container,false)
        weatherViewModel.fetchWeather("84790")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val workRequest = OneTimeWorkRequest
            .Builder(PollWorker::class.java)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(requireContext())
            .enqueue(workRequest)
        context?.let { PollWorker.scheduleWeatherCheck(it) }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weatherRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.submitButton.setOnClickListener {
            val newWeatherData = binding.editTextField.text.toString()
            weatherViewModel.fetchWeather(newWeatherData)
            binding.editTextField.text.clear()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.weatherItems.combine(weatherViewModel.cityName) { items, cityName ->
                    Pair(items, cityName)
                }.collect { (items, cityName) ->
                    Log.d(TAG, "Items received: $items")
                    Log.d(TAG, "City name received: $cityName")
                    binding.weatherRecyclerView.adapter = WeatherListAdapter(items)
                    binding.cityTitle.text = cityName ?: ""
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.isWeatherDataFetchedSuccessfully.collect { isSuccess ->
                    if (!isSuccess) {
                        Toast.makeText(
                            requireContext(),
                            "Try again or check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}